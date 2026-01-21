package firstrentverdict.model.dtos;

/**
 * Capture user's "what if" adjustments to original parameters.
 * Only handles rent adjustment and cash injection in this version.
 */
public record WhatIfRequest(
        Integer adjustedRent, // Nullable - only if user wants to change rent
        Integer cashInjection // Nullable - additional cash to add
) {
    /**
     * Validates that at least ONE adjustment is provided.
     */
    public WhatIfRequest {
        if (adjustedRent == null && cashInjection == null) {
            throw new IllegalArgumentException(
                    "At least one adjustment parameter must be provided");
        }

        if (adjustedRent != null && adjustedRent < 0) {
            throw new IllegalArgumentException("Adjusted rent cannot be negative");
        }
        if (cashInjection != null && cashInjection < 0) {
            throw new IllegalArgumentException("Cash injection cannot be negative");
        }
    }
}
