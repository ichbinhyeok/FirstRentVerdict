package firstrentverdict.service.core;
import firstrentverdict.model.apply.ApplicationDecision;
import firstrentverdict.model.apply.ShouldIApplyInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
class ShouldIApplyServiceTest {
    @Autowired
    private ShouldIApplyService service;
    @Test
    void cashGapIsHardDoNotApplyGate() {
        var result = service.assess(new ShouldIApplyInput(
                "Austin", "TX",
                1800,
                2500,
                7000,
                1,
                75,
                150,
                0,
                0,
                0,
                1800,
                true,
                0,
                12,
                false,
                0,
                0,
                0,
                100,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.DO_NOT_APPLY, result.decision());
        assertTrue(result.blockerCount() > 0);
        assertTrue(result.decisionBasis().contains("cash gap"));
        assertTrue(result.primaryAction().contains("Do not pay"));
        assertTrue(result.cashNeededToClear() > 0);
        assertTrue(result.constraintLabel().contains("cash"));
        assertTrue(result.approvalPath().contains("lower cash schedule"));
        assertTrue(result.negotiationMoves().stream().anyMatch(move -> move.contains("upfront cash")));
        assertTrue(result.applySafeChanges().stream().anyMatch(change -> change.contains("Lower upfront cash")));
        assertTrue(result.decisionSteps().stream().anyMatch(step -> step.contains("Hard blocker")));
    }
    @Test
    void unknownIncomePausesInsteadOfApproving() {
        var result = service.assess(new ShouldIApplyInput(
                "Austin", "TX",
                1200,
                15000,
                null,
                1,
                50,
                0,
                0,
                0,
                0,
                1200,
                true,
                0,
                12,
                false,
                0,
                0,
                0,
                0,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.PAUSE, result.decision());
        assertEquals(0, result.blockerCount());
        assertTrue(result.pauseGateCount() > 0);
        assertTrue(result.decisionBasis().contains("income unknown"));
        assertTrue(result.propertyMessage().contains("written screening criteria"));
        assertTrue(result.negotiationMoves().stream().anyMatch(move -> move.contains("income rule")));
        assertTrue(result.applySafeChanges().stream().anyMatch(change -> change.contains("Enter verified monthly income")));
    }
    @Test
    void cleanScenarioCanApply() {
        var result = service.assess(new ShouldIApplyInput(
                "Austin", "TX",
                1200,
                15000,
                5000,
                1,
                50,
                0,
                0,
                0,
                0,
                1200,
                true,
                0,
                12,
                false,
                0,
                0,
                0,
                0,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.APPLY, result.decision());
        assertEquals(0, result.blockerCount());
        assertEquals(0, result.pauseGateCount());
        assertTrue(result.nextBestMove().contains("Save the written answers"));
        assertTrue(result.negotiationMoves().stream().anyMatch(move -> move.contains("payment schedule")));
        assertTrue(result.applySafeChanges().stream().anyMatch(change -> change.contains("Stop and re-run")));
    }
    @Test
    void moveInFeeCountsAsPreApprovalCashAtRisk() {
        var result = service.assess(new ShouldIApplyInput(
                "Austin", "TX",
                1200,
                15000,
                5000,
                1,
                50,
                0,
                0,
                350,
                0,
                1200,
                true,
                0,
                12,
                false,
                0,
                0,
                0,
                0,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.PAUSE, result.decision());
        assertEquals(400, result.preApprovalCashRisk());
        assertTrue(result.decisionBasis().contains("high pre-approval cash at risk"));
        assertTrue(result.riskFactors().stream()
                .anyMatch(factor -> factor.label().contains("Pre-approval cash at risk")));
        assertTrue(result.costLines().stream()
                .anyMatch(line -> line.label().equals("Move-in fee") && line.beforeApprovalRisk()));
    }
    @Test
    void refundablePreApprovalChargesReduceCashAtRisk() {
        var result = service.assess(new ShouldIApplyInput(
                "Austin", "TX",
                1200,
                15000,
                5000,
                1,
                50,
                200,
                0,
                350,
                0,
                1200,
                true,
                0,
                12,
                false,
                0,
                0,
                0,
                0,
                true,
                true,
                false,
                true,
                false,
                "2026-05-15",
                "3x rent"));
        assertEquals(0, result.preApprovalCashRisk());
        assertTrue(result.riskFactors().stream()
                .noneMatch(factor -> factor.label().contains("Pre-approval cash at risk")));
        assertTrue(result.costLines().stream()
                .anyMatch(line -> line.label().equals("Move-in fee") && !line.beforeApprovalRisk()));
    }
    @Test
    void applicationFeeIsMultipliedByApplicantCount() {
        var result = service.assess(new ShouldIApplyInput(
                "Austin", "TX",
                1600,
                9000,
                5200,
                2,
                75,
                200,
                0,
                0,
                0,
                1600,
                true,
                0,
                12,
                false,
                0,
                0,
                0,
                0,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.PAUSE, result.decision());
        assertEquals(350, result.preApprovalCashRisk());
        assertTrue(result.costLines().stream()
                .anyMatch(line -> line.label().equals("Application fee") && line.amount() == 150));
    }
    @Test
    void monthlyPetRentCountsTowardIncomeScreenAndMoveInCash() {
        var result = service.assess(new ShouldIApplyInput(
                "Austin", "TX",
                1600,
                12000,
                4800,
                1,
                50,
                0,
                0,
                0,
                0,
                1600,
                true,
                0,
                12,
                true,
                0,
                300,
                75,
                0,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.DO_NOT_APPLY, result.decision());
        assertEquals(5025, result.requiredMonthlyIncome());
        assertTrue(result.riskFactors().stream()
                .anyMatch(factor -> factor.label().contains("Income screen")));
        assertTrue(result.costLines().stream()
                .anyMatch(line -> line.label().equals("Pet charges") && line.amount() == 375));
    }
    @Test
    void saferRentTargetUsesEnteredDepositAndPrepaidRentAsVariableCosts() {
        var result = service.assess(new ShouldIApplyInput(
                "Orlando", "FL",
                1750,
                8000,
                6500,
                1,
                75,
                300,
                0,
                0,
                0,
                3500,
                true,
                1750,
                12,
                false,
                0,
                0,
                0,
                150,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.DO_NOT_APPLY, result.decision());
        assertTrue(result.saferMonthlyRentTarget() > 0);
        assertTrue(result.saferMonthlyRentTarget() < 1400,
                "Entered 2x deposit and 1x prepaid rent should not be diluted into a one-month market estimate.");
        assertTrue(result.applySafeChanges().stream()
                .anyMatch(change -> change.contains("Search near")));
    }
    @Test
    void massachusettsLastMonthRentDoesNotFailDepositCapByItself() {
        var result = service.assess(new ShouldIApplyInput(
                "Boston", "MA",
                2950,
                14000,
                11000,
                1,
                20,
                0,
                0,
                0,
                0,
                2950,
                true,
                2950,
                12,
                false,
                0,
                0,
                0,
                100,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertTrue(result.riskFactors().stream()
                .noneMatch(factor -> factor.label().contains("Deposit/prepaid rent over loaded cap")));
        assertTrue(result.decision() != ApplicationDecision.DO_NOT_APPLY);
    }
    @Test
    void policyBlockerExplainsPolicyPathWithoutNonsenseRentTarget() {
        var result = service.assess(new ShouldIApplyInput(
                "New York", "NY",
                3000,
                20000,
                12000,
                1,
                75,
                0,
                0,
                0,
                0,
                3000,
                true,
                0,
                12,
                false,
                0,
                0,
                0,
                100,
                false,
                false,
                false,
                false,
                false,
                "",
                "3x rent"));
        assertEquals(ApplicationDecision.DO_NOT_APPLY, result.decision());
        assertTrue(result.approvalPath().contains("flagged fee/deposit term"));
        assertTrue(result.nextBestMove().contains("lower or separate"));
        assertTrue(result.decisionSteps().stream().noneMatch(step -> step.contains("next search target")));
    }
}

