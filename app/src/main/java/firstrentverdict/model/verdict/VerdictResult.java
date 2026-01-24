package firstrentverdict.model.verdict;

import java.util.List;

/**
 * VerdictResult represents the complete judgment for a rent affordability
 * assessment.
 * 
 * 4-LAYER STRUCTURE (ENFORCED):
 * - Layer 1: Verdict Badge (verdict)
 * - Layer 2: Why This Verdict (whyThisVerdict) - MAX 250 CHARS
 * - Layer 3: Contributing Factors (contributingFactors) - MAX 3 ITEMS
 * - Layer 4: Regional Context (regionalContext)
 * 
 * Safety Gap is ALWAYS visible and drives user re-simulation behavior.
 */
public record VerdictResult(
                // ========== Layer 1: Verdict Badge ==========
                Verdict verdict,

                // ========== Layer 2: Why This Verdict (3-4 lines MAX) ==========
                String whyThisVerdict, // Core explanation (max 250 chars)
                String primaryBottleneck, // Single reason identifier

                // ========== Layer 3: Contributing Factors (2-3 items) ==========
                List<String> contributingFactors, // Contextual bullets (max 3)

                // ========== Layer 4: Regional Context ==========
                RegionalContext regionalContext,

                // ========== Safety Gap (Always Visible) ==========
                SafetyGap safetyGap,

                // ========== Financial Data ==========
                Financials financials,

                // ========== Market Position ==========
                MarketPosition marketPosition) {

        /**
         * Validation enforced at construction time
         */
        public VerdictResult {
                if (verdict == null)
                        throw new IllegalArgumentException("Verdict cannot be null");
                if (financials == null)
                        throw new IllegalArgumentException("Financials cannot be null");
                if (marketPosition == null)
                        throw new IllegalArgumentException("MarketPosition cannot be null");

                // Layer 2 validation: 250 char limit
                if (whyThisVerdict != null && whyThisVerdict.length() > 250) {
                        throw new IllegalArgumentException(
                                        "Why This Verdict must be <= 250 chars (got: " + whyThisVerdict.length() + ")");
                }

                // Layer 3 validation: max 3 contributing factors
                if (contributingFactors != null && contributingFactors.size() > 3) {
                        throw new IllegalArgumentException(
                                        "Contributing Factors limited to 3 items max (got: "
                                                        + contributingFactors.size() + ")");
                }

                // Primary bottleneck required for non-approved verdicts
                if (verdict != Verdict.APPROVED && (primaryBottleneck == null || primaryBottleneck.isBlank())) {
                        throw new IllegalArgumentException("Primary bottleneck required for non-approved verdicts");
                }
        }

        public record Financials(
                        int monthlyRent,
                        int totalUpfrontCost,
                        int availableCash, // Needed for Math Proof
                        int remainingBuffer,
                        int recommendedBuffer,
                        double upfrontBaseMultiplier, // e.g. 2.5 (1 + deposit multiplier)
                        int staticCosts, // moving + pet + fees
                        List<FinancialLineItem> costBreakdown) {
        }
}
