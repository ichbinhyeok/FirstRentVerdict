package firstrentverdict.service.verdict;

import firstrentverdict.model.verdict.Verdict;

/**
 * Context information for generating verdict text.
 * 
 * Contains all data needed by VerdictTextGenerator to produce
 * contextual, verdict-specific explanations.
 */
public record VerdictContext(
        Verdict verdict,
        BottleneckType bottleneck,
        int remainingCash,
        int recommendedBuffer,
        String cityName) {
}
