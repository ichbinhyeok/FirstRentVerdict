package firstrentverdict.model.apply;

public record ApplicationCostLine(
        String label,
        int amount,
        String note,
        boolean beforeApprovalRisk) {
}
