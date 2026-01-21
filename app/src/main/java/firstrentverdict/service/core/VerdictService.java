package firstrentverdict.service.core;

import firstrentverdict.model.dtos.*;
import firstrentverdict.model.verdict.*;
import firstrentverdict.repository.VerdictDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VerdictService {

    private final VerdictDataRepository repository;

    public VerdictService(VerdictDataRepository repository) {
        this.repository = repository;
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
        List<String> breakdown = new ArrayList<>();
        breakdown.add(String.format("First Month Rent: $%d", input.monthlyRent()));
        breakdown.add(String.format("Security Deposit: $%d (%.1fx rent)", depositCost, multiplier));
        breakdown.add(String.format("Moving Cost: ~$%,d", movingCost));
        if (input.hasPet())
            breakdown.add(String.format("Pet Fees: ~$%,d", petOneTime));

        List<String> riskFactors = new ArrayList<>();
        if (input.hasPet())
            riskFactors.add("Pet owners face limited housing options and non-refundable fees.");
        if (remainingCash < recommendedBuffer)
            riskFactors.add("Post-move cash buffer is below recommended safety levels.");

        String summary = generateSummary(verdict, remainingCash, recommendedBuffer);

        String legalNote = null;
        if (cappedByLaw) {
            legalNote = String.format(
                    "Note: Deposit strictly limited to %.1fx rent by state law. Without this protection, market rates might be higher.",
                    depositData.legalCapMultiplier());
        } else if (depositData.legalCapMultiplier() != null) {
            legalNote = String.format("State law limits deposits to %.1fx rent.", depositData.legalCapMultiplier());
        }

        // ==========================================================================
        // PHASE 1 STUB: Populate new 4-layer fields with temporary values
        // TODO Phase 2: Replace with BottleneckAnalyzer + VerdictTextGenerator
        // ==========================================================================

        // Layer 2: Why This Verdict (STUB - hardcoded for now)
        String whyThisVerdict = generateWhyThisVerdictStub(verdict, remainingCash, recommendedBuffer);
        String primaryBottleneck = primaryDistress != null ? primaryDistress : "APPROVED";

        // Layer 3: Contributing Factors (STUB - using existing riskFactors temporarily)
        List<String> contributingFactors = riskFactors.size() > 3
                ? riskFactors.subList(0, 3)
                : new ArrayList<>(riskFactors);

        // Layer 4: Regional Context (STUB)
        RegionalContext regionalContext = new RegionalContext(
                input.city() + ", " + input.state(),
                List.of(
                        "This market typically requires strong upfront liquidity",
                        "Rental competition favors financially prepared applicants"),
                totalUpfront > 5000 // Simple heuristic
        );

        // Safety Gap (CALCULATED)
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
                new VerdictResult.Financials(totalUpfront, remainingCash, recommendedBuffer),
                // DEPRECATED fields (keep for backward compatibility)
                summary,
                breakdown,
                riskFactors,
                primaryDistress,
                legalNote);
    }

    /**
     * PHASE 1 STUB: Temporary text generation
     * TODO Phase 2: Replace with VerdictTextGenerator
     */
    private String generateWhyThisVerdictStub(Verdict verdict, int remaining, int recommended) {
        // ENFORCE 250 char limit
        if (verdict == Verdict.APPROVED) {
            return "You have sufficient buffer to cover upfront costs and maintain financial safety. " +
                    "This move is financially viable based on current data.";
        } else if (verdict == Verdict.BORDERLINE) {
            return "Your upfront costs will consume most available cash, leaving minimal safety margin. " +
                    "This creates vulnerability to unexpected expenses.";
        } else {
            if (remaining < 0) {
                return String.format(
                        "Your upfront requirement exceeds available funds by $%,d. " +
                                "This creates immediate insolvency with no recovery path.",
                        Math.abs(remaining));
            } else {
                return String.format(
                        "While you can cover upfront costs, only $%,d remains—far below the $%,d safety threshold. " +
                                "One emergency could trigger financial crisis.",
                        remaining, recommended);
            }
        }
    }

    private String generateSummary(Verdict verdict, int remaining, int recommended) {
        if (verdict == Verdict.APPROVED) {
            return "You are in a strong financial position. Your estimated entry costs leave you with a healthy safety buffer.";
        } else if (verdict == Verdict.BORDERLINE) {
            return "You can technically afford to move in, but your remaining cash reserves will be dangerously low. You are one minor emergency away from stress.";
        } else {
            return "This move poses a high financial risk. You likely do not have enough cash to cover upfront costs and maintain a basic safety net.";
        }
    }
}
