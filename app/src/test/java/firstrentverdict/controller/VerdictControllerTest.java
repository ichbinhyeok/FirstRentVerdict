package firstrentverdict.controller;

import firstrentverdict.model.verdict.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VerdictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndexPageLoads() throws Exception {
        mockMvc.perform(get("/RentVerdict/"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/index"))
                .andExpect(model().attribute("cities", notNullValue()));
    }

    @Test
    void testHomeRedirectIsPermanent() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/RentVerdict/"));
    }

    @Test
    void testDottedSlugRedirectsToCanonicalSlug() throws Exception {
        mockMvc.perform(get("/RentVerdict/verdict/st.-louis-mo"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/RentVerdict/verdict/st-louis-mo"));
    }

    @Test
    void testVerdictSubmission_Approved() throws Exception {
        // High cash scenario for NYC
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "New York|NY")
                .param("monthlyRent", "3000")
                .param("availableCash", "30000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/result"))
                .andExpect(model().attribute("result", notNullValue()));
    }

    @Test
    void testVerdictSubmissionRejectsInvalidFormRange() throws Exception {
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "New York|NY")
                .param("monthlyRent", "-1000")
                .param("availableCash", "5000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testVerdictSubmissionRejectsUnsupportedCity() throws Exception {
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "Fake City|NY")
                .param("monthlyRent", "3000")
                .param("availableCash", "5000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testResultPageContainsNoindexMetaTag() throws Exception {
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "New York|NY")
                .param("monthlyRent", "3000")
                .param("availableCash", "15000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex, nofollow\">")));
    }

    @Test
    void testCityUrlTrailingSlashRedirectsToCanonical() throws Exception {
        mockMvc.perform(get("/RentVerdict/verdict/new-york-ny/"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/RentVerdict/verdict/new-york-ny"));
    }

    @Test
    void testSavings3000PageIsNoindexBut5000IsIndexable() throws Exception {
        mockMvc.perform(get("/RentVerdict/verdict/can-i-move-with/3000/to/new-york-ny"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex, nofollow\">")));

        mockMvc.perform(get("/RentVerdict/verdict/can-i-move-with/5000/to/new-york-ny"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("<meta name=\"robots\" content=\"noindex, nofollow\">"))));
    }

    @Test
    void testRelocationPairPageIsNoindexByDefault() throws Exception {
        mockMvc.perform(get("/RentVerdict/verdict/moving-from/austin-tx/to/new-york-ny"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex, nofollow\">")));
    }

    @Test
    void testCompareRouteIsGone() throws Exception {
        mockMvc.perform(get("/RentVerdict/verdict/compare/austin-tx-vs-new-york-ny"))
                .andExpect(status().isGone());
    }

    @Test
    void testSitemapExcludesCompareAndLegacyDottedSlug() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("/RentVerdict/verdict/st.-louis-mo"))))
                .andExpect(content().string(not(containsString("/verdict/compare/"))))
                .andExpect(content().string(containsString("/RentVerdict/should-i-apply")))
                .andExpect(content().string(containsString("/RentVerdict/application-fee-risk-checker")))
                .andExpect(content().string(containsString("/RentVerdict/can-i-move-with/5000/to/austin-tx")))
                .andExpect(content().string(containsString("/RentVerdict/application-fee/150/in/austin-tx")))
                .andExpect(content().string(containsString("/RentVerdict/admin-fee/300/in/austin-tx")))
                .andExpect(content().string(containsString("/RentVerdict/first-last-security-deposit-in/austin-tx")))
                .andExpect(content().string(containsString("/RentVerdict/research/move-in-cost-index")))
                .andExpect(content().string(containsString("/RentVerdict/guide/rent-affordability-rule")));
    }

    @Test
    void testShouldIApplyToolPagesLoad() throws Exception {
        mockMvc.perform(get("/RentVerdict/should-i-apply"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("Run gates")))
                .andExpect(content().string(containsString("/RentVerdict/data-sources")));

        mockMvc.perform(get("/RentVerdict/application-fee-risk-checker"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("Fee gate")));

        mockMvc.perform(get("/RentVerdict/should-i-apply-in/austin-tx"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("Austin, TX")));
    }

    @Test
    void testTrustPagesLoad() throws Exception {
        mockMvc.perform(get("/RentVerdict/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/about"))
                .andExpect(content().string(containsString("virtual product and data team")));

        mockMvc.perform(get("/RentVerdict/methodology"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/methodology"))
                .andExpect(content().string(containsString("not a law firm")));

        mockMvc.perform(get("/RentVerdict/data-sources"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/data_sources"))
                .andExpect(content().string(containsString("Where the tool gets its assumptions")));

        mockMvc.perform(get("/RentVerdict/corrections"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/corrections"))
                .andExpect(content().string(containsString("Correction policy")));
    }

    @Test
    void testShouldIApplyScenarioUrlPrefillsCashAndRent() throws Exception {
        mockMvc.perform(get("/RentVerdict/can-i-apply-with/5000/for/1800/in/austin-tx"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("value=\"1800\"")))
                .andExpect(content().string(containsString("value=\"5000\"")));
    }

    @Test
    void testShouldIApplyFeeIncomeAndDepositScenarioPagesLoad() throws Exception {
        mockMvc.perform(get("/RentVerdict/application-fee/150/in/austin-tx"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("Application fee scenario")))
                .andExpect(content().string(containsString("$150 application fee before approval")))
                .andExpect(content().string(containsString("TX fee rule loaded")))
                .andExpect(content().string(containsString("Related scenarios")))
                .andExpect(content().string(containsString("name=\"applicationFee\"")))
                .andExpect(content().string(containsString("value=\"150\"")));

        mockMvc.perform(get("/RentVerdict/admin-fee/300/in/austin-tx"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("Admin fee scenario")))
                .andExpect(content().string(containsString("value=\"300\"")));

        mockMvc.perform(get("/RentVerdict/can-i-apply-with/4500/income-for/1500/rent-in/austin-tx"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("Income screen scenario")))
                .andExpect(content().string(containsString("3.0x")))
                .andExpect(content().string(containsString("value=\"4500\"")));

        mockMvc.perform(get("/RentVerdict/first-last-security-deposit-in/boston-ma"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply"))
                .andExpect(content().string(containsString("Deposit stack scenario")));
    }

    @Test
    void testShouldIApplySubmissionProducesNoindexResult() throws Exception {
        mockMvc.perform(post("/RentVerdict/should-i-apply")
                .param("cityState", "Austin|TX")
                .param("monthlyRent", "1800")
                .param("availableCash", "5000")
                .param("grossMonthlyIncome", "6000")
                .param("applicationFee", "75")
                .param("adminFee", "150")
                .param("holdingDeposit", "0")
                .param("moveInFee", "0")
                .param("securityDeposit", "1800")
                .param("prepaidRent", "0")
                .param("hasPet", "false")
                .param("petFee", "0")
                .param("utilityDeposit", "100")
                .param("incomeRule", "3x rent")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/should_i_apply_result"))
                .andExpect(model().attribute("result", notNullValue()))
                .andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex, nofollow\">")))
                .andExpect(content().string(containsString("Action plan")))
                .andExpect(content().string(containsString("Levers to test")))
                .andExpect(content().string(containsString("What would make this apply-safe?")))
                .andExpect(content().string(containsString("This is a risk screen, not legal, financial, or approval advice.")))
                .andExpect(content().string(containsString("data-copy-message")))
                .andExpect(content().string(containsString("Gate Output")));
    }

    @Test
    void testSitemapHasNoSelfRelocationPair() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("/RentVerdict/verdict/moving-from/new-york-ny/to/new-york-ny"))));
    }

    @Test
    void testRobotsServesSitemapDirective() throws Exception {
        mockMvc.perform(get("/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sitemap: ")));
    }

    @Test
    void testResearchPageLoads() throws Exception {
        String reportDate = LocalDate.now().withDayOfMonth(1).toString();

        mockMvc.perform(get("/RentVerdict/research/move-in-cost-index"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/research_move_in_index"))
                .andExpect(content().string(containsString("Move-In Cost Index")))
                .andExpect(content().string(containsString("\"datePublished\": \"" + reportDate + "\"")))
                .andExpect(content().string(not(containsString(reportDate.replace("-", "\\-")))));
    }

    @Test
    void testFirstMonthCostRedirectsToResearchIndex() throws Exception {
        mockMvc.perform(get("/RentVerdict/first-month-cost/3000/ny"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/RentVerdict/research/move-in-cost-index"));
    }

    @Test
    void testSimulateRejectsUnsupportedCity() throws Exception {
        mockMvc.perform(post("/RentVerdict/api/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "city":"Fake City",
                          "state":"NY",
                          "monthlyRent":3000,
                          "availableCash":12000,
                          "hasPet":false,
                          "isLocalMove":true,
                          "creditTier":"GOOD"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSimulateRequiresCreditTier() throws Exception {
        mockMvc.perform(post("/RentVerdict/api/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "city":"New York",
                          "state":"NY",
                          "monthlyRent":3000,
                          "availableCash":12000,
                          "hasPet":false,
                          "isLocalMove":true
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLongDistanceSimulateRequiresOrigin() throws Exception {
        mockMvc.perform(post("/RentVerdict/api/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "city":"New York",
                          "state":"NY",
                          "monthlyRent":3000,
                          "availableCash":12000,
                          "hasPet":false,
                          "isLocalMove":false,
                          "creditTier":"GOOD"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testVerdictSubmission_Denied() throws Exception {
        // Low cash scenario for NYC
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "New York|NY")
                .param("monthlyRent", "3000")
                .param("availableCash", "5000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/result"))
                .andExpect(model().attribute("result", notNullValue()));
    }

    @Test
    void testBetaScenario_NewYork_LegalCap() throws Exception {
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "New York|NY")
                .param("monthlyRent", "3800")
                .param("availableCash", "6000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/result"))
                .andExpect(model().attribute("result", notNullValue()))
                .andDo(result -> {
                    org.springframework.web.servlet.ModelAndView mv = result.getModelAndView();
                    assert mv != null;
                    VerdictResult vr = (VerdictResult) mv.getModel().get("result");
                    String depositNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Security Deposit"))
                            .findFirst().orElseThrow().annotation();
                    // NY Deposit annotation now contains legal context from state laws
                    if (!depositNote.contains("Strictly limited")) {
                        throw new AssertionError("NY Deposit expected note about strict limit, got: " + depositNote);
                    }
                });
    }

    @Test
    void testBetaScenario_Austin_PetRule() throws Exception {
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "Austin|TX")
                .param("monthlyRent", "2000")
                .param("availableCash", "10000")
                .param("hasPet", "true")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/result"))
                .andDo(result -> {
                    org.springframework.web.servlet.ModelAndView mv = result.getModelAndView();
                    assert mv != null;
                    VerdictResult vr = (VerdictResult) mv.getModel().get("result");
                    // Label changed to "Pet Deposit/Fee"
                    String petNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Pet Deposit/Fee"))
                            .findFirst().orElseThrow().annotation();
                    if (!petNote.startsWith("Range:")) {
                        throw new AssertionError("Austin Pet expected Range: prefix, got: " + petNote);
                    }
                    String depositNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Security Deposit"))
                            .findFirst().orElseThrow().annotation();
                    if (!depositNote.contains("Standard Rate")) {
                        throw new AssertionError("Austin Deposit expected Standard Rate, got: " + depositNote);
                    }
                });
    }

    @Test
    void testBetaScenario_SanFrancisco_HighCap() throws Exception {
        mockMvc.perform(post("/RentVerdict/verdict")
                .param("cityState", "San Francisco|CA")
                .param("monthlyRent", "3000")
                .param("availableCash", "10000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andDo(result -> {
                    org.springframework.web.servlet.ModelAndView mv = result.getModelAndView();
                    java.util.Objects.requireNonNull(mv);
                    VerdictResult vr = (VerdictResult) mv.getModel().get("result");
                    String depositNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Security Deposit"))
                            .findFirst().orElseThrow().annotation();
                    if (!depositNote.contains("Standard Rate")) {
                        throw new AssertionError("SF Deposit expected Standard Rate, got: " + depositNote);
                    }
                });
    }
}
