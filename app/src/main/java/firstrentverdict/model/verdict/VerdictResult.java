package firstrentverdict.model.verdict;

import java.util.List;

public record VerdictResult(
        Verdict verdict,
        String summary,
        List<String> breakdown,
        List<String> riskFactors,
        Financials financials,
        String primaryDistressFactor,
        String legalProtectionNote) {
    public record Financials(
            int totalUpfrontCost,
            int remainingBuffer,
            int recommendedBuffer) {
    }
}
