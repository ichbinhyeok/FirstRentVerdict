package firstrentverdict.service.verdict;

import firstrentverdict.model.verdict.Verdict;

/**
 * Snapshot of financial state for bottleneck analysis.
 * 
 * Immutable record capturing the key financial metrics needed to identify
 * the primary constraint preventing approval.
 */
public record FinancialSnapshot(
        int monthlyRent,
        int availableCash,
        int totalUpfrontCost,
        int remainingBuffer,
        int recommendedBuffer,
        Verdict verdict) {
}
