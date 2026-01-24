package firstrentverdict.model.verdict;

public record MarketPosition(
        int p25Rent,
        int medianRent,
        int p75Rent,
        int userRent,
        String marketZone // "Below Market", "Market Range", "Premium"
) {
}
