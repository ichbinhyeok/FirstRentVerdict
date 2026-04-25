package firstrentverdict.service.core;

import firstrentverdict.model.apply.ApplicationCostLine;
import firstrentverdict.model.apply.ApplicationDecision;
import firstrentverdict.model.apply.ApplicationRiskFactor;
import firstrentverdict.model.apply.ShouldIApplyInput;
import firstrentverdict.model.apply.ShouldIApplyResult;
import firstrentverdict.model.dtos.ApplicationFeeRulesData;
import firstrentverdict.model.dtos.CashBufferData;
import firstrentverdict.model.dtos.DepositPrepaidRulesData;
import firstrentverdict.model.dtos.MovingData;
import firstrentverdict.model.dtos.ScreeningIncomeAssumptionsData;
import firstrentverdict.model.dtos.SecurityDepositData;
import firstrentverdict.repository.VerdictDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShouldIApplyService {

    private static final String DEFAULT_INCOME_RULE = "3x rent";

    private final VerdictDataRepository repository;

    public ShouldIApplyService(VerdictDataRepository repository) {
        this.repository = repository;
    }

    public ShouldIApplyResult assess(ShouldIApplyInput input) {
        if (!repository.isValidCity(input.city(), input.state())) {
            throw new IllegalArgumentException("Unsupported city: " + input.city() + ", " + input.state());
        }

        SecurityDepositData depositData = repository.getSecurityDeposit(input.city(), input.state()).orElseThrow();
        MovingData.CityMoving movingData = repository.getMoving(input.city(), input.state()).orElseThrow();
        CashBufferData.CityBuffer bufferData = repository.getCashBuffer(input.city(), input.state()).orElseThrow();
        var applicationRule = repository.getApplicationFeeRule(input.state());
        var depositRule = repository.getDepositPrepaidRule(input.state());
        var incomeAssumption = resolveIncomeAssumption(input.incomeRule());

        int estimatedDeposit = input.securityDeposit() != null
                ? input.securityDeposit()
                : estimateDeposit(input.monthlyRent(), depositData, depositRule);
        int applicationFeeTotal = input.applicationFee() * input.applicantCount();
        int petCost = input.hasPet() ? input.petFee() + input.petDeposit() + input.monthlyPetRent() : 0;
        int recurringMonthlyHousing = input.monthlyRent() + (input.hasPet() ? input.monthlyPetRent() : 0);
        int movingCost = movingData.typical();
        int preApprovalCashRisk = refundable(input.applicationFeeRefundable(), applicationFeeTotal)
                + refundable(input.adminFeeRefundable(), input.adminFee())
                + refundable(input.holdingDepositRefundable(), input.holdingDeposit())
                + refundable(input.moveInFeeRefundable(), input.moveInFee());
        int totalMoveInCash = input.monthlyRent()
                + applicationFeeTotal
                + estimatedDeposit
                + input.prepaidRent()
                + input.moveInFee()
                + input.brokerFee()
                + input.adminFee()
                + input.holdingDeposit()
                + petCost
                + input.utilityDeposit()
                + movingCost;
        int postMoveCash = input.availableCash() - totalMoveInCash;
        int recommendedBuffer = bufferData.recommendedPostMoveBuffer();
        int cashGap = Math.max(0, totalMoveInCash - input.availableCash());
        int requiredMonthlyIncome = (int) Math.ceil(recurringMonthlyHousing * incomeAssumption.rentToIncomeMultiplier());

        List<ApplicationCostLine> costLines = buildCostLines(input, applicationFeeTotal, estimatedDeposit, petCost, movingCost);
        List<ApplicationRiskFactor> riskFactors = new ArrayList<>();
        List<String> questionsToAsk = new ArrayList<>();
        List<String> blockers = new ArrayList<>();
        List<String> pauseGates = new ArrayList<>();

        if (cashGap > 0) {
            blockers.add("cash gap");
            riskFactors.add(new ApplicationRiskFactor(
                    "Move-in cash gap",
                    "high",
                    "You are short $" + format(cashGap) + " before keeping any post-move buffer."));
        } else if (postMoveCash < recommendedBuffer / 2) {
            pauseGates.add("critical buffer");
            riskFactors.add(new ApplicationRiskFactor(
                    "Post-move buffer",
                    "high",
                    "The move leaves less than half of the recommended buffer."));
        } else if (postMoveCash < recommendedBuffer) {
            pauseGates.add("thin reserve");
            riskFactors.add(new ApplicationRiskFactor(
                    "Thin reserve",
                    "medium",
                    "The move works on paper, but the remaining cash is below the local safety buffer."));
        }

        String incomeStatus = assessIncome(input, requiredMonthlyIncome, riskFactors, blockers, pauseGates);

        assessApplicationFeeRisk(input, applicationRule, riskFactors, questionsToAsk, blockers, pauseGates);
        assessDepositRisk(input, estimatedDeposit, depositRule, riskFactors, questionsToAsk, blockers, pauseGates);

        if (preApprovalCashRisk >= 350) {
            pauseGates.add("high pre-approval cash at risk");
            riskFactors.add(new ApplicationRiskFactor(
                "Pre-approval cash at risk",
                "medium",
                "You may pay $" + format(preApprovalCashRisk) + " before knowing whether the property will approve you."));
        }

        assessMissingListingDetails(input, riskFactors, questionsToAsk, pauseGates);

        if (questionsToAsk.isEmpty()) {
            questionsToAsk.add("Can I review written screening criteria before paying any application or admin fee?");
            questionsToAsk.add("Which charges are refundable if I am denied or decide not to sign?");
            questionsToAsk.add("What is the exact cash due at application, approval, lease signing, and move-in?");
        }

        ApplicationDecision decision = decide(blockers, pauseGates);
        String decisionBasis = buildDecisionBasis(blockers, pauseGates);
        String summary = buildSummary(decision, cashGap, postMoveCash, recommendedBuffer, incomeStatus);
        String dataCoverageNote = buildDataCoverageNote(applicationRule, depositRule);
        int cashNeededToClear = Math.max(0, totalMoveInCash + recommendedBuffer - input.availableCash());
        int saferMonthlyRentTarget = estimateSaferMonthlyRentTarget(
                input,
                estimatedDeposit,
                petCost,
                movingCost,
                recommendedBuffer,
                depositData,
                depositRule,
                incomeAssumption);
        String primaryAction = buildPrimaryAction(decision, blockers, pauseGates);
        String constraintLabel = buildConstraintLabel(decision, blockers, pauseGates, incomeStatus, cashNeededToClear);
        String nextBestMove = buildNextBestMove(
                decision,
                cashNeededToClear,
                saferMonthlyRentTarget,
                requiredMonthlyIncome,
                incomeStatus,
                preApprovalCashRisk,
                input.monthlyRent(),
                constraintLabel);
        String propertyMessage = buildPropertyMessage(input, decision, cashNeededToClear);
        String approvalPath = buildApprovalPath(decision, constraintLabel, cashNeededToClear, preApprovalCashRisk);
        List<String> negotiationMoves = buildNegotiationMoves(
                input,
                estimatedDeposit,
                cashNeededToClear,
                saferMonthlyRentTarget,
                requiredMonthlyIncome,
                incomeStatus,
                preApprovalCashRisk,
                decision);
        List<String> applySafeChanges = buildApplySafeChanges(
                input,
                estimatedDeposit,
                cashNeededToClear,
                saferMonthlyRentTarget,
                requiredMonthlyIncome,
                incomeStatus,
                preApprovalCashRisk,
                decision,
                constraintLabel,
                blockers,
                pauseGates);
        List<String> decisionSteps = buildDecisionSteps(
                decision,
                blockers,
                pauseGates,
                cashNeededToClear,
                saferMonthlyRentTarget,
                requiredMonthlyIncome,
                preApprovalCashRisk,
                input.monthlyRent());

        return new ShouldIApplyResult(
                decision,
                summary,
                totalMoveInCash,
                preApprovalCashRisk,
                postMoveCash,
                recommendedBuffer,
                cashGap,
                requiredMonthlyIncome,
                incomeStatus,
                decisionBasis,
                blockers.size(),
                pauseGates.size(),
                primaryAction,
                nextBestMove,
                propertyMessage,
                cashNeededToClear,
                saferMonthlyRentTarget,
                constraintLabel,
                approvalPath,
                negotiationMoves,
                applySafeChanges,
                decisionSteps,
                summarizeApplicationRule(applicationRule),
                summarizeDepositRule(depositRule),
                dataCoverageNote,
                costLines,
                riskFactors,
                questionsToAsk.stream().distinct().limit(6).toList());
    }

    private ScreeningIncomeAssumptionsData.IncomeAssumption resolveIncomeAssumption(String incomeRule) {
        String key = incomeRule == null || incomeRule.isBlank() ? DEFAULT_INCOME_RULE : incomeRule;
        return repository.getIncomeAssumption(key)
                .or(() -> repository.getIncomeAssumption(DEFAULT_INCOME_RULE))
                .orElse(new ScreeningIncomeAssumptionsData.IncomeAssumption(
                        DEFAULT_INCOME_RULE,
                        3.0,
                        "standard",
                        "Fallback screening assumption.",
                        false,
                        "low"));
    }

    private int estimateDeposit(
            int monthlyRent,
            SecurityDepositData depositData,
            Optional<DepositPrepaidRulesData.DepositPrepaidRule> depositRule) {

        double multiplier = 1.0;
        if (depositData.city_practice() != null
                && depositData.city_practice().typicalMultipliers() != null
                && !depositData.city_practice().typicalMultipliers().isEmpty()) {
            multiplier = depositData.city_practice().typicalMultipliers().get(0);
        }

        if (depositRule.isPresent() && depositRule.get().depositCapMultiplier() != null) {
            multiplier = Math.min(multiplier, depositRule.get().depositCapMultiplier());
        }

        return (int) Math.round(monthlyRent * multiplier);
    }

    private int estimateSaferMonthlyRentTarget(
            ShouldIApplyInput input,
            int estimatedDeposit,
            int petCost,
            int movingCost,
            int recommendedBuffer,
            SecurityDepositData depositData,
            Optional<DepositPrepaidRulesData.DepositPrepaidRule> depositRule,
            ScreeningIncomeAssumptionsData.IncomeAssumption incomeAssumption) {

        double currentDepositMultiplier = input.monthlyRent() == 0
                ? 1.0
                : Math.max(0.5, estimatedDeposit / (double) input.monthlyRent());
        boolean depositWasEntered = input.securityDeposit() != null;
        if (!depositWasEntered
                && depositData.city_practice() != null
                && depositData.city_practice().typicalMultipliers() != null
                && !depositData.city_practice().typicalMultipliers().isEmpty()) {
            currentDepositMultiplier = depositData.city_practice().typicalMultipliers().get(0);
        }
        if (depositRule.isPresent() && depositRule.get().depositCapMultiplier() != null) {
            currentDepositMultiplier = Math.min(currentDepositMultiplier, depositRule.get().depositCapMultiplier());
        }

        double prepaidRentMultiplier = input.monthlyRent() == 0
                ? 0.0
                : Math.max(0.0, input.prepaidRent() / (double) input.monthlyRent());

        int fixedCash = (input.applicationFee() * input.applicantCount())
                + input.adminFee()
                + input.holdingDeposit()
                + input.moveInFee()
                + input.brokerFee()
                + petCost
                + input.utilityDeposit()
                + movingCost
                + recommendedBuffer;
        int availableForRentAndDeposit = input.availableCash() - fixedCash;
        if (availableForRentAndDeposit <= 0) {
            return 0;
        }
        int target = (int) Math.floor(availableForRentAndDeposit
                / (1.0 + currentDepositMultiplier + prepaidRentMultiplier));
        if (input.grossMonthlyIncome() != null && input.grossMonthlyIncome() > 0) {
            int incomeLimitedTarget = (int) Math.floor(input.grossMonthlyIncome()
                    / incomeAssumption.rentToIncomeMultiplier()
                    - (input.hasPet() ? input.monthlyPetRent() : 0));
            target = Math.min(target, incomeLimitedTarget);
        }
        return Math.max(0, roundDownToNearest25(target));
    }

    private List<ApplicationCostLine> buildCostLines(
            ShouldIApplyInput input,
            int applicationFeeTotal,
            int estimatedDeposit,
            int petCost,
            int movingCost) {
        List<ApplicationCostLine> lines = new ArrayList<>();
        addLine(lines, "Application fee", applicationFeeTotal, refundableNote(
                input.applicationFeeRefundable(),
                input.applicantCount() > 1
                        ? "Application fee multiplied across " + input.applicantCount() + " applicants."
                        : "Money usually spent before approval."), !input.applicationFeeRefundable());
        addLine(lines, "Admin fee", input.adminFee(), refundableNote(
                input.adminFeeRefundable(),
                "Ask whether this is refundable or applied to move-in."), !input.adminFeeRefundable());
        addLine(lines, "Holding deposit", input.holdingDeposit(), refundableNote(
                input.holdingDepositRefundable(),
                "Ask what happens if you are denied or do not sign."), !input.holdingDepositRefundable());
        addLine(lines, "First month's rent", input.monthlyRent(), "Baseline rent due before keys.", false);
        addLine(lines, "Security deposit", estimatedDeposit, input.securityDepositConfirmed()
                ? "Listing-confirmed deposit entered by the user."
                : "Use the listing amount if known; otherwise city/state estimate.", false);
        addLine(lines, "Prepaid rent", input.prepaidRent(), "Includes last month or advance rent if required.", false);
        addLine(lines, "Move-in fee", input.moveInFee(), refundableNote(
                input.moveInFeeRefundable(),
                "Non-rent charge due near approval or move-in."), !input.moveInFeeRefundable());
        addLine(lines, "Broker fee", input.brokerFee(), refundableNote(
                input.brokerFeeRefundable(),
                "Only include if due before or at lease signing."), false);
        addLine(lines, "Pet charges", petCost, "One-time pet fee, pet deposit, and first month pet rent.", false);
        addLine(lines, "Utility deposit", input.utilityDeposit(), "Cash needed before the unit is usable.", false);
        addLine(lines, "Local moving cost", movingCost, "Typical local move estimate for the selected city.", false);
        return lines;
    }

    private void addLine(List<ApplicationCostLine> lines, String label, int amount, String note, boolean beforeApprovalRisk) {
        if (amount > 0) {
            lines.add(new ApplicationCostLine(label, amount, note, beforeApprovalRisk));
        }
    }

    private String assessIncome(
            ShouldIApplyInput input,
            int requiredMonthlyIncome,
            List<ApplicationRiskFactor> riskFactors,
            List<String> blockers,
            List<String> pauseGates) {
        if (input.grossMonthlyIncome() == null || input.grossMonthlyIncome() <= 0) {
            pauseGates.add("income unknown");
            riskFactors.add(new ApplicationRiskFactor(
                    "Income rule unknown",
                    "medium",
                    "No monthly income was entered, so the checker cannot clear the rent-to-income screen."));
            return "Unknown - enter income or ask the property";
        }
        if (input.grossMonthlyIncome() < requiredMonthlyIncome) {
            blockers.add("income below selected assumption");
            riskFactors.add(new ApplicationRiskFactor(
                    "Income screen",
                    "high",
                    "Entered income is below the selected screening assumption."));
            return "Below selected assumption";
        }
        return "Meets selected assumption";
    }

    private void assessApplicationFeeRisk(
            ShouldIApplyInput input,
            Optional<ApplicationFeeRulesData.ApplicationFeeRule> applicationRule,
            List<ApplicationRiskFactor> riskFactors,
            List<String> questionsToAsk,
            List<String> blockers,
            List<String> pauseGates) {
        if (input.applicationFee() <= 0) {
            questionsToAsk.add("Is there any application or screening fee before approval?");
            return;
        }

        if (applicationRule.isEmpty()) {
            pauseGates.add("application fee rule unknown");
            riskFactors.add(new ApplicationRiskFactor(
                    "Application fee rule unknown",
                    "medium",
                    "No state fee rule is loaded yet for " + input.state() + ". Ask before paying."));
            questionsToAsk.add("What law or property policy controls this application or screening fee?");
            return;
        }

        ApplicationFeeRulesData.ApplicationFeeRule rule = applicationRule.get();
        addAll(questionsToAsk, rule.disclosureRequired());
        addAll(questionsToAsk, rule.refundRequiredWhen());

        boolean fixedCap = contains(rule.capType(), "fixed") || contains(rule.capType(), "lesser");
        if (fixedCap && rule.capAmount() != null && input.applicationFee() > rule.capAmount()) {
            blockers.add("application fee above loaded cap");
            riskFactors.add(new ApplicationRiskFactor(
                    "Application fee above loaded cap",
                    "high",
                    input.state() + " seed rule says " + rule.capFormula()));
            return;
        }

        boolean costOnly = contains(rule.capType(), "cost") || contains(rule.capType(), "processing");
        if (costOnly && input.applicationFee() >= 100) {
            pauseGates.add("cost-only fee needs itemization");
            riskFactors.add(new ApplicationRiskFactor(
                    "High cost-only screening charge",
                    "medium",
                    "The loaded rule points to actual/customary processing cost, so ask for the itemization."));
            return;
        }

        questionsToAsk.add(rule.productRiskNote());
    }

    private void assessDepositRisk(
            ShouldIApplyInput input,
            int estimatedDeposit,
            Optional<DepositPrepaidRulesData.DepositPrepaidRule> depositRule,
            List<ApplicationRiskFactor> riskFactors,
            List<String> questionsToAsk,
            List<String> blockers,
            List<String> pauseGates) {
        if (depositRule.isEmpty()) {
            pauseGates.add("deposit rule unknown");
            riskFactors.add(new ApplicationRiskFactor(
                    "Deposit rule unknown",
                    "medium",
                    "No V3 deposit/prepaid-rent seed rule is loaded yet for " + input.state() + "."));
            questionsToAsk.add("Is the security deposit, prepaid rent, and move-in fee stack allowed for this property?");
            return;
        }

        DepositPrepaidRulesData.DepositPrepaidRule rule = depositRule.get();
        questionsToAsk.add(rule.productRiskNote());
        if (rule.prepaidRentLimit() != null && !rule.prepaidRentLimit().isBlank()) {
            questionsToAsk.add("Does prepaid rent count toward the deposit or advance-rent limit here?");
        }

        if (rule.depositCapMultiplier() == null) {
            return;
        }

        int depositLikeCash = prepaidRentCountsAgainstDepositCap(rule)
                ? estimatedDeposit + input.prepaidRent()
                : estimatedDeposit;
        int cap = (int) Math.round(input.monthlyRent() * rule.depositCapMultiplier());
        if (depositLikeCash > cap) {
            blockers.add("deposit/prepaid rent over loaded cap");
            riskFactors.add(new ApplicationRiskFactor(
                    "Deposit/prepaid rent over loaded cap",
                    "high",
                    "Loaded " + input.state() + " seed rule: " + rule.capFormula()));
        }
    }

    private void assessMissingListingDetails(
            ShouldIApplyInput input,
            List<ApplicationRiskFactor> riskFactors,
            List<String> questionsToAsk,
            List<String> pauseGates) {
        if (!input.securityDepositConfirmed()) {
            pauseGates.add("deposit amount not listing-confirmed");
            riskFactors.add(new ApplicationRiskFactor(
                    "Deposit is estimated",
                    "medium",
                    "The security deposit is not marked as listing-confirmed, so the final cash stack can change."));
            questionsToAsk.add("Is the security deposit amount final and listing-specific, or only an estimate?");
        }
        if (input.leaseTermMonths() == null || input.leaseTermMonths() <= 0) {
            questionsToAsk.add("What lease term does this fee and deposit schedule assume?");
        }
        if (input.hasPet() && input.monthlyPetRent() > 0) {
            questionsToAsk.add("Is monthly pet rent included in the income screen or only in monthly payment?");
        }
        if (input.brokerFee() > 0) {
            questionsToAsk.add("When is the broker fee due, and is it owed if the application is denied?");
        }
        if (input.moveInDate() == null || input.moveInDate().isBlank()) {
            questionsToAsk.add("What is the move-in date and the exact due date for each charge?");
        }
    }

    private int refundable(boolean isRefundable, int amount) {
        return isRefundable ? 0 : amount;
    }

    private String refundableNote(boolean isRefundable, String defaultNote) {
        return isRefundable
                ? "Marked refundable or credited by the user; keep written proof."
                : defaultNote;
    }

    private ApplicationDecision decide(List<String> blockers, List<String> pauseGates) {
        if (!blockers.isEmpty()) {
            return ApplicationDecision.DO_NOT_APPLY;
        }
        if (!pauseGates.isEmpty()) {
            return ApplicationDecision.PAUSE;
        }
        return ApplicationDecision.APPLY;
    }

    private String buildPrimaryAction(
            ApplicationDecision decision,
            List<String> blockers,
            List<String> pauseGates) {
        return switch (decision) {
            case DO_NOT_APPLY -> "Do not pay the application fee yet.";
            case PAUSE -> "Pause and get written answers before paying.";
            case APPLY -> "You can apply if the property confirms refundability.";
        };
    }

    private String buildNextBestMove(
            ApplicationDecision decision,
            int cashNeededToClear,
            int saferMonthlyRentTarget,
            int requiredMonthlyIncome,
            String incomeStatus,
            int preApprovalCashRisk,
            int monthlyRent,
            String constraintLabel) {
        if (decision == ApplicationDecision.DO_NOT_APPLY) {
            if (incomeStatus.startsWith("Below")) {
                return "Do not spend the application fee unless the property confirms a different income standard or a valid compensating factor. Target income needed is $" + format(requiredMonthlyIncome) + "/mo.";
            }
            if (constraintLabel.startsWith("Policy")) {
                return "Ask the property to lower or separate the flagged fee/deposit term, then re-run before paying.";
            }
            if (cashNeededToClear > 0 && saferMonthlyRentTarget > 0 && saferMonthlyRentTarget < monthlyRent) {
                return "Look for listings near $" + format(saferMonthlyRentTarget) + "/mo or bring about $" + format(cashNeededToClear) + " more cash before applying.";
            }
            if (cashNeededToClear > 0) {
                return "Bring about $" + format(cashNeededToClear) + " more cash or remove deposit/prepaid charges before applying.";
            }
            return "Treat this as a failed gate and ask the property to resolve the blocker in writing before paying.";
        }
        if (decision == ApplicationDecision.PAUSE) {
            if (preApprovalCashRisk >= 350) {
                return "Ask which pre-approval charges are refundable or credited before risking $" + format(preApprovalCashRisk) + ".";
            }
            if (cashNeededToClear > 0 && constraintLabel.startsWith("Upfront cash")) {
                return "Get the upfront stack lowered by about $" + format(cashNeededToClear) + " or add that cash before paying.";
            }
            return "Use the message below to confirm the unknown gate, then re-run the checker with the answer.";
        }
        return "Save the written answers, then apply through the property only if the amounts match this stack.";
    }

    private String buildPropertyMessage(
            ShouldIApplyInput input,
            ApplicationDecision decision,
            int cashNeededToClear) {
        String opening = "Before I pay the application fee for this listing, can you confirm ";
        if (decision == ApplicationDecision.DO_NOT_APPLY && cashNeededToClear > 0) {
            return opening
                    + "the exact cash due at application, approval, lease signing, and move-in? My current estimate is short by about $"
                    + format(cashNeededToClear)
                    + " after keeping a post-move buffer, so I need the written fee/deposit schedule before applying.";
        }
        return opening
                + "the written screening criteria, which charges are refundable if I am denied or do not sign, and the exact cash due at application, approval, lease signing, and move-in?";
    }

    private String buildConstraintLabel(
            ApplicationDecision decision,
            List<String> blockers,
            List<String> pauseGates,
            String incomeStatus,
            int cashNeededToClear) {
        if (incomeStatus.startsWith("Below")) {
            return "Income screen is the first blocker";
        }
        if (cashNeededToClear > 0 || blockers.stream().anyMatch(blocker -> blocker.contains("cash"))) {
            return "Upfront cash stack is the first blocker";
        }
        if (!blockers.isEmpty()) {
            return "Policy gate is the first blocker";
        }
        if (decision == ApplicationDecision.PAUSE || !pauseGates.isEmpty()) {
            return "Written confirmation is missing";
        }
        return "Listing can move forward if terms match";
    }

    private String buildApprovalPath(
            ApplicationDecision decision,
            String constraintLabel,
            int cashNeededToClear,
            int preApprovalCashRisk) {
        if (decision == ApplicationDecision.DO_NOT_APPLY && constraintLabel.startsWith("Income")) {
            return "This only turns into an apply path if the property confirms a lower income rule, a verified exception, or another documented approval route before payment.";
        }
        if (decision == ApplicationDecision.DO_NOT_APPLY && cashNeededToClear > 0) {
            return "The approval path is not a better score. It is a lower cash schedule, more cash available, or a cheaper listing before you risk the fee.";
        }
        if (decision == ApplicationDecision.DO_NOT_APPLY && constraintLabel.startsWith("Policy")) {
            return "This only turns into an apply path if the property changes the flagged fee/deposit term or confirms a documented exception before payment.";
        }
        if (decision == ApplicationDecision.PAUSE && preApprovalCashRisk > 0) {
            return "The path is to convert the pre-approval charges into written refund or credit terms before you pay.";
        }
        if (decision == ApplicationDecision.PAUSE) {
            return "The path is to close the unknown gate in writing, then re-run the check with the answer.";
        }
        return "The path is procedural: save the screening criteria, refund terms, and payment schedule before submitting the application.";
    }

    private List<String> buildNegotiationMoves(
            ShouldIApplyInput input,
            int estimatedDeposit,
            int cashNeededToClear,
            int saferMonthlyRentTarget,
            int requiredMonthlyIncome,
            String incomeStatus,
            int preApprovalCashRisk,
            ApplicationDecision decision) {
        List<String> moves = new ArrayList<>();
        int depositLikeCash = estimatedDeposit + input.prepaidRent();
        if (decision == ApplicationDecision.APPLY) {
            moves.add("Keep the entered amounts fixed and save screenshots of the payment schedule before applying.");
            moves.add("Do not continue if the portal adds a new admin, deposit, or prepaid rent charge.");
        }
        if (cashNeededToClear > 0) {
            moves.add("Ask the property to reduce, split, or delay at least $" + format(cashNeededToClear) + " of upfront cash before you apply.");
        }
        if (depositLikeCash > input.monthlyRent()) {
            moves.add("Challenge the deposit/prepaid-rent stack: current deposit-like cash is $" + format(depositLikeCash) + " before other fees.");
        }
        if (preApprovalCashRisk > 0) {
            moves.add("Before risking $" + format(preApprovalCashRisk) + " pre-approval, get refund or credit language in writing.");
        }
        if (incomeStatus.startsWith("Below")) {
            moves.add("Ask for the exact income rule and any documented exception path before paying.");
        } else if (input.grossMonthlyIncome() == null || input.grossMonthlyIncome() <= 0) {
            moves.add("Confirm the income rule first; the checker cannot clear approval odds without it.");
        }
        if (saferMonthlyRentTarget > 0 && saferMonthlyRentTarget < input.monthlyRent()) {
            moves.add("If the fee schedule will not move, search near $" + format(saferMonthlyRentTarget) + "/mo for the same cash position.");
        }
        if (moves.isEmpty()) {
            moves.add("Use the message below to turn unknown terms into written terms before payment.");
        }
        return moves.stream().distinct().limit(5).toList();
    }

    private List<String> buildApplySafeChanges(
            ShouldIApplyInput input,
            int estimatedDeposit,
            int cashNeededToClear,
            int saferMonthlyRentTarget,
            int requiredMonthlyIncome,
            String incomeStatus,
            int preApprovalCashRisk,
            ApplicationDecision decision,
            String constraintLabel,
            List<String> blockers,
            List<String> pauseGates) {
        List<String> changes = new ArrayList<>();
        if (decision == ApplicationDecision.APPLY) {
            changes.add("Keep the portal payment schedule at or below the entered total of $" + format(input.monthlyRent()
                    + (input.applicationFee() * input.applicantCount())
                    + input.adminFee()
                    + input.holdingDeposit()
                    + input.moveInFee()
                    + input.brokerFee()
                    + estimatedDeposit
                    + input.prepaidRent()
                    + (input.hasPet() ? input.petFee() + input.petDeposit() + input.monthlyPetRent() : 0)
                    + input.utilityDeposit()) + " before local moving cost.");
            changes.add("Stop and re-run if a new admin fee, deposit, prepaid rent, or pet charge appears.");
            return changes;
        }

        if (cashNeededToClear > 0) {
            changes.add("Lower upfront cash by about $" + format(cashNeededToClear) + " or add that cash before paying.");
        }
        if (saferMonthlyRentTarget > 0 && saferMonthlyRentTarget < input.monthlyRent()) {
            changes.add("Search near $" + format(saferMonthlyRentTarget) + "/mo if the fee schedule cannot change.");
        }
        if (incomeStatus.startsWith("Below")) {
            changes.add("Get written approval criteria showing a lower income rule, or raise verified monthly income to about $" + format(requiredMonthlyIncome) + "/mo.");
        }
        if (constraintLabel.startsWith("Policy") || blockers.stream().anyMatch(blocker -> blocker.contains("fee") || blocker.contains("deposit"))) {
            changes.add("Ask the property to change or document the flagged fee/deposit term, then re-run before paying.");
        }
        int depositLikeCash = estimatedDeposit + input.prepaidRent();
        if (depositLikeCash > input.monthlyRent()) {
            changes.add("Test removing prepaid rent or reducing deposit-like cash from $" + format(depositLikeCash) + " toward one month rent.");
        }
        if (preApprovalCashRisk >= 350 || pauseGates.stream().anyMatch(gate -> gate.contains("pre-approval"))) {
            changes.add("Convert the $" + format(preApprovalCashRisk) + " pre-approval charge into written refundable or credited terms.");
        }
        if (input.grossMonthlyIncome() == null || input.grossMonthlyIncome() <= 0) {
            changes.add("Enter verified monthly income or get the property income rule before treating this as apply-safe.");
        }
        if (changes.isEmpty()) {
            changes.add("Close the written confirmation gate, then re-run this listing with the confirmed terms.");
        }
        return changes.stream().distinct().limit(5).toList();
    }

    private List<String> buildDecisionSteps(
            ApplicationDecision decision,
            List<String> blockers,
            List<String> pauseGates,
            int cashNeededToClear,
            int saferMonthlyRentTarget,
            int requiredMonthlyIncome,
            int preApprovalCashRisk,
            int monthlyRent) {
        List<String> steps = new ArrayList<>();
        if (decision == ApplicationDecision.DO_NOT_APPLY) {
            steps.add("Do not submit payment on this listing until the failed gate changes.");
            if (!blockers.isEmpty()) {
                steps.add("Hard blocker: " + String.join(", ", blockers) + ".");
            }
            if (cashNeededToClear > 0) {
                steps.add("To make this safer, add about $" + format(cashNeededToClear) + " cash or lower the rent/deposit stack.");
            }
            if (saferMonthlyRentTarget > 0 && saferMonthlyRentTarget < monthlyRent) {
                steps.add("Use $" + format(saferMonthlyRentTarget) + "/mo as the next search target for a similar fee stack.");
            }
            steps.add("Send the confirmation message before paying another fee.");
            return steps;
        }
        if (decision == ApplicationDecision.PAUSE) {
            steps.add("Do not pay yet; one confirmation gate is still open.");
            if (!pauseGates.isEmpty()) {
                steps.add("Open gate: " + String.join(", ", pauseGates) + ".");
            }
            if (preApprovalCashRisk > 0) {
                steps.add("Confirm whether $" + format(preApprovalCashRisk) + " due before approval is refundable or credited.");
            }
            if (cashNeededToClear > 0) {
                steps.add("Reduce the upfront stack or add about $" + format(cashNeededToClear) + " before submitting payment.");
            }
            steps.add("Re-run the checker with the property answer.");
            return steps;
        }
        steps.add("Confirm written screening criteria and refundable charges.");
        steps.add("Keep enough cash after move-in for the buffer shown above.");
        steps.add("Apply only if the payment schedule matches the entered numbers.");
        return steps;
    }

    private String buildDecisionBasis(List<String> blockers, List<String> pauseGates) {
        if (!blockers.isEmpty()) {
            return "Hard gate failed: " + String.join(", ", blockers) + ".";
        }
        if (!pauseGates.isEmpty()) {
            return "No hard gate failed, but confirmation gate remains: " + String.join(", ", pauseGates) + ".";
        }
        return "All loaded cash, income, fee, and deposit gates cleared.";
    }

    private String buildSummary(
            ApplicationDecision decision,
            int cashGap,
            int postMoveCash,
            int recommendedBuffer,
            String incomeStatus) {
        return switch (decision) {
            case APPLY -> "The listing clears the current cash and screening assumptions. Confirm refundable charges before paying.";
            case PAUSE -> "Do not pay yet. The listing may work, but one or more fee, income, or buffer checks needs confirmation.";
            case DO_NOT_APPLY -> cashGap > 0
                    ? "Do not apply yet. The entered cash does not cover the estimated move-in stack."
                    : "Do not apply yet. The current income and risk checks point to likely failure.";
        } + " Post-move cash: " + formatSignedDollars(postMoveCash) + " vs buffer target $" + format(recommendedBuffer)
                + ". Income status: " + incomeStatus + ".";
    }

    private String summarizeApplicationRule(Optional<ApplicationFeeRulesData.ApplicationFeeRule> rule) {
        return rule.map(r -> r.sourceCitation() + ": " + r.capFormula())
                .orElse("No V3 application-fee rule loaded for this state yet. Use ask-before-paying language.");
    }

    private String summarizeDepositRule(Optional<DepositPrepaidRulesData.DepositPrepaidRule> rule) {
        return rule.map(r -> r.sourceCitation() + ": " + r.capFormula())
                .orElse("No V3 deposit/prepaid-rent rule loaded for this state yet. Use city estimate and ask-before-paying language.");
    }

    private String buildDataCoverageNote(
            Optional<ApplicationFeeRulesData.ApplicationFeeRule> applicationRule,
            Optional<DepositPrepaidRulesData.DepositPrepaidRule> depositRule) {
        if (applicationRule.isPresent() && depositRule.isPresent()) {
            return "State-level seed rules are loaded for both application fees and deposit/prepaid rent. This is risk guidance, not legal advice.";
        }
        if (applicationRule.isPresent()) {
            return "Application-fee seed rule is loaded, but deposit/prepaid-rent coverage is incomplete for this state.";
        }
        if (depositRule.isPresent()) {
            return "Deposit/prepaid-rent seed rule is loaded, but application-fee coverage is incomplete for this state.";
        }
        return "This state is outside the current V3 seed-rule coverage. Treat legal/fee checks as unknown and ask before paying.";
    }

    private void addAll(List<String> target, List<String> values) {
        if (values != null) {
            values.stream()
                    .filter(v -> v != null && !v.isBlank())
                    .forEach(target::add);
        }
    }

    private boolean prepaidRentCountsAgainstDepositCap(DepositPrepaidRulesData.DepositPrepaidRule rule) {
        if (rule.prepaidRentLimit() == null || rule.prepaidRentLimit().isBlank()) {
            return true;
        }
        return !contains(rule.prepaidRentLimit(), "in addition");
    }

    private boolean contains(String value, String token) {
        return value != null && value.toLowerCase().contains(token.toLowerCase());
    }

    private String format(int amount) {
        return String.format("%,d", Math.abs(amount));
    }

    private String formatSignedDollars(int amount) {
        return (amount < 0 ? "-$" : "$") + format(amount);
    }

    private int roundDownToNearest25(int amount) {
        return amount - (amount % 25);
    }
}
