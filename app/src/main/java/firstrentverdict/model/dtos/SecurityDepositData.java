package firstrentverdict.model.dtos;

import java.util.List;

public record SecurityDepositData(
        String city,
        String state,
        Double legalCapMultiplier,
        List<Double> typicalMultipliers,
        List<Double> highRiskMultipliers,
        String notes) {
}
