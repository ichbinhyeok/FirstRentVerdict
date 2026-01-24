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

        return new CityPageContent(
                String.format("Upfront Cash Requirements for Renting in %s, %s (2026)", city, state), // Title
                String.format(
                        "View detailed rental financial data for %s, including typical upfront costs ($%s) and income guidelines.",
                        city, String.format("%,d", upfrontTotal)), // Meta Desc
                city,
                state,
                avgRent,
                requiredIncomeMonthly,
                requiredIncomeYearly,
                deposit,
                moving,
                upfrontTotal,
                marketContext,
                incomeLogic);
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
            String incomeLogicText) {
    }
}
