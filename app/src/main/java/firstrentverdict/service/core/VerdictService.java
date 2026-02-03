package firstrentverdict.service.core;

import firstrentverdict.model.dtos.*;
import firstrentverdict.model.verdict.*;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.verdict.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerdictService {

    private final VerdictDataRepository repository;
    private final BottleneckAnalyzer bottleneckAnalyzer;
    private final VerdictTextGenerator textGenerator;

    public VerdictService(
            VerdictDataRepository repository,
            BottleneckAnalyzer bottleneckAnalyzer,
            VerdictTextGenerator textGenerator) {
        this.repository = repository;
        this.bottleneckAnalyzer = bottleneckAnalyzer;
        this.textGenerator = textGenerator;
    }

    public VerdictResult assessVerdict(VerdictInput input) {
        // 1. Validate City
        if (!repository.isValidCity(input.city(), input.state())) {
            throw new IllegalArgumentException("Unsupported city: " + input.city() + ", " + input.state());
        }

        // 2. Load Data
        SecurityDepositData depositData = repository.getSecurityDeposit(input.city(), input.state()).orElseThrow();
        MovingData.CityMoving movingData = repository.getMoving(input.city(), input.state()).orElseThrow();
        PetData.CityPet petData = repository.getPet(input.city(), input.state()).orElseThrow();
        CashBufferData.CityBuffer bufferData = repository.getCashBuffer(input.city(), input.state()).orElseThrow();

        // 3. Calculate Components

        // Security Deposit Logic: Strict Legal Cap
        // If state has a cap, we use Math.min(typical, cap).
        // Security Deposit Logic: Dynamic Risk Assessment
        // If available cash is tight (< 3x rent), landlords may perceive higher risk.
        // We simulate this by checking 'high_risk_multipliers' if available in data.

        double depositMult = 1.0;
        String depositRiskNote = "Standard Rate";
        boolean isHighRisk = input.availableCash() < (input.monthlyRent() * 3);

        if (depositData != null && depositData.city_practice() != null) {
            var practice = depositData.city_practice();

            if (isHighRisk && practice.highRiskMultipliers() != null && !practice.highRiskMultipliers().isEmpty()) {
                // Use the highest multiplier for conservative estimation
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
        int depositCost = (int) (input.monthlyRent() * depositMult);

        // Moving Cost
        // Use 'typical' for now. Could act on input.isLocalMove() later if data
        // supported non-local.
        int movingCost = movingData.typical();

        // Pet Cost (One-time + First Month's Pet Rent)
        int petOneTime = 0;
        int petMonthlyRent = 0;
        if (input.hasPet()) {
            petOneTime = petData.oneTime().avg();
            if (petData.monthlyPetRent() != null) {
                petMonthlyRent = petData.monthlyPetRent().avg();
            }
        }

        // Total Upfront (includes first month pet rent)
        int totalUpfront = input.monthlyRent() + depositCost + movingCost + petOneTime + petMonthlyRent;
        int remainingCash = input.availableCash() - totalUpfront;
        int recommendedBuffer = bufferData.recommendedPostMoveBuffer();

        // 4. Determine Verdict
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

        // 5. Build Result
        List<String> riskFactors = new ArrayList<>();
        if (input.hasPet())
            riskFactors.add("Pet owners face limited housing options and non-refundable fees.");
        if (remainingCash < recommendedBuffer)
            riskFactors.add("Post-move cash buffer is below recommended safety levels.");

        // ==========================================================================
        // PHASE 2: Smart Verdict Generation with BottleneckAnalyzer & TextGenerator
        // ==========================================================================

        // Create financial snapshot for analysis
        FinancialSnapshot snapshot = new FinancialSnapshot(
                input.monthlyRent(),
                input.availableCash(),
                totalUpfront,
                remainingCash,
                recommendedBuffer,
                verdict);

        // Identify primary bottleneck
        BottleneckType bottleneck = bottleneckAnalyzer.analyze(snapshot);
        String primaryBottleneck = bottleneckAnalyzer.toDisplayText(bottleneck);

        // Generate contextual "Why This Verdict" text
        VerdictContext context = new VerdictContext(
                verdict,
                bottleneck,
                remainingCash,
                recommendedBuffer,
                input.city());
        String whyThisVerdict = textGenerator.generate(context);

        // Layer 3: Contributing Factors (using riskFactors for now)
        List<String> contributingFactors = riskFactors.size() > 3
                ? riskFactors.subList(0, 3)
                : new ArrayList<>(riskFactors);

        // Layer 4: Regional Context (stub - to be enhanced in future)
        RegionalContext regionalContext = new RegionalContext(
                input.city() + ", " + input.state(),
                List.of(
                        "This market typically requires strong upfront liquidity",
                        "Rental competition favors financially prepared applicants"),
                totalUpfront > 5000);

        // Safety Gap (calculated)
        // Safety Gap (Canonical Value Rule)
        // IF verdict == DENIED AND bottleneck == IMMEDIATE_INSOLVENCY:
        // gapAmount = remainingCash (negative canonical value)
        // ELSE:
        // gapAmount = remainingCash - recommendedBuffer (gap to safety)

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

        SafetyGap safetyGap = new SafetyGap(gapAmount, actionPrompt, displayText, verdict == Verdict.APPROVED);

        // ==========================================================================

        // Calculate data for Financials
        // Base multiplier = 1 (first month rent) + deposit multiplier
        double baseMultiplier = 1.0 + depositMult;
        int staticCosts = movingCost + petOneTime + petMonthlyRent;

        // ==========================================================================
        // Smart Receipt Logic: Calculate Costs with Annotations
        // ==========================================================================

        List<FinancialLineItem> costBreakdown = new ArrayList<>();

        // 1. First Month Rent
        costBreakdown.add(new FinancialLineItem(
                "First Month Rent",
                input.monthlyRent(),
                "Applied Baseline: User Input"));

        // 2. Security Deposit
        // 2. Security Deposit
        String depositAnnotation;
        String sourceNote = (depositData != null && depositData.city_practice() != null
                && depositData.city_practice().notes() != null)
                        ? depositData.city_practice().notes()
                        : "Required by Lease Terms";

        // Prioritize the Risk Note we calculated earlier
        depositAnnotation = depositRiskNote + " · " + sourceNote;

        costBreakdown.add(new FinancialLineItem(
                "Security Deposit",
                depositCost,
                depositAnnotation));

        // 3. Moving Costs (with range info)
        String movingAnnotation = String.format("Range: $%,d - $%,d · %s",
                movingData.low(), movingData.high(),
                movingData.assumptions() != null ? movingData.assumptions() : "Local Move");
        costBreakdown.add(new FinancialLineItem(
                "Moving Costs",
                movingCost,
                movingAnnotation));

        // 4. Pet Fees (One-time)
        if (input.hasPet()) {
            String petOneTimeAnnotation = String.format("Range: $%,d - $%,d · %s",
                    petData.oneTime().low(), petData.oneTime().high(),
                    petData.oneTime().notes() != null ? petData.oneTime().notes() : "One-time deposit/fee");
            costBreakdown.add(new FinancialLineItem(
                    "Pet Deposit/Fee",
                    petOneTime,
                    petOneTimeAnnotation));

            // 5. Pet Monthly Rent (first month)
            if (petMonthlyRent > 0) {
                String petMonthlyAnnotation = String.format("$%d/month ongoing · Range: $%,d - $%,d/mo",
                        petMonthlyRent,
                        petData.monthlyPetRent().low(), petData.monthlyPetRent().high());
                costBreakdown.add(new FinancialLineItem(
                        "Pet Rent (1st Month)",
                        petMonthlyRent,
                        petMonthlyAnnotation));
            }
        }

        // ==========================================================================
        // Market Radar Logic
        // ==========================================================================
        RentData.CityRent rentData = repository.getRent(input.city(), input.state()).orElse(
                new RentData.CityRent(input.city(), input.state(), 2026, input.monthlyRent(), input.monthlyRent(),
                        input.monthlyRent(), List.of(), null, false));

        String marketZone;
        if (input.monthlyRent() <= rentData.p25()) {
            marketZone = "Below Market";
        } else if (input.monthlyRent() >= rentData.p75()) {
            marketZone = "Premium Range";
        } else {
            marketZone = "Market Standard";
        }

        MarketPosition marketPosition = new MarketPosition(
                rentData.p25(),
                rentData.median(),
                rentData.p75(),
                input.monthlyRent(),
                marketZone);

        // Calculate simulation multipliers
        double simTypicalMult = (depositData != null && depositData.city_practice() != null
                && depositData.city_practice().typicalMultipliers() != null
                && !depositData.city_practice().typicalMultipliers().isEmpty())
                        ? depositData.city_practice().typicalMultipliers().get(0)
                        : 1.0;

        double simHighRiskMult = simTypicalMult;
        if (depositData != null && depositData.city_practice() != null
                && depositData.city_practice().highRiskMultipliers() != null
                && !depositData.city_practice().highRiskMultipliers().isEmpty()) {
            simHighRiskMult = depositData.city_practice().highRiskMultipliers().stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(simTypicalMult);
        }

        return new VerdictResult(
                verdict,
                whyThisVerdict,
                primaryBottleneck,
                contributingFactors,
                regionalContext,
                safetyGap,
                new VerdictResult.Financials(
                        input.monthlyRent(),
                        totalUpfront,
                        input.availableCash(),
                        remainingCash,
                        recommendedBuffer,
                        baseMultiplier, // e.g. 2.5 (1 + deposit multiplier)
                        simTypicalMult,
                        simHighRiskMult,
                        staticCosts, // moving + pet + fees
                        costBreakdown),
                marketPosition);
    }
}
