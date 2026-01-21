package firstrentverdict.model.verdict;

/**
 * Safety Gap represents the financial buffer status.
 * Negative gapAmount = user is short of safety threshold
 * Positive gapAmount = user has buffer beyond threshold
 */
public record SafetyGap(
        int gapAmount, // Negative = short, Positive = buffer
        String actionPrompt, // "$500 Short → Adjust Rent or Add Cash"
        boolean isApproved) {
    /**
     * Generates user-facing display text for the safety gap.
     * Examples:
     * "+$2,000 Safety Buffer" (approved)
     * "$500 Short of Safety" (denied)
     */
    public String toDisplayText() {
        if (isApproved) {
            return String.format("+$%,d Safety Buffer", gapAmount);
        } else {
            return String.format("$%,d Short of Safety", Math.abs(gapAmount));
        }
    }
}
