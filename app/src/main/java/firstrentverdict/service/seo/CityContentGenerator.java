package firstrentverdict.service.seo;

import firstrentverdict.model.dtos.RentData;
import firstrentverdict.model.dtos.SecurityDepositData;
import firstrentverdict.model.dtos.MovingData;
import firstrentverdict.model.dtos.CitiesData;
import org.springframework.stereotype.Service;

@Service
public class CityContentGenerator {

        public CityPageContent generate(
                        String city,
                        String state,
                        RentData.CityRent rentData,
                        SecurityDepositData depositData,
                        MovingData.CityMoving movingData) {
                // 1. Calculate Core Financials
                int avgRent = rentData.median();
                // 3x Rule
                int requiredIncomeMonthly = avgRent * 3;
                int requiredIncomeYearly = requiredIncomeMonthly * 12;

                // Upfront Calculation (Rent + Deposit + Moving + minimal buffer implicit)
                // We assume 1 month deposit typical unless data says otherwise
                double depositMult = (depositData != null && !depositData.typicalMultipliers().isEmpty())
                                ? depositData.typicalMultipliers().get(0)
                                : 1.0;
                int deposit = (int) (avgRent * depositMult);
                int moving = (movingData != null) ? movingData.typical() : 500; // fallback
                int upfrontTotal = avgRent + deposit + moving;

                // 2. Generate Neutral "Market Context" text
                String marketContext;
                // Stub national median reference (e.g. $1,500 for single bed in 2026) ->
                // strictly hypothetical for logic
                int nationalRef = 1500;

                if (avgRent > nationalRef * 1.2) {
                        marketContext = String.format(
                                        "%s typically requires stronger upfront liquidity due to market rates trending above the national median. Landlords here often strictly enforce income requirements.",
                                        city);
                } else if (avgRent < nationalRef * 0.8) {
                        marketContext = String.format(
                                        "%s offers a lower barrier to entry compared to major national hubs. However, standard proof of income is still required.",
                                        city);
                } else {
                        marketContext = String.format(
                                        "Market rates in %s align with national averages, meaning standard 3x income verification is the norm.",
                                        city);
                }

                // 3. Generate Income Rule Description
                String incomeLogic = String.format(
                                "Landlords in %s typically reference the 3x rent rule. For a median apartment at $%s, this suggests a monthly household income around $%s.",
                                city, rentData.median(), String.format("%,d", requiredIncomeMonthly));

                // new rule: Determine Risk Type & Pre-Verdict Narrative
                String preVerdictHeadline;
                String riskNarrative;

                // Ratio of Upfront to Rent (How many months of rent needed upfront?)
                double upfrontRatio = (double) upfrontTotal / avgRent;

                if (upfrontRatio >= 3.5) {
                        // High Upfront Cost Scenario (Liquidity Trap)
                        preVerdictHeadline = String.format(
                                        "In %s, the danger isn't just the rent—it's the liquidity shock.", city);
                        riskNarrative = "Most denials in this market trigger not because of monthly income, but because applicants underestimate the sheer cash required to sign the lease. You face a high 'Liquidity Wall' here.";
                } else if (avgRent > 2200) {
                        // High Rent Scenario (Income Squeeze)
                        preVerdictHeadline = String.format(
                                        "%s demands proof of high income, but your daily cash flow is the real risk.",
                                        city);
                        riskNarrative = "Landlords here are strict about the 3x rule. Even if you are approved, the high baseline rent leaves very little room for error. The risk here is 'House Poor' survival.";
                } else {
                        // Standard/Balanced Scenario (Hidden Inflation)
                        preVerdictHeadline = String
                                        .format("Don't let %s's moderate rent fool you—hidden costs stack up.", city);
                        riskNarrative = "While rent seems manageable, our data shows that moving costs and accumulating fees often push tenants into the red within the first 90 days. The risk is 'Hidden Inflation'.";
                }

                // 4. Generate User-Centric QnA
                java.util.List<QnA> qnaList = new java.util.ArrayList<>();

                // Q1: Income Sufficiency
                qnaList.add(new QnA(
                                String.format("Is $%s/year enough to live in %s?",
                                                String.format("%,d", requiredIncomeYearly), city),
                                String.format("Mathematically, yes. This meets the standard 3x requirement. However, this number assumes you have zero debt. If you have student loans or a car payment, $%s is likely the 'Safe Floor' you need to target.",
                                                String.format("%,d", (int) (requiredIncomeYearly * 1.2)))));

                // Q2: Cash on Hand
                qnaList.add(new QnA(
                                String.format("How much savings do I usually need before moving to %s?", city),
                                String.format("You shouldn't attempt a move here with less than $%s in liquid assets. This covers your upfront %s costs plus a minimal safety buffer. Anything less puts you at immediate risk of insolvency.",
                                                String.format("%,d", (int) (upfrontTotal * 1.5)), state)));

                return new CityPageContent(
                                String.format("True Cost of Renting in %s: Risk Assessment (2026)", city),
                                String.format("Thinking of moving to %s? Don't just check the rent. Our audit reveals the liquidity trap and income risks specific to the %s market.",
                                                city, city),
                                city,
                                state,
                                avgRent,
                                requiredIncomeMonthly,
                                requiredIncomeYearly,
                                deposit,
                                moving,
                                upfrontTotal,
                                marketContext,
                                incomeLogic,
                                preVerdictHeadline,
                                riskNarrative,
                                qnaList);
        }

        // DTO for the View
        public record CityPageContent(
                        String pageTitle,
                        String metaDescription,
                        String city,
                        String state,
                        int medianRent,
                        int monthlyIncomeReq,
                        int yearlyIncomeReq,
                        int typicalDeposit,
                        int typicalMoving,
                        int totalUpfrontEstimate,
                        String marketContextText,
                        String incomeLogicText,
                        String preVerdictHeadline,
                        String riskNarrative,
                        java.util.List<QnA> commonQuestions) {
        }

        public record QnA(String question, String answer) {
        }
}
