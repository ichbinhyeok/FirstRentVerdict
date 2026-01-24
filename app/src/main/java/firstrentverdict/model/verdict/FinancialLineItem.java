package firstrentverdict.model.verdict;

public record FinancialLineItem(
        String label,
        int amount,
        String annotation // e.g. "Rule: Legal Cap", "Applied Standard: 1x Rent"
) {
}
