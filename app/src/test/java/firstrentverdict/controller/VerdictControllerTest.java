package firstrentverdict.controller;

import firstrentverdict.model.verdict.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
