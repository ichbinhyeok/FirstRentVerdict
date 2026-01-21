package firstrentverdict.service.verdict;

import org.springframework.stereotype.Component;

/**
 * Identifies the single primary bottleneck preventing approval.
 * 
 * Analyzes financial snapshot to determine the most critical constraint
 * following strict priority order from most to least severe.
 */
@Component
public class BottleneckAnalyzer {

    /**
     * Analyzes financial snapshot to identify primary bottleneck.
     * 
     * Priority order:
     * 1. IMMEDIATE_INSOLVENCY (remaining < 0)
     * 2. CRITICAL_LIQUIDITY_RISK (remaining < 50% of recommended)
     * 3. THIN_BUFFER_WARNING (remaining < recommended)
     * 4. APPROVED (remaining >= recommended)
     */
    public BottleneckType analyze(FinancialSnapshot snapshot) {
        if (snapshot.remainingBuffer() < 0) {
            return BottleneckType.IMMEDIATE_INSOLVENCY;
        }

        if (snapshot.remainingBuffer() < (snapshot.recommendedBuffer() * 0.5)) {
            return BottleneckType.CRITICAL_LIQUIDITY_RISK;
        }

        if (snapshot.remainingBuffer() < snapshot.recommendedBuffer()) {
            return BottleneckType.THIN_BUFFER_WARNING;
        }

        return BottleneckType.APPROVED;
    }

    /**
     * Converts bottleneck type to user-facing display text.
     */
    public String toDisplayText(BottleneckType type) {
        return type.getDisplayText();
    }
}
