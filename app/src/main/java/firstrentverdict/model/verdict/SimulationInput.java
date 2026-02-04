package firstrentverdict.model.verdict;

import java.time.LocalDate;

public record SimulationInput(
        String city,
        String state,
        int monthlyRent,
        int availableCash,
        boolean hasPet,

        // Extended Simulation Fields
        String fromCity, // For long-distance moves
        String fromState,
        CreditTier creditTier, // Credit score tier
        boolean isLocalMove, // Explicit move type toggle
        String petType, // "dog", "cat" (optional refinement)

        LocalDate moveInDate) {
}
