package firstrentverdict.service.seo;

import firstrentverdict.model.dtos.RentData;
import firstrentverdict.model.dtos.SecurityDepositData;
import firstrentverdict.model.dtos.MovingData;
import firstrentverdict.model.dtos.PetData;
import firstrentverdict.model.dtos.CityInsightData;
import firstrentverdict.model.dtos.CityEconomicFactsData;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.calc.DistanceCalculator;
import firstrentverdict.service.calc.MovingCostCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CityContentGenerator {

        private static final Set<String> BROKER_FEE_CITIES = Set.of(
                        "new york|ny", "boston|ma", "jersey city|nj", "hoboken|nj");
        private static final double BROKER_FEE_PERCENT = 0.10;
        private static final double DEFAULT_RELOCATION_DISTANCE_MILES = 1000.0;

        private final VerdictDataRepository repository;
        private final MovingCostCalculator movingCostCalculator;
        private final DistanceCalculator distanceCalculator;

        public CityContentGenerator(
                        VerdictDataRepository repository,
                        MovingCostCalculator movingCostCalculator,
                        DistanceCalculator distanceCalculator) {
                this.repository = repository;
                this.movingCostCalculator = movingCostCalculator;
                this.distanceCalculator = distanceCalculator;
        }

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
                        CityEconomicFactsData.CityEconomicFact economicFact,
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
                        if (intent == Intent.CREDIT_POOR && depositData.city_practice().highRiskMultipliers() != null
                                        && !depositData.city_practice().highRiskMultipliers().isEmpty()) {
                                depositMult = depositData.city_practice().highRiskMultipliers().stream()
                                                .mapToDouble(Double::doubleValue).max().orElse(depositMult);
                        }
                        depositNotes = depositData.city_practice().notes();
                }

                java.util.Optional<firstrentverdict.model.dtos.StateLawData.StateLaw> stateLawOpt = repository
                                .getStateLaw(state);
                if (stateLawOpt.isPresent() && stateLawOpt.get().legalCapMultiplier() != null) {
                        double cap = stateLawOpt.get().legalCapMultiplier();
                        if (depositMult > cap) {
                                depositMult = cap;
                                depositNotes = depositNotes + " (Capped by State Law)";
                        }
                }

                int deposit = (int) (avgRent * depositMult);

                // 2. Precise Moving Logic (aligned with simulation engine)
                int moving = (movingData != null) ? movingData.typical() : 500;
                String movingNotes = (movingData != null) ? movingData.assumptions() : "Standard local move.";

                if (intent == Intent.RELOCATION) {
                        moving = estimateLongDistanceCost(movingData, DEFAULT_RELOCATION_DISTANCE_MILES);
                        movingNotes = String.format(
                                        "Long-distance estimate using %,.0f-mile baseline (engine-aligned).",
                                        DEFAULT_RELOCATION_DISTANCE_MILES);
                }

                if (intent == Intent.RELOCATION_PAIR && contextValue instanceof String fromCity) {
                        java.util.Optional<firstrentverdict.model.dtos.CityCoordinates.CityCoordinate> toCoord = repository
                                        .getCityCoordinate(city, state);
                        String[] fromParts = parseCitySlug(fromCity);
                        if (fromParts != null) {
                                java.util.Optional<firstrentverdict.model.dtos.CityCoordinates.CityCoordinate> fromCoord = repository
                                                .getCityCoordinate(fromParts[0], fromParts[1]);
                                if (toCoord.isPresent() && fromCoord.isPresent()) {
                                        double distance = distanceCalculator.calculateMiles(
                                                        fromCoord.get().lat(), fromCoord.get().lng(),
                                                        toCoord.get().lat(), toCoord.get().lng());
                                        moving = estimateLongDistanceCost(movingData, distance);
                                        movingNotes = "Estimates based on distance (" + (int) distance + " miles).";
                                }
                        }
                }

                // 3. Precise Pet Logic
                int petDeposit = (petData != null && petData.oneTime() != null) ? petData.oneTime().avg() : 300;
                int petRentMonthly = (petData != null && petData.monthlyPetRent() != null)
                                ? petData.monthlyPetRent().avg()
                                : 35;
                String petNotes = (petData != null && petData.oneTime() != null) ? petData.oneTime().notes()
                                : "Standard pet fees apply.";

                int brokerFee = hasBrokerFee(city, state) ? (int) (avgRent * 12 * BROKER_FEE_PERCENT) : 0;

                int upfrontTotal = avgRent + deposit + moving + brokerFee;
                if (intent == Intent.PET_FRIENDLY) {
                        upfrontTotal += petDeposit + petRentMonthly;
                }

                Integer medianHouseholdIncome = economicFact != null ? economicFact.medianHouseholdIncome() : null;
                Double renterSharePct = economicFact != null ? economicFact.renterSharePct() : null;
                Double rentBurdenSharePct = economicFact != null ? economicFact.rentBurdened35PlusSharePct() : null;
                Double annualRentToIncomePct = economicFact != null ? economicFact.annualRentToIncomePct() : null;
                String economicSignal = buildEconomicSignal(annualRentToIncomePct, rentBurdenSharePct);

                // 4. Content Generation
                String pageTitle;
                String metaDescription;
                String localInsight = "";
                List<QnA> qnaList = new ArrayList<>();

                // Meta suffix kept concise for search snippets.
                String ctrSuffix = " Updated for 2026.";

                switch (intent) {
                        case PET_FRIENDLY -> {
                                pageTitle = String.format(
                                                "Average Pet Deposit in %s: Fees, Pet Rent, and Move-In Cost (2026)",
                                                city);
                                metaDescription = String.format(
                                                "Average pet deposit in %s is about $%,d, with pet rent near $%,d/month. See the full pet-friendly move-in cost before you apply.%s",
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
                                pageTitle = String.format("Can I Move to %s with $%s? (2026 Cash Check)", city,
                                                String.format("%,d", savings));
                                metaDescription = String.format(
                                                "Is $%s enough for %s? Typical move-in cash is about $%,d for rent, deposit, and moving. Compare your buffer before applying.%s",
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
                                String multStr = depositMult == (long) depositMult
                                                ? String.format("%d", (long) depositMult)
                                                : String.format("%.1f", depositMult);

                                pageTitle = String.format("Renting in %s with Poor Credit (2026 Approval Guide)", city);
                                metaDescription = String.format(
                                                "Credit under 600 in %s? Expect higher deposit pressure (up to %sx, around $%,d). Use this approval checklist before you apply.%s",
                                                city, multStr, deposit, ctrSuffix);
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
                                                "Have a 640 score in %s? See realistic deposit expectations and the 3 steps that improve lease approval odds.%s",
                                                city, ctrSuffix);
                                localInsight = "Fair Credit Tip: Most corporate landlords here will approve a 620+ score but may request a full month's security deposit.";

                                qnaList.add(new QnA("Is 640 a good enough score for apartments in " + city + "?",
                                                "It is often the 'borderline' score. You are likely to be approved, but without the best move-in incentives."));
                        }
                        case CREDIT_GOOD -> {
                                pageTitle = String.format(
                                                "Renting in %s with Good Credit (700+): Priority Move-In Guide", city);
                                metaDescription = String.format(
                                                "Use your 700+ score in %s to target low-deposit listings, fee waivers, and stronger move-in incentives.%s",
                                                city, ctrSuffix);
                                localInsight = "Good Credit Power: With a 700+ score, you have high leverage to negotiate $0 security deposit or waived application fees.";

                                qnaList.add(new QnA("Can I get a $0 deposit in " + city + " with 750 credit?",
                                                "Yes, many luxury buildings and national property managers offer 'Deposit-Free' options for top-tier credit profiles."));
                        }
                        case RELOCATION -> {
                                pageTitle = String.format("Moving to %s, %s: Full Move-In Cost Guide (2026)", city, state);
                                metaDescription = String.format(
                                                "Planning a move to %s? Typical move-in cash is about $%,d, including rent, deposit, and relocation costs. Use the local checklist before signing.%s",
                                                city, upfrontTotal, ctrSuffix);
                                localInsight = String.format(
                                                "Relocation snapshot: Most first-month movers into %s should plan for around $%,d in total liquidity before lease signing.",
                                                city, upfrontTotal);
                        }
                        case RELOCATION_PAIR -> {
                                String fromCitySlug = (String) contextValue;
                                String fromCity = fromCitySlug;
                                String[] fParts = parseCitySlug(fromCitySlug);
                                if (fParts != null) {
                                        fromCity = java.util.Arrays.stream(fParts[0].split(" "))
                                                        .map(s -> s.length() > 0
                                                                        ? s.substring(0, 1).toUpperCase()
                                                                                        + s.substring(1)
                                                                        : "")
                                                        .collect(java.util.stream.Collectors.joining(" "));
                                }
                                pageTitle = String.format("Moving from %s to %s Cost Audit (2026)", fromCity, city);
                                metaDescription = String.format(
                                                "Relocating from %s to %s? Expected move-in cash is about $%,d when rent, deposit, and distance costs are combined.%s",
                                                fromCity, city, upfrontTotal, ctrSuffix);
                                localInsight = String.format(
                                                "Cost Shift: Median rent in %s is $%,d. Moving from %s typically adds $%,d to the bill.",
                                                city, avgRent, fromCity, moving);

                                qnaList.add(new QnA("Is " + city + " cheaper than " + fromCity + "?",
                                                "Median rent in " + city + " is $" + String.format("%,d", avgRent)
                                                                + ". Compare this to your current costs."));
                        }
                        default -> {
                                pageTitle = String.format("Can You Afford an Apartment in %s, %s? Move-In Cash Guide (2026)", city, state);
                                metaDescription = String.format(
                                                "Need to move into %s? Typical upfront cash is about $%s between rent, deposit, and moving. Check the real approval threshold before you apply.%s",
                                                city, String.format("%,d", avgRent), String.format("%,d", upfrontTotal),
                                                ctrSuffix);
                                localInsight = String.format(
                                                "Approval Snapshot: %s Median 1BR rent in %s is $%,d, but the bigger hurdle is clearing the full move-in cash requirement.",
                                                depositNotes, city, avgRent);

                                qnaList.add(new QnA("What income do I usually need to rent in " + city + "?",
                                                "Landlords require gross income to be 3x rent. In " + city
                                                                + ", you'll need around $"
                                                                + String.format("%,d", avgRent * 3) + "/mo."));
                        }
                }

                if (brokerFee > 0) {
                        localInsight += String.format(" This market often includes broker fees around $%,d (10%% of annual rent).",
                                        brokerFee);
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

                if (medianHouseholdIncome != null || rentBurdenSharePct != null || annualRentToIncomePct != null) {
                        StringBuilder econNarrative = new StringBuilder("Market pressure signal: ");
                        if (medianHouseholdIncome != null) {
                                econNarrative.append("median household income is $")
                                                .append(String.format("%,d", medianHouseholdIncome))
                                                .append(". ");
                        }
                        if (annualRentToIncomePct != null) {
                                econNarrative.append("Median annual rent is about ")
                                                .append(String.format("%.1f", annualRentToIncomePct))
                                                .append("% of median household income. ");
                        }
                        if (rentBurdenSharePct != null) {
                                econNarrative.append(String.format("%.1f", rentBurdenSharePct))
                                                .append("% of renter households spend 35%+ of income on rent.");
                        }
                        localInsight = (localInsight + " " + econNarrative).trim();

                        if (annualRentToIncomePct != null) {
                                qnaList.add(new QnA(
                                                "How stretched is rent vs income in " + city + "?",
                                                "ACS data indicates the annual rent-to-income ratio is roughly "
                                                                + String.format("%.1f", annualRentToIncomePct)
                                                                + "%, which we classify as " + economicSignal
                                                                + " pressure."));
                        }
                }

                // Universal QnA for every page
                String universalCostContext = "Usually, you'll need the first month's rent, a security deposit, and moving logistics";
                if (brokerFee > 0) {
                        universalCostContext += ", plus a broker fee";
                }
                if (intent == Intent.PET_FRIENDLY) {
                        universalCostContext += ", plus a pet deposit and first-month pet rent";
                }
                universalCostContext += ".";
                qnaList.add(new QnA("What are the typical upfront costs for a rental in " + city + "?",
                                universalCostContext + " Total estimate: $"
                                                + String.format("%,d", upfrontTotal) + "."));

                List<String> dataSources = new ArrayList<>(List.of(
                                "2026 Internal Market Index",
                                "US Census ACS",
                                "Local Rental Survey"));
                if (economicFact != null && economicFact.sources() != null && !economicFact.sources().isEmpty()) {
                        dataSources.add("US Census ACS Place-Level API");
                }

                String riskNarrative = "Liquidity Risk: The total $%,d move-in cost is the primary hurdle for %s renters."
                                .formatted(upfrontTotal, city);
                if (annualRentToIncomePct != null || rentBurdenSharePct != null) {
                        riskNarrative += " Economic pressure is " + economicSignal + " based on ACS affordability metrics.";
                }

                return new CityPageContent(
                                pageTitle, metaDescription, city, state, avgRent,
                                (int) (avgRent * 0.8), (int) (avgRent * 1.2), avgRent * 3, avgRent * 36,
                                deposit, moving, (int) (moving * 0.6), (int) (moving * 1.4), upfrontTotal,
                                qualitativeStory.isEmpty() ? String.format(
                                                "Moving into %s requires significant upfront liquidity.", city)
                                                : qualitativeStory,
                                String.format("Income Verification: You need $%,d/mo to pass the standard 3x income test.",
                                                avgRent * 3),
                                "Audited Cost Report",
                                riskNarrative,
                                qnaList, dataSources,
                                depositNotes, movingNotes,
                                (avgRent > 2000 ? "High-Cost" : "Moderate"), 0,
                                localLaw,
                                localInsight,
                                petDeposit, petRentMonthly, petNotes,
                                medianHouseholdIncome, renterSharePct, rentBurdenSharePct, annualRentToIncomePct,
                                economicSignal,
                                seasonalTip, renterAdvice, intent == Intent.PET_FRIENDLY);
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
                        Integer medianHouseholdIncome, Double renterSharePct, Double rentBurdenSharePct,
                        Double annualRentToIncomePct, String economicSignal,
                        String seasonalTip, String renterAdvice, boolean isPetIntent) {
        }

        public record QnA(String question, String answer) {
        }

        private String buildEconomicSignal(Double annualRentToIncomePct, Double rentBurdenSharePct) {
                if (annualRentToIncomePct == null && rentBurdenSharePct == null) {
                        return "moderate";
                }

                boolean highRentToIncome = annualRentToIncomePct != null && annualRentToIncomePct >= 30.0;
                boolean highBurden = rentBurdenSharePct != null && rentBurdenSharePct >= 40.0;
                boolean midRentToIncome = annualRentToIncomePct != null && annualRentToIncomePct >= 24.0;
                boolean midBurden = rentBurdenSharePct != null && rentBurdenSharePct >= 32.0;

                if (highRentToIncome || highBurden) {
                        return "high";
                }
                if (midRentToIncome || midBurden) {
                        return "elevated";
                }
                return "moderate";
        }

        private String[] parseCitySlug(String slug) {
                int lastDash = slug.lastIndexOf('-');
                if (lastDash > 0 && lastDash < slug.length() - 1) {
                        String cityName = slug.substring(0, lastDash).replace("-", " ");
                        String stateCode = slug.substring(lastDash + 1);
                        return new String[] { cityName, stateCode };
                }
                return null;
        }

        private boolean hasBrokerFee(String city, String state) {
                String key = city.trim().toLowerCase() + "|" + state.trim().toLowerCase();
                return BROKER_FEE_CITIES.contains(key);
        }

        private int estimateLongDistanceCost(MovingData.CityMoving movingData, double distance) {
                if (movingData == null) {
                        return movingCostCalculator.estimateLongDistanceCost(Math.max(distance, 50));
                }
                return movingCostCalculator.calculateCost(false, distance, movingData);
        }
}
