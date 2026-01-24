package firstrentverdict.service.core;

import firstrentverdict.model.dtos.*;
import firstrentverdict.model.verdict.*;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.verdict.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // We prioritize legal cap if it exists.
        double multiplier = depositData.typicalMultipliers().get(0); // Default to first typical
        boolean cappedByLaw = false;

        if (depositData.legalCapMultiplier() != null) {
            if (multiplier >= depositData.legalCapMultiplier()) {
                multiplier = depositData.legalCapMultiplier();
                cappedByLaw = true;
            }
        }

        int depositCost = (int) (input.monthlyRent() * multiplier);

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
        String primaryDistress = null;

        if (remainingCash < 0) {
            verdict = Verdict.DENIED;
            primaryDistress = "Immediate Insolvency (Cannot pay upfront costs)";
        } else if (remainingCash < (recommendedBuffer * 0.5)) {
            verdict = Verdict.DENIED;
            primaryDistress = "Critical Liquidity Risk (<50% of Safe Buffer)";
        } else if (remainingCash < recommendedBuffer) {
            verdict = Verdict.BORDERLINE;
            primaryDistress = "Thin Buffer Warning (50-99% of Safe Buffer)";
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
        int gapAmount = remainingCash - recommendedBuffer;
        String actionPrompt = gapAmount < 0
                ? "Reduce rent or increase available cash"
                : "Maintain this buffer for emergencies";
        SafetyGap safetyGap = new SafetyGap(gapAmount, actionPrompt, verdict == Verdict.APPROVED);

        // ==========================================================================

        return new VerdictResult(
                verdict,
                whyThisVerdict,
                primaryBottleneck,
                contributingFactors,
                regionalContext,
                safetyGap,
                new VerdictResult.Financials(input.monthlyRent(), totalUpfront, remainingCash, recommendedBuffer));
    }
}
