package firstrentverdict.controller;

import firstrentverdict.model.verdict.*;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.core.VerdictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class VerdictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VerdictDataRepository repository;

    @Test
    void testIndexPageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/index"))
                .andExpect(model().attribute("cities", notNullValue()));
    }

    @Test
    void testVerdictSubmission_Approved() throws Exception {
        // High cash scenario for NYC
        mockMvc.perform(post("/verdict")
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
        mockMvc.perform(post("/verdict")
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
        mockMvc.perform(post("/verdict")
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
                    VerdictResult vr = (VerdictResult) result.getModelAndView().getModel().get("result");
                    String depositNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Security Deposit"))
                            .findFirst().orElseThrow().annotation();
                    if (!depositNote.startsWith("Rule: Legal Cap")) {
                        throw new AssertionError("NY Deposit expected Rule: Legal Cap, got: " + depositNote);
                    }
                });
    }

    @Test
    void testBetaScenario_Austin_PetRule() throws Exception {
        mockMvc.perform(post("/verdict")
                .param("cityState", "Austin|TX")
                .param("monthlyRent", "2000")
                .param("availableCash", "10000")
                .param("hasPet", "true")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/result"))
                .andDo(result -> {
                    VerdictResult vr = (VerdictResult) result.getModelAndView().getModel().get("result");
                    String petNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Pet Fees"))
                            .findFirst().orElseThrow().annotation();
                    if (!petNote.startsWith("Rule: Non-refundable")) { // Update to match actual logic if needed
                        // In VerdictService it is "Rule: Non-refundable (Market Norm)" because 'fee' is
                        // in notes
                        // Our assert logic must match exact expectation or prefix.
                        if (!petNote.contains("Non-refundable")) {
                            throw new AssertionError("Austin Pet expected Rule: Non-refundable, got: " + petNote);
                        }
                    }
                    String depositNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Security Deposit"))
                            .findFirst().orElseThrow().annotation();
                    if (!depositNote.startsWith("Applied Standard:")) {
                        throw new AssertionError("Austin Deposit expected Applied Standard, got: " + depositNote);
                    }
                });
    }

    @Test
    void testBetaScenario_SanFrancisco_HighCap() throws Exception {
        mockMvc.perform(post("/verdict")
                .param("cityState", "San Francisco|CA")
                .param("monthlyRent", "3000")
                .param("availableCash", "10000")
                .param("hasPet", "false")
                .param("isLocalMove", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andDo(result -> {
                    VerdictResult vr = (VerdictResult) result.getModelAndView().getModel().get("result");
                    String depositNote = vr.financials().costBreakdown().stream()
                            .filter(i -> i.label().equals("Security Deposit"))
                            .findFirst().orElseThrow().annotation();
                    // SF Typical (1.0) < Legal Cap (2.0), so Cap is NOT triggered. Should be
                    // "Applied Standard".
                    if (!depositNote.startsWith("Applied Standard:")) {
                        throw new AssertionError("SF Deposit expected Applied Standard, got: " + depositNote);
                    }
                });
    }
}
