package firstrentverdict.service.whatif;

import firstrentverdict.model.dtos.WhatIfRequest;
import firstrentverdict.model.verdict.VerdictInput;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.core.VerdictService;
import org.springframework.stereotype.Service;

/**
 * Applies adjustments to original VerdictInput and delegates to VerdictService.
 * Follows immutable pattern for input modifications.
 */
@Service
public class WhatIfService {

    private final VerdictService verdictService;
    private final VerdictDataRepository repository;

    public WhatIfService(VerdictService verdictService, VerdictDataRepository repository) {
        this.verdictService = verdictService;
        this.repository = repository;
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
        int targetRent = repository.getRent(original.city(), original.state())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot apply market correction for unsupported city: " + original.city() + ", " + original.state()))
                .p25();
        return recalculate(original, new WhatIfRequest(targetRent, null));
    }

    // Allows callers to run market-correction with an explicit target rent.
    public VerdictResult simulateMarketCorrection(VerdictInput original, int targetP25Rent) {
        if (targetP25Rent < 0) {
            throw new IllegalArgumentException("Target corrected rent cannot be negative");
        }
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
