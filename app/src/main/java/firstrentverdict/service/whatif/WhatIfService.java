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
     * Creates new VerdictInput with adjustments applied.
     * Original remains unchanged (immutable pattern).
     */
    private VerdictInput applyAdjustments(VerdictInput original, WhatIfRequest adjustments) {
        int newRent = adjustments.adjustedRent() != null
                ? adjustments.adjustedRent()
                : original.monthlyRent();

        int newCash = original.availableCash() +
                (adjustments.cashInjection() != null ? adjustments.cashInjection() : 0);

        // Return new immutable instance, maintaining other parameters from original
        return new VerdictInput(
                original.city(),
                original.state(),
                newRent,
                newCash,
                original.hasPet(),
                original.isLocalMove(),
                null // session token or other metadata if any
        );
    }
}
