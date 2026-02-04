package firstrentverdict.model.verdict;

public enum CreditTier {
    EXCELLENT("750+", 1.0),
    GOOD("670-749", 1.0),
    FAIR("580-669", 1.5),
    POOR("300-579", 2.0);

    private final String range;
    private final double depositMultiplierDetails;

    CreditTier(String range, double depositMultiplierDetails) {
        this.range = range;
        this.depositMultiplierDetails = depositMultiplierDetails;
    }

    public String getRange() {
        return range;
    }

    // Suggested multiplier for risk assessment logic
    public double getMultiplierFactor() {
        return depositMultiplierDetails;
    }
}
