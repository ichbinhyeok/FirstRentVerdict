package firstrentverdict.service.verdict;

import firstrentverdict.model.verdict.Verdict;

/**
 * Context information for generating verdict text.
 * 
 * Contains all data needed by VerdictTextGenerator to produce
 * contextual, city-specific, narrative-driven explanations.
 * 
 * ENHANCED: Now includes market tier, deposit notes, and state info
 * for richer, more persuasive verdict messaging.
 */
public record VerdictContext(
                // Core verdict data
                Verdict verdict,
                BottleneckType bottleneck,
                int remainingCash,
                int recommendedBuffer,

                // City/State info
                String cityName,
                String stateName,

                // Market context (from data)
                MarketTier marketTier,
                String depositLegalNote, // From security_deposit.json notes
                boolean hasPet,

                // Risk indicators
                boolean isHighRiskDeposit,
                double depositMultiplier) {

        /**
         * Market tier classification for narrative generation
         */
        public enum MarketTier {
                HIGH_COST("high-cost"), // Median > $2000
                MODERATE("moderate"), // $1200-$2000
                AFFORDABLE("affordable"); // < $1200

                private final String label;

                MarketTier(String label) {
                        this.label = label;
                }

                public String getLabel() {
                        return label;
                }
        }
}
