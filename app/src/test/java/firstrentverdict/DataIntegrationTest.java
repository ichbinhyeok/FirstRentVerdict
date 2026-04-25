package firstrentverdict;

import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.model.dtos.*;
import firstrentverdict.service.calc.MovingCostCalculator;
import firstrentverdict.service.seo.CityContentGenerator;
import firstrentverdict.controller.SitemapController;
import firstrentverdict.controller.VerdictController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataIntegrationTest {

    @Autowired
    private VerdictDataRepository repository;

    @Autowired
    private CityContentGenerator cityContentGenerator;

    @Autowired
    private MovingCostCalculator movingCostCalculator;

    @Autowired
    private SitemapController sitemapController;

    @Autowired
    private VerdictController verdictController;

    @Test
    void verifyDataLoading_NewYork() {
        // Rent
        Optional<RentData.CityRent> rent = repository.getRent("New York", "NY");
        assertTrue(rent.isPresent(), "Rent data for NY should exist");
        assertEquals(3850, rent.get().median());

        // Security Deposit (Merged Logic Check)
        Optional<SecurityDepositData> deposit = repository.getSecurityDeposit("New York", "NY");
        assertTrue(deposit.isPresent(), "Deposit data for NY should exist");
        assertNotNull(deposit.get().city_practice(), "City Practice should not be null");
        assertEquals(Double.valueOf(1.0), deposit.get().city_practice().typicalMultipliers().get(0),
                "NY Typical Multiplier should be 1.0");

        // Cash Buffer
        Optional<CashBufferData.CityBuffer> buffer = repository.getCashBuffer("New York", "NY");
        assertTrue(buffer.isPresent());

        // Moving
        Optional<MovingData.CityMoving> moving = repository.getMoving("New York", "NY");
        assertTrue(moving.isPresent());
    }

    @Test
    void verifyDataLoading_TexasWithoutCap() {
        // Texas (Austin) has no legal cap, check if null or logic handled
        Optional<SecurityDepositData> deposit = repository.getSecurityDeposit("Austin", "TX");
        assertTrue(deposit.isPresent());
        assertNotNull(deposit.get().city_practice(), "City Practice should not be null");
        // Check typical multiplier exists
        assertFalse(deposit.get().city_practice().typicalMultipliers().isEmpty(),
                "Typically multipliers list should not be empty");
    }

    @Test
    void verifyCityInsightsLoaded() {
        Optional<CityInsightData.CityInsight> insight = repository.getCityInsight("New York", "NY");
        assertTrue(insight.isPresent(), "City insights should load for New York");
        assertNotNull(insight.get().renterAdvice(), "City insight renter advice should not be null");
    }

    @Test
    void verifyCityEconomicFactsLoaded() {
        Optional<CityEconomicFactsData.CityEconomicFact> econ = repository.getCityEconomicFact("New York", "NY");
        assertTrue(econ.isPresent(), "Economic facts should load for New York");
        assertNotNull(econ.get().medianHouseholdIncome(), "Median household income should be available");
        assertNotNull(econ.get().rentBurdened35PlusSharePct(), "Rent burden share should be available");
    }

    @Test
    void verifyStateMigrationFlowsLoaded() {
        Optional<StateMigrationFlowsData.StateFlow> txFlow = repository.getStateMigrationFlow("TX");
        assertTrue(txFlow.isPresent(), "State migration flow should load for TX");
        assertNotNull(txFlow.get().topOrigins(), "TX top origins should not be null");
        assertFalse(txFlow.get().topOrigins().isEmpty(), "TX top origins should not be empty");
        assertEquals("CA", txFlow.get().topOrigins().get(0).fromState(),
                "TX top inbound origin should be CA based on IRS 2021-2022");
    }

    @Test
    void verifyV3RiskRulesLoaded() {
        assertTrue(repository.getApplicationFeeRule("NY").isPresent(), "NY application fee rule should load");
        assertTrue(repository.getDepositPrepaidRule("CA").isPresent(), "CA deposit/prepaid rule should load");
        assertTrue(repository.getIncomeAssumption("3x rent").isPresent(), "3x income assumption should load");
        assertFalse(repository.getRiskVocabulary().isEmpty(), "V3 risk vocabulary should load");
    }

    @Test
    void verifyCityContentEstimatesIncludeBrokerAndPetMonthlyForNewYork() {
        var rent = repository.getRent("New York", "NY").orElseThrow();
        var deposit = repository.getSecurityDeposit("New York", "NY").orElseThrow();
        var moving = repository.getMoving("New York", "NY").orElseThrow();
        var pet = repository.getPet("New York", "NY").orElseThrow();
        var econ = repository.getCityEconomicFact("New York", "NY").orElse(null);

        var general = cityContentGenerator.generate(
                "New York", "NY", rent, deposit, moving, pet, null, econ,
                CityContentGenerator.Intent.GENERAL, null);
        var petPage = cityContentGenerator.generate(
                "New York", "NY", rent, deposit, moving, pet, null, econ,
                CityContentGenerator.Intent.PET_FRIENDLY, null);

        int base = rent.median();
        int depositCost = (int) (base * deposit.city_practice().typicalMultipliers().get(0));
        int movingCost = moving.typical();
        int brokerFee = (int) (base * 12 * 0.10);

        int expectedGeneral = base + depositCost + movingCost + brokerFee;
        int expectedPet = expectedGeneral + pet.oneTime().avg() + pet.monthlyPetRent().avg();

        assertEquals(expectedGeneral, general.totalUpfrontEstimate());
        assertEquals(expectedPet, petPage.totalUpfrontEstimate());
        assertNotNull(general.economicSignal(), "Economic signal should be populated when ACS data exists");
        assertTrue(general.localInsight().contains("Market pressure signal"),
                "Local insight should include ACS-based market pressure narrative");
    }

    @Test
    void verifyRelocationEstimateUsesEngineLongDistanceFormula() {
        var rent = repository.getRent("Austin", "TX").orElseThrow();
        var deposit = repository.getSecurityDeposit("Austin", "TX").orElseThrow();
        var moving = repository.getMoving("Austin", "TX").orElseThrow();
        var pet = repository.getPet("Austin", "TX").orElseThrow();
        var econ = repository.getCityEconomicFact("Austin", "TX").orElse(null);

        var relocation = cityContentGenerator.generate(
                "Austin", "TX", rent, deposit, moving, pet, null, econ,
                CityContentGenerator.Intent.RELOCATION, null);

        int expectedMoving = movingCostCalculator.calculateCost(false, 1000.0, moving);
        assertEquals(expectedMoving, relocation.typicalMoving());
    }

    @Test
    void verifySitemapFocusedDefaults() {
        String xml = sitemapController.sitemap();
        int locCount = xml.split("<loc>").length - 1;
        assertTrue(locCount <= 1000,
                "V3 tool-state sitemap should stay below the first-wave ceiling, but got " + locCount + " URLs");
        assertFalse(xml.contains("/RentVerdict/verdict/moving-from/"),
                "Sitemap should exclude relocation-pair combinations by default");
        assertTrue(xml.contains("/RentVerdict/verdict/can-i-move-with/5000/to/austin-tx"),
                "Sitemap should keep only the focused savings amount");
        assertFalse(xml.contains("/RentVerdict/verdict/can-i-move-with/3000/to/austin-tx"),
                "Sitemap should omit non-focused savings amounts");
        assertFalse(xml.contains("/RentVerdict/verdict/credit/good/austin-tx"),
                "Sitemap should omit credit-good pages by default");
        assertTrue(xml.contains("/RentVerdict/research/move-in-cost-index"),
                "Sitemap should include the research asset page");
    }

    @Test
    void verifyRelocationPageIncludesTopOriginStatesModelAttribute() {
        var model = new ConcurrentModel();
        Object view = verdictController.relocationPage("austin-tx", model);
        assertEquals("pages/relocation_landing", view);
        @SuppressWarnings("unchecked")
        List<String> topOriginStates = (List<String>) model.getAttribute("topOriginStates");
        assertNotNull(topOriginStates, "topOriginStates should be attached for relocation pages");
        assertFalse(topOriginStates.isEmpty(), "topOriginStates should contain IRS inbound states");
        assertTrue(topOriginStates.contains("CA"), "Texas should include CA as a top inbound origin");
    }
}
