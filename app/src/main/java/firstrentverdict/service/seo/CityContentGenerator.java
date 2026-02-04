package firstrentverdict.service.seo;

import firstrentverdict.model.dtos.RentData;
import firstrentverdict.model.dtos.SecurityDepositData;
import firstrentverdict.model.dtos.MovingData;
import firstrentverdict.model.dtos.PetData;
import firstrentverdict.model.dtos.CityInsightData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityContentGenerator {

        public enum Intent {
                GENERAL,
                CREDIT_POOR,
                CREDIT_FAIR,
                RELOCATION,
                PET_FRIENDLY,
                SAVINGS_BASED,
                RENT_STATE,
                CREDIT_GOOD,
                RELOCATION_PAIR
        }

        public CityPageContent generate(
                        String city,
                        String state,
                        RentData.CityRent rentData,
                        SecurityDepositData depositData,
                        MovingData.CityMoving movingData,
                        PetData.CityPet petData,
                        CityInsightData.CityInsight insight,
                        Intent intent,
                        Object contextValue) {
                int avgRent = rentData.median();

                // 1. Precise Deposit Logic
                double depositMult = 1.0;
                String depositNotes = "Standard 1 month rent.";
                if (depositData != null && depositData.city_practice() != null) {
                        depositMult = !depositData.city_practice().typicalMultipliers().isEmpty()
                                        ? depositData.city_practice().typicalMultipliers().get(0)
                                        : 1.0;
                        depositNotes = depositData.city_practice().notes();
                }
                int deposit = (int) (avgRent * depositMult);

                // 2. Precise Moving Logic
                int moving = (movingData != null) ? movingData.typical() : 500;
                String movingNotes = (movingData != null) ? movingData.assumptions() : "Standard local move.";

                // 3. Precise Pet Logic
                int petDeposit = (petData != null && petData.oneTime() != null) ? petData.oneTime().avg() : 300;
                int petRentMonthly = (petData != null && petData.monthlyPetRent() != null)
                                ? petData.monthlyPetRent().avg()
                                : 35;
                String petNotes = (petData != null && petData.oneTime() != null) ? petData.oneTime().notes()
                                : "Standard pet fees apply.";

                int upfrontTotal = avgRent + deposit + moving;
                if (intent == Intent.PET_FRIENDLY) {
                        upfrontTotal += petDeposit;
                }

                // 4. Content Generation
                String pageTitle;
                String metaDescription;
                String localInsight = "";
                List<QnA> qnaList = new ArrayList<>();

                // High-CTR Suffixes
                String ctrSuffix = " ✓ Free 2026 Analysis";

                switch (intent) {
                        case PET_FRIENDLY -> {
                                pageTitle = String.format("Moving to %s with Pets: $%,d Deposit & Fee Audit (2026)",
                                                city, petDeposit);
                                metaDescription = String.format(
                                                "How much is a pet deposit in %s? Average is $%,d plus $%,d/mo. [2026 Rental Calculator] Total move-in cost: $%,d.%s",
                                                city, petDeposit, petRentMonthly, upfrontTotal, ctrSuffix);
                                localInsight = String.format("In %s, %s Monthly pet rent is typically $%,d.", city,
                                                petNotes, petRentMonthly);

                                qnaList.add(new QnA("Are pet deposits refundable in " + city + "?",
                                                "Typically, a portion is a non-refundable 'Pet Fee' ($200-$300) while the remainder is a refundable deposit."));
                                qnaList.add(new QnA("What is the average pet rent in " + city + "?",
                                                "Most apartments in " + city + " charge between $"
                                                                + (petRentMonthly - 10) + " and $"
                                                                + (petRentMonthly + 15) + " per month."));
                        }
                        case SAVINGS_BASED -> {
                                int savings = (contextValue instanceof Integer) ? (Integer) contextValue : 5000;
                                pageTitle = String.format("Can I move to %s with $%s in Savings?", city,
                                                String.format("%,d", savings));
                                metaDescription = String.format(
                                                "Is $%s enough for %s? [Calculator] You need at least $%,d upfront for rent, deposit, and moving. Check your approval odds.%s",
                                                String.format("%,d", savings), city, upfrontTotal, ctrSuffix);
                                localInsight = (savings >= upfrontTotal)
                                                ? String.format("Safe: Your $%s exceeds the $%,d entry cost. You'll have a $%,d cushion.",
                                                                String.format("%,d", savings), upfrontTotal,
                                                                savings - upfrontTotal)
                                                : String.format("Risk: $%s is $%,d short of the $%,d required for a safe move to %s.",
                                                                String.format("%,d", savings), upfrontTotal - savings,
                                                                upfrontTotal, city);

                                qnaList.add(new QnA("How much should I save before moving to " + city + "?",
                                                "We recommend saving at least 3x the monthly rent plus $2,000 for moving expenses. For "
                                                                + city + ", that's roughly $"
                                                                + String.format("%,d", (avgRent * 3) + 2000) + "."));
                        }
                        case CREDIT_POOR -> {
                                int highRiskDeposit = (int) (avgRent * 2.0);
                                pageTitle = String.format("Renting in %s with Poor Credit (2026 Approval Guide)", city);
                                metaDescription = String.format(
                                                "Credit under 600? In %s, expect to pay up to 2x deposit ($%,d). [2026 Audit] Get the approval blueprint for low scores.%s",
                                                city, highRiskDeposit, ctrSuffix);
                                localInsight = String.format(
                                                "Credit Update: %s High-risk applicants in %s should prepare for higher upfront liquidity.",
                                                depositNotes, city);

                                qnaList.add(new QnA("Will I get denied in " + city + " with a 550 credit score?",
                                                "Not necessarily. Many landlords in " + city
                                                                + " will approve you if you offer a higher security deposit."));
                        }
                        case CREDIT_FAIR -> {
                                pageTitle = String.format(
                                                "Renting in %s with Fair Credit (600-660): 2026 Approval Odds", city);
                                metaDescription = String.format(
                                                "Have a 640 score? See how to avoid high deposits in %s. [2026 Rent Verdict] 3 strategies to secure a lease with fair credit.%s",
                                                city, ctrSuffix);
                                localInsight = "Fair Credit Tip: Most corporate landlords here will approve a 620+ score but may request a full month's security deposit.";

                                qnaList.add(new QnA("Is 640 a good enough score for apartments in " + city + "?",
                                                "It is often the 'borderline' score. You are likely to be approved, but without the best move-in incentives."));
                        }
                        case CREDIT_GOOD -> {
                                pageTitle = String.format(
                                                "Renting in %s with Good Credit (700+): Priority Move-In Guide", city);
                                metaDescription = String.format(
                                                "Leverage your 750+ score for $0 deposits in %s. [2026 Market Report] How to find the best rental incentives for high-credit tenants.%s",
                                                city, ctrSuffix);
                                localInsight = "Good Credit Power: With a 700+ score, you have high leverage to negotiate $0 security deposit or waived application fees.";

                                qnaList.add(new QnA("Can I get a $0 deposit in " + city + " with 750 credit?",
                                                "Yes, many luxury buildings and national property managers offer 'Deposit-Free' options for top-tier credit profiles."));
                        }
                        case RELOCATION_PAIR -> {
                                String fromCity = (String) contextValue;
                                pageTitle = String.format("Moving from %s to %s Cost Audit (2026)", fromCity, city);
                                metaDescription = String.format(
                                                "Relocating from %s to %s? We found a $%,d move-in barrier. [2026 Cost Index] Calculate your specific relocation budget.%s",
                                                fromCity, city, upfrontTotal, ctrSuffix);
                                localInsight = String.format(
                                                "Cost Shift: Median rent in %s is $%,d. Moving from %s typically adds $%,d to the bill.",
                                                city, avgRent, fromCity, moving);

                                qnaList.add(new QnA("Is " + city + " cheaper than " + fromCity + "?",
                                                "Median rent in " + city + " is $" + String.format("%,d", avgRent)
                                                                + ". Compare this to your current costs."));
                        }
                        default -> {
                                pageTitle = String.format("%s %s Rent Breakdown: $%,d/mo + $%,d Move-In", city, state,
                                                avgRent, upfrontTotal);
                                metaDescription = String.format(
                                                "Total cost to rent in %s: $%s/mo rent plus $%s move-in. [2026 Audited Data] See if you qualify for a 1BR apartment today.%s",
                                                city, String.format("%,d", avgRent), String.format("%,d", upfrontTotal),
                                                ctrSuffix);
                                localInsight = String.format(
                                                "Market Trend: %s Median rent for a 1BR in %s is currently $%,d.",
                                                depositNotes, city, avgRent);

                                qnaList.add(new QnA("What is the '3x Rule' in " + city + "?",
                                                "Landlords require gross income to be 3x rent. In " + city
                                                                + ", you'll need around $"
                                                                + String.format("%,d", avgRent * 3) + "/mo."));
                        }
                }

                // Incorporate Human Insights from city_insights.json
                String qualitativeStory = "";
                String seasonalTip = "Peak moving season aligns with the general summer cycle.";
                String renterAdvice = "Always inspect the property in person before signing a lease.";
                String localLaw = "Standard state-level landlord-tenant laws apply.";

                if (insight != null) {
                        qualitativeStory = String.format("%s Tip: %s. Local Law: %s.", insight.marketTrend(),
                                        insight.seasonalTip(), insight.localLaw());
                        localInsight += " " + insight.renterAdvice();
                        seasonalTip = insight.seasonalTip();
                        renterAdvice = insight.renterAdvice();
                        localLaw = insight.localLaw();

                        qnaList.add(new QnA("What is a local tip for renting in " + city + "?",
                                        insight.renterAdvice()));
                        qnaList.add(new QnA("When is the best time to move to " + city + "?", insight.seasonalTip()));
                }

                // Universal QnA for every page
                qnaList.add(new QnA("What are the typical upfront costs for a rental in " + city + "?",
                                "Usually, you'll need the first month's rent, a security deposit (often 1x rent), and around $500-$1,000 for moving logistics. Total estimate: $"
                                                + String.format("%,d", upfrontTotal) + "."));

                return new CityPageContent(
                                pageTitle, metaDescription, city, state, avgRent,
                                (int) (avgRent * 0.8), (int) (avgRent * 1.2), avgRent * 3, avgRent * 36,
                                deposit, moving, (int) (moving * 0.6), (int) (moving * 1.4), upfrontTotal,
                                qualitativeStory.isEmpty() ? String.format(
                                                "The rental market in %s requires significant upfront liquidity.", city)
                                                : qualitativeStory,
                                String.format("Income Verification: You need $%,d/mo to pass the standard 3x income test.",
                                                avgRent * 3),
                                "Audited Cost Report",
                                "Liquidity Risk: The total $%,d move-in cost is the primary hurdle for %s renters."
                                                .formatted(upfrontTotal, city),
                                qnaList, List.of("2026 Internal Market Index", "US Census ACS", "Local Rental Survey"),
                                depositNotes, movingNotes,
                                (avgRent > 2000 ? "High-Cost" : "Moderate"), 0,
                                localLaw,
                                localInsight,
                                petDeposit, petRentMonthly, petNotes,
                                seasonalTip, renterAdvice);
        }

        public record CityPageContent(
                        String pageTitle, String metaDescription, String city, String state,
                        int medianRent, int rentLow, int rentHigh, int monthlyIncomeReq, int yearlyIncomeReq,
                        int typicalDeposit, int typicalMoving, int movingLow, int movingHigh, int totalUpfrontEstimate,
                        String marketContextText, String incomeLogicText, String preVerdictHeadline,
                        String riskNarrative,
                        List<QnA> commonQuestions, List<String> dataSources, String depositNotes,
                        String movingAssumptions,
                        String affordabilityTier, int nationalComparison, String legalSummary, String localInsight,
                        int petDeposit, int petRent, String petNotes,
                        String seasonalTip, String renterAdvice) {
        }

        public record QnA(String question, String answer) {
        }
}
