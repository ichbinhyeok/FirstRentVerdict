package firstrentverdict.service.seo;

import firstrentverdict.model.dtos.RentData;
import firstrentverdict.model.dtos.SecurityDepositData;
import firstrentverdict.model.dtos.MovingData;
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
                double depositMult = (depositData != null && depositData.city_practice() != null
                                && !depositData.city_practice().typicalMultipliers().isEmpty())
                                                ? depositData.city_practice().typicalMultipliers().get(0)
                                                : 1.0;
                int deposit = (int) (avgRent * depositMult);
                int moving = (movingData != null) ? movingData.typical() : 500;
                int upfrontTotal = avgRent + deposit + moving;

                // Calculate Ranges for Display
                int rentLow = (rentData.p25() > 0) ? rentData.p25() : (int) (avgRent * 0.85);
                int rentHigh = (rentData.p75() > 0) ? rentData.p75() : (int) (avgRent * 1.15);

                int movingLow = (movingData != null) ? movingData.low() : 300;
                int movingHigh = (movingData != null) ? movingData.high() : 1000;

                // 2. SEO Title & Meta (Intent Matching)
                String pageTitle = String.format("Average Rent in %s %s 2026: Cost & Move-in Guide", city, state);

                String metaDescription = String.format(
                                "Average rent in %s is $%s/month. Move-in costs range from $%s to $%s. See full breakdown, deposit rules, and check affordability with our free calculator.",
                                city,
                                String.format("%,d", avgRent),
                                String.format("%,d", upfrontTotal - 500), // simplistic lower bound estimate for click
                                                                          // appeal of range
                                String.format("%,d", upfrontTotal + 500));

                // 3. Generate Narratives
                String marketContext;
                int nationalRef = 1500;

                if (avgRent > nationalRef * 1.2) {
                        marketContext = String.format(
                                        "%s is a high-cost market, creating a significant barrier to entry. With average rents ($%s) trending above the national median, landlords often strictly enforce income requirements.",
                                        city, String.format("%,d", avgRent));
                } else if (avgRent < nationalRef * 0.8) {
                        marketContext = String.format(
                                        "%s offers a lower barrier to entry compared to major national hubs. However, even with lower rents ($%s), the upfront cash requirement catches many by surprise.",
                                        city, String.format("%,d", avgRent));
                } else {
                        marketContext = String.format(
                                        "Market rates in %s ($%s) align with national averages. This means the standard 3x income verification is the norm, but local deposit laws vary.",
                                        city, String.format("%,d", avgRent));
                }

                String incomeLogic = String.format(
                                "Landlords in %s typically reference the 3x rent rule. For a median apartment at $%s, you need a stable monthly household income around $%s to be approved.",
                                city, String.format("%,d", rentData.median()),
                                String.format("%,d", requiredIncomeMonthly));

                // Risk Narrative
                String preVerdictHeadline;
                String riskNarrative;
                double upfrontRatio = (double) upfrontTotal / avgRent;

                if (upfrontRatio >= 3.5) {
                        preVerdictHeadline = String.format("Warning: High Upfront Liquidity Risk in %s", city);
                        riskNarrative = "Most denials in this market happen not because of monthly income, but because applicants underestimate the total cash needed to sign the lease. The 'Liquidity Wall' here is steeper than average.";
                } else if (avgRent > 2200) {
                        preVerdictHeadline = String.format("High Income Requirement Alert for %s", city);
                        riskNarrative = "Landlords here are strict. Even if approved, the high baseline rent leaves little room for error. The risk is becoming 'House Poor' immediately after moving.";
                } else {
                        preVerdictHeadline = String.format("Hidden Costs in %s's Rental Market", city);
                        riskNarrative = "While rent seems manageable, our data shows that moving costs and fees often push tenants into the red within the first 90 days. Watch out for 'Hidden Inflation'.";
                }

                // 4. Aggregate Sources
                java.util.List<String> sources = new java.util.ArrayList<>();
                if (rentData.sources() != null)
                        sources.addAll(rentData.sources());
                if (depositData != null && depositData.sources() != null)
                        sources.addAll(depositData.sources());
                if (movingData != null && movingData.sources() != null)
                        sources.addAll(movingData.sources());

                // Remove duplicates and limit
                sources = sources.stream().distinct().limit(5).collect(java.util.stream.Collectors.toList());

                // 5. QnA
                java.util.List<QnA> qnaList = new java.util.ArrayList<>();
                qnaList.add(new QnA(
                                String.format("Is $%s/year enough to live in %s?",
                                                String.format("%,d", requiredIncomeYearly), city),
                                String.format("Mathematically, yes. This meets the standard 3x requirement. However, if you have student loans or car payments, we recommend targeting a 'Safe Floor' of $%s/year.",
                                                String.format("%,d", (int) (requiredIncomeYearly * 1.2)))));

                qnaList.add(new QnA(
                                String.format("How much cash do I need to move to %s?", city),
                                String.format("You should aim for at least $%s in liquid savings. This covers your first month, security deposit, and local moving costs, plus a minimal emergency buffer.",
                                                String.format("%,d", (int) (upfrontTotal * 1.2)))));

                return new CityPageContent(
                                pageTitle,
                                metaDescription,
                                city,
                                state,
                                avgRent,
                                rentLow,
                                rentHigh,
                                requiredIncomeMonthly,
                                requiredIncomeYearly,
                                deposit,
                                moving,
                                movingLow,
                                movingHigh,
                                upfrontTotal,
                                marketContext,
                                incomeLogic,
                                preVerdictHeadline,
                                riskNarrative,
                                qnaList,
                                sources,
                                (depositData != null) ? depositData.city_practice().notes() : "Standard 1 month rent.",
                                (movingData != null) ? movingData.assumptions() : "Standard local move.");
        }

        public record CityPageContent(
                        String pageTitle,
                        String metaDescription,
                        String city,
                        String state,
                        int medianRent,
                        int rentLow, // p25
                        int rentHigh, // p75
                        int monthlyIncomeReq,
                        int yearlyIncomeReq,
                        int typicalDeposit,
                        int typicalMoving,
                        int movingLow,
                        int movingHigh,
                        int totalUpfrontEstimate,
                        String marketContextText,
                        String incomeLogicText,
                        String preVerdictHeadline,
                        String riskNarrative,
                        java.util.List<QnA> commonQuestions,
                        java.util.List<String> dataSources, // Links for E-E-A-T
                        String depositNotes, // Legal context
                        String movingAssumptions // Context
        ) {
        }

        public record QnA(String question, String answer) {
        }
}
