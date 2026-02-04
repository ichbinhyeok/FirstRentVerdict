package firstrentverdict.service.core;

import firstrentverdict.model.dtos.*;
import firstrentverdict.model.verdict.*;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.verdict.*;
import firstrentverdict.service.calc.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class VerdictService {

    // Cities that typically require broker fees (10-15% of rent)
    private static final Set<String> BROKER_FEE_CITIES = Set.of(
            "New York", "Boston", "Jersey City", "Hoboken");
    private static final double BROKER_FEE_PERCENT = 0.10; // Conservative 10%

    private final VerdictDataRepository repository;
    private final BottleneckAnalyzer bottleneckAnalyzer;
    private final VerdictTextGenerator textGenerator;
    private final MovingCostCalculator movingCostCalculator;
    private final DistanceCalculator distanceCalculator;

    public VerdictService(
            VerdictDataRepository repository,
            BottleneckAnalyzer bottleneckAnalyzer,
            VerdictTextGenerator textGenerator,
            MovingCostCalculator movingCostCalculator,
            DistanceCalculator distanceCalculator) {
        this.repository = repository;
        this.bottleneckAnalyzer = bottleneckAnalyzer;
        this.textGenerator = textGenerator;
        this.movingCostCalculator = movingCostCalculator;
        this.distanceCalculator = distanceCalculator;
    }

    public VerdictResult assessVerdict(VerdictInput input) {
        return simulateVerdict(new SimulationInput(
                input.city(), input.state(), input.monthlyRent(), input.availableCash(),
                input.hasPet(), null, null, CreditTier.GOOD, true, null, input.moveInDate()));
    }

    public VerdictResult simulateVerdict(SimulationInput input) {
        return calculateCoreLogic(input);
    }

    private VerdictResult calculateCoreLogic(SimulationInput input) {
        // 1. Validate City
        if (!repository.isValidCity(input.city(), input.state())) {
            throw new IllegalArgumentException("Unsupported city: " + input.city() + ", " + input.state());
        }

        // 2. Load All City Data
        SecurityDepositData depositData = repository.getSecurityDeposit(input.city(), input.state()).orElseThrow();
        MovingData.CityMoving movingData = repository.getMoving(input.city(), input.state()).orElseThrow();
        PetData.CityPet petData = repository.getPet(input.city(), input.state()).orElseThrow();
        CashBufferData.CityBuffer bufferData = repository.getCashBuffer(input.city(), input.state()).orElseThrow();
        RentData.CityRent rentData = repository.getRent(input.city(), input.state()).orElseThrow();

        // 3. Calculate Security Deposit with Risk Assessment
        // 3. Calculate Security Deposit with Risk Assessment
        double depositMult = 1.0;
        String depositRiskNote = "Standard Rate";
        boolean isHighRisk = input.availableCash() < (input.monthlyRent() * 3);

        if (input.creditTier() == CreditTier.POOR || input.creditTier() == CreditTier.FAIR) {
            isHighRisk = true;
        }

        String depositLegalNote = null;

        if (depositData != null && depositData.city_practice() != null) {
            var practice = depositData.city_practice();
            depositLegalNote = practice.notes();

            if (input.creditTier() != null) {
                switch (input.creditTier()) {
                    case POOR -> {
                        depositMult = practice.highRiskMultipliers().stream().mapToDouble(d -> d).max().orElse(2.0);
                        depositRiskNote = "High Risk (Poor Credit)";
                    }
                    case FAIR -> {
                        double typical = practice.typicalMultipliers().isEmpty() ? 1.0
                                : practice.typicalMultipliers().get(0);
                        depositMult = Math.min(typical * 1.5,
                                practice.highRiskMultipliers().stream().mapToDouble(d -> d).min().orElse(2.0));
                        depositRiskNote = "Moderate Risk (Fair Credit)";
                    }
                    default -> {
                        depositMult = practice.typicalMultipliers().isEmpty() ? 1.0
                                : practice.typicalMultipliers().get(0);
                        depositRiskNote = "Standard Rate (" + input.creditTier() + ")";
                    }
                }
            } else {
                if (isHighRisk && practice.highRiskMultipliers() != null && !practice.highRiskMultipliers().isEmpty()) {
                    depositMult = practice.highRiskMultipliers().stream()
                            .mapToDouble(Double::doubleValue)
                            .max()
                            .orElse(1.0);
                    depositRiskNote = String.format("Risk-Adjusted (%.1fx Rent)", depositMult);
                } else if (practice.typicalMultipliers() != null && !practice.typicalMultipliers().isEmpty()) {
                    depositMult = practice.typicalMultipliers().get(0);
                    depositRiskNote = String.format("Standard (%.1fx Rent)", depositMult);
                }
            }
        }
        int depositCost = (int) (input.monthlyRent() * depositMult);

        // 4. Moving Cost
        int movingCost;
        if (input.isLocalMove()) {
            movingCost = movingData.typical();
        } else {
            double distance = 0.0;
            if (input.fromCity() != null && input.fromState() != null) {
                var fromCoord = repository.getCityCoordinate(input.fromCity(), input.fromState());
                var toCoord = repository.getCityCoordinate(input.city(), input.state());
                if (fromCoord.isPresent() && toCoord.isPresent()) {
                    distance = distanceCalculator.calculateMiles(
                            fromCoord.get().lat(), fromCoord.get().lng(),
                            toCoord.get().lat(), toCoord.get().lng());
                }
            }
            movingCost = movingCostCalculator.calculateCost(false, distance, movingData);
        }

        // 5. Pet Costs
        int petOneTime = 0;
        int petMonthlyRent = 0;
        if (input.hasPet()) {
            petOneTime = petData.oneTime().avg();
            if (petData.monthlyPetRent() != null) {
                petMonthlyRent = petData.monthlyPetRent().avg();
            }
        }

        // 6. Broker Fee (NYC, Boston, etc.)
        int brokerFee = 0;
        if (BROKER_FEE_CITIES.contains(input.city())) {
            brokerFee = (int) (input.monthlyRent() * BROKER_FEE_PERCENT);
        }

        // 7. Total Upfront Calculation
        int totalUpfront = input.monthlyRent() + depositCost + movingCost + petOneTime + petMonthlyRent + brokerFee;
        int remainingCash = input.availableCash() - totalUpfront;
        int recommendedBuffer = bufferData.recommendedPostMoveBuffer();

        // 8. Determine Verdict
        Verdict verdict;
        if (remainingCash < 0) {
            verdict = Verdict.DENIED;
        } else if (remainingCash < (recommendedBuffer * 0.5)) {
            verdict = Verdict.DENIED;
        } else if (remainingCash < recommendedBuffer) {
            verdict = Verdict.BORDERLINE;
        } else {
            verdict = Verdict.APPROVED;
        }

        // 9. Calculate Market Tier
        VerdictContext.MarketTier marketTier = calculateMarketTier(rentData.median());

        // 10. Build Enhanced Cost Breakdown
        List<FinancialLineItem> costBreakdown = buildCostBreakdown(
                input, depositCost, depositRiskNote, depositLegalNote,
                movingData, petData, petOneTime, petMonthlyRent, brokerFee);

        // 11. Bottleneck Analysis
        FinancialSnapshot snapshot = new FinancialSnapshot(
                input.monthlyRent(), input.availableCash(), totalUpfront,
                remainingCash, recommendedBuffer, verdict);
        BottleneckType bottleneck = bottleneckAnalyzer.analyze(snapshot);
        String primaryBottleneck = bottleneckAnalyzer.toDisplayText(bottleneck);

        // 12. Generate WHY text with Enhanced Context
        VerdictContext context = new VerdictContext(
                verdict, bottleneck, remainingCash, recommendedBuffer,
                input.city(), input.state(), marketTier, depositLegalNote,
                input.hasPet(), isHighRisk, depositMult);
        String whyThisVerdict = textGenerator.generate(context);

        // 13. Build DYNAMIC Contributing Factors (from actual data)
        List<String> contributingFactors = buildContributingFactors(
                input, isHighRisk, depositLegalNote, remainingCash, recommendedBuffer, brokerFee);

        // 14. Build DYNAMIC Regional Context (no more hardcoding!)
        RegionalContext regionalContext = buildRegionalContext(
                input, rentData, depositData, marketTier, totalUpfront);

        // 15. Safety Gap Calculation
        SafetyGap safetyGap = calculateSafetyGap(verdict, bottleneck, remainingCash, recommendedBuffer);

        // 16. Market Position for Radar
        MarketPosition marketPosition = buildMarketPosition(input, rentData);

        // 17. Calculate simulation multipliers
        double simTypicalMult = getTypicalMultiplier(depositData);
        double simHighRiskMult = getHighRiskMultiplier(depositData, simTypicalMult);

        // 18. Build Final Result
        int staticCosts = movingCost + petOneTime + petMonthlyRent + brokerFee;
        double baseMultiplier = 1.0 + depositMult;

        return new VerdictResult(
                verdict,
                whyThisVerdict,
                primaryBottleneck,
                contributingFactors,
                regionalContext,
                safetyGap,
                new VerdictResult.Financials(
                        input.monthlyRent(), totalUpfront, input.availableCash(),
                        remainingCash, recommendedBuffer, baseMultiplier,
                        simTypicalMult, simHighRiskMult, staticCosts, costBreakdown),
                marketPosition);
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    private VerdictContext.MarketTier calculateMarketTier(int medianRent) {
        if (medianRent >= 2000)
            return VerdictContext.MarketTier.HIGH_COST;
        if (medianRent >= 1200)
            return VerdictContext.MarketTier.MODERATE;
        return VerdictContext.MarketTier.AFFORDABLE;
    }

    /**
     * Builds itemized cost breakdown with annotations from source data
     */
    private List<FinancialLineItem> buildCostBreakdown(
            SimulationInput input, int depositCost, String depositRiskNote, String depositLegalNote,
            MovingData.CityMoving movingData, PetData.CityPet petData,
            int petOneTime, int petMonthlyRent, int brokerFee) {

        List<FinancialLineItem> items = new ArrayList<>();

        // First Month Rent
        items.add(new FinancialLineItem(
                "First Month Rent", input.monthlyRent(), "Applied Baseline: User Input"));

        // Security Deposit with legal context
        String depositAnnotation = depositRiskNote;
        if (depositLegalNote != null && !depositLegalNote.isEmpty()) {
            depositAnnotation += " · " + truncate(depositLegalNote, 80);
        }
        items.add(new FinancialLineItem("Security Deposit", depositCost, depositAnnotation));

        // Moving Costs with range
        String movingAnnotation = String.format("Range: $%,d - $%,d · %s",
                movingData.low(), movingData.high(),
                movingData.assumptions() != null ? movingData.assumptions() : "Local Move");
        items.add(new FinancialLineItem("Moving Costs", movingData.typical(), movingAnnotation));

        // Broker Fee (if applicable)
        if (brokerFee > 0) {
            items.add(new FinancialLineItem(
                    "Est. Broker Fee",
                    brokerFee,
                    "Common in this market (10-15% typical) · May vary by listing"));
        }

        // Pet Fees
        if (input.hasPet()) {
            String petAnnotation = String.format("Range: $%,d - $%,d · %s",
                    petData.oneTime().low(), petData.oneTime().high(),
                    petData.oneTime().notes() != null ? petData.oneTime().notes() : "One-time deposit/fee");
            items.add(new FinancialLineItem("Pet Deposit/Fee", petOneTime, petAnnotation));

            if (petMonthlyRent > 0) {
                String petMonthlyAnnotation = String.format("$%d/month ongoing · Range: $%,d - $%,d/mo",
                        petMonthlyRent, petData.monthlyPetRent().low(), petData.monthlyPetRent().high());
                items.add(new FinancialLineItem("Pet Rent (1st Month)", petMonthlyRent, petMonthlyAnnotation));
            }
        }

        return items;
    }

    /**
     * Builds contributing factors from ACTUAL data (no hardcoding)
     */
    private List<String> buildContributingFactors(
            SimulationInput input, boolean isHighRisk, String depositLegalNote,
            int remainingCash, int recommendedBuffer, int brokerFee) {

        List<String> factors = new ArrayList<>();

        // High-risk deposit assessment
        if (isHighRisk) {
            factors.add("Low cash-to-rent ratio may trigger higher deposit requirements");
        }

        // Pet-related factor
        if (input.hasPet()) {
            factors.add("Pet owners face additional fees and limited housing options");
        }

        // Broker fee market
        if (brokerFee > 0) {
            factors.add("This market commonly requires broker fees at move-in");
        }

        // Buffer status
        if (remainingCash < recommendedBuffer && remainingCash >= 0) {
            factors.add("Post-move reserves fall below recommended safety threshold");
        } else if (remainingCash < 0) {
            factors.add("Available funds insufficient for total move-in costs");
        }

        // State-specific deposit law (from data)
        if (depositLegalNote != null && depositLegalNote.length() > 10) {
            // Only add if it's meaningful legal context
            if (depositLegalNote.toLowerCase().contains("law") ||
                    depositLegalNote.toLowerCase().contains("cap") ||
                    depositLegalNote.toLowerCase().contains("limit")) {
                factors.add("State law affects deposit limits in this area");
            }
        }

        // Limit to 3 max
        return factors.size() > 3 ? factors.subList(0, 3) : factors;
    }

    /**
     * Builds DYNAMIC regional context based on actual market data
     */
    private RegionalContext buildRegionalContext(
            SimulationInput input, RentData.CityRent rentData, SecurityDepositData depositData,
            VerdictContext.MarketTier marketTier, int totalUpfront) {

        List<String> contextFactors = new ArrayList<>();
        String cityStateName = input.city() + ", " + input.state();

        // Market tier context
        switch (marketTier) {
            case HIGH_COST -> contextFactors.add(
                    "High-cost market: Landlords expect strong financial profiles");
            case MODERATE -> contextFactors.add(
                    "Competitive market: Well-prepared applicants have advantages");
            case AFFORDABLE -> contextFactors.add(
                    "Accessible market: But upfront costs still require planning");
        }

        // State-specific deposit context (from actual data)
        if (depositData != null && depositData.city_practice() != null &&
                depositData.city_practice().notes() != null) {
            String note = depositData.city_practice().notes();
            if (note.toLowerCase().contains("law") || note.toLowerCase().contains("cap")) {
                contextFactors.add("Deposit regulated by state/local law");
            } else if (note.toLowerCase().contains("credit")) {
                contextFactors.add("Credit score impacts deposit requirements here");
            }
        }

        // Rent percentile context
        if (rentData.p75() > 0) {
            double rentRatio = (double) rentData.median() / rentData.p75();
            if (rentRatio < 0.7) {
                contextFactors.add("Wide rent variance: Room to negotiate or find deals");
            }
        }

        // Limit to 3
        if (contextFactors.size() > 3) {
            contextFactors = contextFactors.subList(0, 3);
        }

        boolean isHighCost = marketTier == VerdictContext.MarketTier.HIGH_COST || totalUpfront > 5000;
        return new RegionalContext(cityStateName, contextFactors, isHighCost);
    }

    private SafetyGap calculateSafetyGap(Verdict verdict, BottleneckType bottleneck,
            int remainingCash, int recommendedBuffer) {
        int gapAmount;
        if (verdict == Verdict.DENIED && bottleneck == BottleneckType.IMMEDIATE_INSOLVENCY) {
            gapAmount = remainingCash;
        } else {
            gapAmount = remainingCash - recommendedBuffer;
        }

        String actionPrompt;
        if (bottleneck == BottleneckType.IMMEDIATE_INSOLVENCY) {
            actionPrompt = "Lower rent or add cash to remove insolvency risk";
        } else {
            actionPrompt = gapAmount < 0
                    ? "Reduce rent or increase available cash"
                    : "Maintain this buffer for emergencies";
        }

        String displayText;
        if (verdict == Verdict.APPROVED) {
            displayText = String.format("+$%,d Safety Buffer", gapAmount);
        } else {
            displayText = String.format("$%,d Short of Safety", Math.abs(gapAmount));
        }

        return new SafetyGap(gapAmount, actionPrompt, displayText, verdict == Verdict.APPROVED);
    }

    private MarketPosition buildMarketPosition(SimulationInput input, RentData.CityRent rentData) {
        String marketZone;
        if (input.monthlyRent() <= rentData.p25()) {
            marketZone = "Below Market";
        } else if (input.monthlyRent() >= rentData.p75()) {
            marketZone = "Premium Range";
        } else {
            marketZone = "Market Standard";
        }

        return new MarketPosition(
                rentData.p25(), rentData.median(), rentData.p75(),
                input.monthlyRent(), marketZone);
    }

    private double getTypicalMultiplier(SecurityDepositData depositData) {
        if (depositData != null && depositData.city_practice() != null
                && depositData.city_practice().typicalMultipliers() != null
                && !depositData.city_practice().typicalMultipliers().isEmpty()) {
            return depositData.city_practice().typicalMultipliers().get(0);
        }
        return 1.0;
    }

    private double getHighRiskMultiplier(SecurityDepositData depositData, double fallback) {
        if (depositData != null && depositData.city_practice() != null
                && depositData.city_practice().highRiskMultipliers() != null
                && !depositData.city_practice().highRiskMultipliers().isEmpty()) {
            return depositData.city_practice().highRiskMultipliers().stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(fallback);
        }
        return fallback;
    }

    private String truncate(String text, int maxLen) {
        if (text == null)
            return "";
        if (text.length() <= maxLen)
            return text;
        return text.substring(0, maxLen - 3) + "...";
    }
}
