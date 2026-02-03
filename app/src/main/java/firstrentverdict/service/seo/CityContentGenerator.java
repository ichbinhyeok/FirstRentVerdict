package firstrentverdict.service.seo;

import firstrentverdict.model.dtos.RentData;
import firstrentverdict.model.dtos.SecurityDepositData;
import firstrentverdict.model.dtos.MovingData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

                // 2. SEO Title & Meta (Intent Matching with Numbers for CTR)
                String pageTitle = String.format("%s %s Rent: $%,d/mo + $%,d Move-In (2026 Data)",
                                city, state, avgRent, upfrontTotal);

                String metaDescription = String.format(
                                "Average rent in %s is $%s/month. Need $%s-$%s to move in (deposit + moving + first month). Free calculator to check if you can afford it.",
                                city,
                                String.format("%,d", avgRent),
                                String.format("%,d", (int) (upfrontTotal * 0.85)),
                                String.format("%,d", (int) (upfrontTotal * 1.25)));

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
                List<String> sources = new ArrayList<>();
                if (rentData.sources() != null)
                        sources.addAll(rentData.sources());
                if (depositData != null && depositData.sources() != null)
                        sources.addAll(depositData.sources());
                if (movingData != null && movingData.sources() != null)
                        sources.addAll(movingData.sources());

                // Remove duplicates and limit
                sources = sources.stream().distinct().limit(5).collect(Collectors.toList());

                // 5. QnA (Expanded to 5 for better FAQ Schema and unique content)
                List<QnA> qnaList = new ArrayList<>();

                // Q1: Income requirement
                qnaList.add(new QnA(
                                String.format("Is $%s/year enough to live in %s?",
                                                String.format("%,d", requiredIncomeYearly), city),
                                String.format(
                                                "Mathematically, yes. This meets the standard 3x requirement for a $%s/month apartment. However, if you have student loans, car payments, or other debts, we recommend targeting a 'Safe Floor' of $%s/year to maintain financial stability.",
                                                String.format("%,d", avgRent),
                                                String.format("%,d", (int) (requiredIncomeYearly * 1.2)))));

                // Q2: Move-in cash
                qnaList.add(new QnA(
                                String.format("How much cash do I need to move to %s?", city),
                                String.format(
                                                "You should aim for at least $%s in liquid savings. This breaks down to: first month rent ($%s), security deposit ($%s), moving costs (~$%s), plus an emergency buffer. Without this, you risk immediate financial stress.",
                                                String.format("%,d", (int) (upfrontTotal * 1.2)),
                                                String.format("%,d", avgRent),
                                                String.format("%,d", deposit),
                                                String.format("%,d", moving))));

                // Q3: Security deposit rules (city-specific)
                String depositNote = (depositData != null && depositData.city_practice() != null
                                && depositData.city_practice().notes() != null)
                                                ? depositData.city_practice().notes()
                                                : "typically 1 month's rent";
                qnaList.add(new QnA(
                                String.format("What is the security deposit in %s, %s?", city, state),
                                String.format(
                                                "In %s, the security deposit is %s. For a median apartment at $%s/month, expect to pay around $%s upfront as a deposit. This is usually refundable if you leave the unit in good condition.",
                                                city, depositNote,
                                                String.format("%,d", avgRent),
                                                String.format("%,d", deposit))));

                // Q4: Compare to national average
                int nationalAvgRent = 1500;
                String comparison = avgRent > nationalAvgRent * 1.1 ? "above"
                                : (avgRent < nationalAvgRent * 0.9 ? "below" : "close to");
                int percentDiff = (int) Math.abs(((double) (avgRent - nationalAvgRent) / nationalAvgRent) * 100);
                qnaList.add(new QnA(
                                String.format("Is %s expensive compared to other US cities?", city),
                                String.format("%s rent ($%s/month) is %s the national average by about %d%%. %s",
                                                city, String.format("%,d", avgRent), comparison, percentDiff,
                                                avgRent > nationalAvgRent
                                                                ? "This means you'll need higher income and more savings to move here safely."
                                                                : "This makes it more accessible for first-time renters with moderate savings.")));

                // Q5: First-time renter tips
                qnaList.add(new QnA(
                                String.format("Tips for first-time renters in %s?", city),
                                String.format(
                                                "1) Have at least $%s saved before signing. 2) Your income should be 3x the rent ($%s/month or $%s/year). 3) Check your credit score beforehand. 4) Budget for moving costs ($%s-%s locally). 5) Keep 1-2 months rent as emergency fund after moving.",
                                                String.format("%,d", (int) (upfrontTotal * 1.2)),
                                                String.format("%,d", requiredIncomeMonthly),
                                                String.format("%,d", requiredIncomeYearly),
                                                String.format("%,d", movingLow),
                                                String.format("%,d", movingHigh))));

                // Calculate Thin Content Prevention fields
                String affordabilityTier;
                if (avgRent > nationalRef * 1.2) {
                        affordabilityTier = "High-Cost";
                } else if (avgRent < nationalRef * 0.8) {
                        affordabilityTier = "Affordable";
                } else {
                        affordabilityTier = "Moderate";
                }

                int nationalComparison = (int) (((double) (avgRent - nationalRef) / nationalRef) * 100);

                String legalSummary;
                if (depositData != null && depositData.city_practice() != null
                                && depositData.city_practice().notes() != null) {
                        legalSummary = depositData.city_practice().notes();
                } else {
                        legalSummary = "Standard security deposit rules apply. Check local regulations.";
                }

                // Generate city-specific local insight based on data patterns
                String localInsight;
                if (avgRent > 2500) {
                        localInsight = String.format(
                                        "%s is among the most expensive rental markets in the US. First-time renters often need a co-signer or 6+ months of savings.",
                                        city);
                } else if (depositMult > 1.5) {
                        localInsight = String.format(
                                        "Landlords in %s commonly require higher security deposits. Budget for 2+ months rent upfront.",
                                        city);
                } else if (movingHigh > 1000) {
                        localInsight = String.format(
                                        "Moving costs in %s can vary significantly. DIY moves average $%d, while full-service movers run $%d+.",
                                        city, movingLow, movingHigh);
                } else {
                        localInsight = String.format(
                                        "%s offers relatively accessible entry for first-time renters, but upfront cash requirements still catch many off-guard.",
                                        city);
                }

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
                                (depositData != null && depositData.city_practice() != null)
                                                ? depositData.city_practice().notes()
                                                : "Standard 1 month rent.",
                                (movingData != null) ? movingData.assumptions() : "Standard local move.",
                                // New fields
                                affordabilityTier,
                                nationalComparison,
                                legalSummary,
                                localInsight);
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
                        List<QnA> commonQuestions,
                        List<String> dataSources, // Links for E-E-A-T
                        String depositNotes, // Legal context
                        String movingAssumptions, // Context
                        // NEW: Thin Content Prevention Fields
                        String affordabilityTier, // "High-Cost", "Moderate", "Affordable"
                        int nationalComparison, // percentage vs national avg (e.g., +20, -15)
                        String legalSummary, // One-line legal highlight
                        String localInsight // City-specific unique fact
        ) {
        }

        public record QnA(String question, String answer) {
        }
}
