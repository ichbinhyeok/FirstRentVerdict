package firstrentverdict.service.verdict;

/**
 * Represents the single primary bottleneck preventing approval.
 * 
 * Priority order (from most to least severe):
 * 1. IMMEDIATE_INSOLVENCY - Cannot cover upfront costs
 * 2. CRITICAL_LIQUIDITY_RISK - Remaining cash < 50% of recommended buffer
 * 3. THIN_BUFFER_WARNING - Remaining cash < recommended buffer
 * 4. APPROVED - No bottleneck
 */
public enum BottleneckType {
    IMMEDIATE_INSOLVENCY("Immediate Insolvency (Cannot pay upfront costs)"),
    CRITICAL_LIQUIDITY_RISK("Critical Liquidity Risk (<50% of Safe Buffer)"),
    THIN_BUFFER_WARNING("Thin Buffer Warning (50-99% of Safe Buffer)"),
    APPROVED("APPROVED");

    private final String displayText;

    BottleneckType(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
