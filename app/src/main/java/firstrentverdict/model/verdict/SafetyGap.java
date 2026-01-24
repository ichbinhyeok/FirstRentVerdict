package firstrentverdict.model.verdict;

/**
 * Safety Gap represents the financial buffer status.
 * Negative gapAmount = user is short of safety threshold
 * Positive gapAmount = user has buffer beyond threshold
 */
public record SafetyGap(
        int gapAmount, // Negative = short, Positive = buffer
        String actionPrompt, // "$500 Short → Adjust Rent or Add Cash"
        String displayText, // "+$2,000 Safety Buffer" or "$500 Short of Safety"
        boolean isApproved) {
}
