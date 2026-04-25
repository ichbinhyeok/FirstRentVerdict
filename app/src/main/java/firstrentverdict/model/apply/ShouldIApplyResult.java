package firstrentverdict.model.apply;

import java.util.List;

public record ShouldIApplyResult(
        ApplicationDecision decision,
        String summary,
        int totalMoveInCash,
        int preApprovalCashRisk,
        int postMoveCash,
        int recommendedBuffer,
        int moveInCashGap,
        int requiredMonthlyIncome,
        String incomeStatus,
        String decisionBasis,
        int blockerCount,
        int pauseGateCount,
        String primaryAction,
        String nextBestMove,
        String propertyMessage,
        int cashNeededToClear,
        int saferMonthlyRentTarget,
        String constraintLabel,
        String approvalPath,
        List<String> negotiationMoves,
        List<String> applySafeChanges,
        List<String> decisionSteps,
        String applicationRuleSummary,
        String depositRuleSummary,
        String dataCoverageNote,
        List<ApplicationCostLine> costLines,
        List<ApplicationRiskFactor> riskFactors,
        List<String> questionsToAsk) {
}
