package firstrentverdict.service.whatif;

import firstrentverdict.model.dtos.WhatIfRequest;
import firstrentverdict.model.verdict.VerdictInput;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.service.core.VerdictService;
import org.springframework.stereotype.Service;

/**
 * Applies adjustments to original VerdictInput and delegates to VerdictService.
 * Follows immutable pattern for input modifications.
 */
@Service
public class WhatIfService {

    private final VerdictService verdictService;

    public WhatIfService(VerdictService verdictService) {
        this.verdictService = verdictService;
    }

    /**
     * Applies what-if adjustments to original input and re-calculates verdict.
     * 
     * @param original    The original VerdictInput from session
     * @param adjustments User's what-if adjustments
     * @return New VerdictResult with adjusted parameters
     */
    public VerdictResult recalculate(VerdictInput original, WhatIfRequest adjustments) {
        if (original == null) {
            throw new IllegalStateException("No original verdict found in session");
        }

        VerdictInput adjusted = applyAdjustments(original, adjustments);
        return verdictService.assessVerdict(adjusted);
    }

    /**
     * Applies "Market Correction" scenario (p25 rent)
     */
    public VerdictResult recalculateWithMarketCorrection(VerdictInput original) {
        if (original == null) {
            throw new IllegalStateException("No original verdict found in session");
        }

        // We will fetch p25 data via VerdictService (or Repository directly).
        // Ideally WhatIfService should have access to Repository.
        // For now, we will assume the caller might pass the target rent or we fetch it
        // here.
        // Let's refactor to inject Repository if needed, but VerdictService calculates
        // MarketPosition.
        // Actually, cleaner way:
        // We let Controller pass the target rent (which it can get from
        // originalResult.marketPosition().p25Rent()).
        // But WhatIfService logic is best place.

        // Stub for now: The controller will likely call a variant or we add logic here.
        // Let's stick to the plan: WhatIfService implements the logic.
        // We need repository access to get p25 if it's not passed.
        // But `VerdictResult` already has `MarketPosition`.
        // So the Controller can extract p25 from result and pass it as a regular
        // "WhatIfRequest"
        // OR we add a specific method.

        // Let's support a specific method that takes the p25 value directly to avoid
        // circular dependency or extra lookups.
        return null; // Placeholder as we decide architecture.
    }

    // Actual implementation using existing `recalculate` but with a helper for p25
    public VerdictResult simulateMarketCorrection(VerdictInput original, int targetP25Rent) {
        return recalculate(original, new WhatIfRequest(targetP25Rent, null));
    }

    private VerdictInput applyAdjustments(VerdictInput original, WhatIfRequest adjustments) {
        int newRent = adjustments.adjustedRent() != null
                ? adjustments.adjustedRent()
                : original.monthlyRent();

        int newCash = original.availableCash() +
                (adjustments.cashInjection() != null ? adjustments.cashInjection() : 0);

        return new VerdictInput(
                original.city(),
                original.state(),
                newRent,
                newCash,
                original.hasPet(),
                original.isLocalMove(),
                original.creditTier(),
                original.moveInDate());
    }
}
