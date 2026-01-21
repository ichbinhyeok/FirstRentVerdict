package firstrentverdict.controller;

import firstrentverdict.model.verdict.Verdict;
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
}
