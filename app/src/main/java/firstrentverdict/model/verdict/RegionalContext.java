package firstrentverdict.model.verdict;

import java.util.List;

/**
 * Regional Context provides city-specific factors that explain
 * why the verdict may be stricter in certain markets.
 * This is NOT general information - it's judicial context.
 */
public record RegionalContext(
        String cityName,
        List<String> contextFactors, // 2-3 bullets max
        boolean isHighCost) {
    /**
     * Validates context factor count (max 3)
     */
    public RegionalContext {
        if (contextFactors != null && contextFactors.size() > 3) {
            throw new IllegalArgumentException("Regional context limited to 3 factors max");
        }
    }
}
