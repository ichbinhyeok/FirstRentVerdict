package firstrentverdict.model.verdict;

import java.time.LocalDate;

public record VerdictInput(
        String city,
        String state,
        int monthlyRent,
        int availableCash,
        boolean hasPet,
        boolean isLocalMove,
        LocalDate moveInDate // Optional, can be null
) {
}
