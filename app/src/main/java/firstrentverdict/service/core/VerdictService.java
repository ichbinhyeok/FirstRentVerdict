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
        // Security Deposit Logic
        double depositMult = 1.0;
        if (depositData != null && depositData.city_practice() != null) {
            if (depositData.city_practice().typicalMultipliers() != null
                    && !depositData.city_practice().typicalMultipliers().isEmpty()) {
                depositMult = depositData.city_practice().typicalMultipliers().get(0);
            }
        }
        int depositCost = (int) (input.monthlyRent() * depositMult);

        // Moving Cost
        // Use 'typical' for now. Could act on input.isLocalMove() later if data
        // supported non-local.
        int movingCost = movingData.typical();

        // Pet Cost
        int petOneTime = 0;
        if (input.hasPet()) {
            petOneTime = petData.oneTime().avg();
        }

        // Total Upfront
        int totalUpfront = input.monthlyRent() + depositCost + movingCost + petOneTime;
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
        int staticCosts = movingCost + petOneTime;

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
        String depositAnnotation;
        // Simplified annotation logic as legal cap data is now in notes
        if (depositData != null && depositData.city_practice() != null && depositData.city_practice().notes() != null
                && !depositData.city_practice().notes().isEmpty()) {
            depositAnnotation = "Applied Standard · " + depositData.city_practice().notes();
        } else {
            depositAnnotation = "Applied Standard · Required by Lease Terms";
        }

        costBreakdown.add(new FinancialLineItem(
                "Security Deposit",
                depositCost,
                depositAnnotation));

        // 3. Moving Costs
        costBreakdown.add(new FinancialLineItem(
                "Moving Costs",
                movingCost,
                "Applied Baseline · Required Execution Cost"));

        // 4. Pet Fees
        if (input.hasPet()) {
            String petAnnotation;
            // Check if "Non-refundable" is explicitly mentioned in notes (stub logic based
            // on known data pattern)
            // In a real DB we would have a structured flag. Here we check the note string.
            boolean isNonRefundable = petData.oneTime().notes() != null &&
                    (petData.oneTime().notes().toLowerCase().contains("non-refundable") ||
                            petData.oneTime().notes().toLowerCase().contains("fee"));

            if (isNonRefundable) {
                petAnnotation = "Rule: Non-refundable Fee (Market Norm)";
            } else {
                petAnnotation = "Applied Baseline: Market Rate";
            }
            costBreakdown.add(new FinancialLineItem(
                    "Pet Fees",
                    petOneTime,
                    petAnnotation));
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
                        staticCosts, // moving + pet + fees
                        costBreakdown),
                marketPosition);
    }
}
