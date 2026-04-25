package firstrentverdict;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import firstrentverdict.model.dtos.*;
import firstrentverdict.repository.VerdictDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class DataConsistencyTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final LocalDate MINIMUM_V3_DATA_CHECK_DATE = LocalDate.parse("2026-04-24");

    @Autowired
    private VerdictDataRepository repository;

    @Test
    public void verifyAllCitiesHaveCompleteData() throws IOException {
        // DataLoadingService @PostConstruct might be skipped in some test setups or run
        // automatically.
        // We'll trust SpringBootTest to run it, but if not we can call it.
        // dataLoadingService.loadData();

        // Actually, let's just use the repository which should be populated.

        List<CitiesData.CityEntry> allCities = repository.getAllCities();
        if (allCities.isEmpty()) {
            fail("No cities loaded! Check DataLoadingService.");
        }

        System.out.println("Verifying " + allCities.size() + " cities...");

        StringBuilder errors = new StringBuilder();

        for (CitiesData.CityEntry city : allCities) {
            String c = city.city();
            String s = city.state();

            if (repository.getRent(c, s).isEmpty()) {
                errors.append(String.format("Missing Rent Data for %s, %s\n", c, s));
            }
            if (repository.getSecurityDeposit(c, s).isEmpty()) {
                errors.append(String.format("Missing Security Deposit Data for %s, %s\n", c, s));
            }
            if (repository.getMoving(c, s).isEmpty()) {
                errors.append(String.format("Missing Moving Data for %s, %s\n", c, s));
            }
            if (repository.getPet(c, s).isEmpty()) {
                errors.append(String.format("Missing Pet Data for %s, %s\n", c, s));
            }
            if (repository.getCashBuffer(c, s).isEmpty()) {
                errors.append(String.format("Missing Cash Buffer Data for %s, %s\n", c, s));
            }
        }

        if (errors.length() > 0) {
            fail("Data Consistency Errors Failed:\n" + errors.toString());
        } else {
            System.out.println("All cities have complete data!");
        }
    }

    @Test
    public void verifyV3ApplicationFeeRulesHaveFreshSources() throws IOException {
        JsonNode root = readDataFile("application_fee_rules.json");
        assertFreshV3Dataset(root, "application_fee_rules.json");

        JsonNode rules = root.path("rules");
        assertTrue(rules.isArray() && !rules.isEmpty(), "application fee rules must include at least one rule");

        for (JsonNode rule : rules) {
            assertRequiredText(rule, "state", "application_fee_rules.json");
            assertRequiredText(rule, "fee_type", "application_fee_rules.json");
            assertRequiredText(rule, "cap_type", "application_fee_rules.json");
            assertRequiredText(rule, "cap_formula", "application_fee_rules.json");
            assertSourceBackedRule(rule, "application_fee_rules.json");
        }
    }

    @Test
    public void verifyV3DepositPrepaidRulesHaveFreshSources() throws IOException {
        JsonNode root = readDataFile("deposit_prepaid_rules.json");
        assertFreshV3Dataset(root, "deposit_prepaid_rules.json");

        JsonNode rules = root.path("rules");
        assertTrue(rules.isArray() && !rules.isEmpty(), "deposit/prepaid rules must include at least one rule");

        for (JsonNode rule : rules) {
            assertRequiredText(rule, "state", "deposit_prepaid_rules.json");
            assertRequiredText(rule, "cap_type", "deposit_prepaid_rules.json");
            assertRequiredText(rule, "cap_formula", "deposit_prepaid_rules.json");
            assertTrue(rule.hasNonNull("return_deadline_days"), "deposit_prepaid_rules.json rule must include return_deadline_days");
            assertSourceBackedRule(rule, "deposit_prepaid_rules.json");
        }
    }

    @Test
    public void verifyV3IncomeScreeningAssumptionsAreNotLaws() throws IOException {
        JsonNode root = readDataFile("screening_income_assumptions.json");
        assertFreshV3Dataset(root, "screening_income_assumptions.json");

        JsonNode assumptions = root.path("assumptions");
        assertTrue(assumptions.isArray() && assumptions.size() >= 3, "income assumptions must include flexible, standard, and strict scenarios");

        boolean hasDefaultThreeX = false;
        for (JsonNode assumption : assumptions) {
            assertRequiredText(assumption, "threshold_label", "screening_income_assumptions.json");
            assertRequiredText(assumption, "risk_band", "screening_income_assumptions.json");
            assertRequiredText(assumption, "description", "screening_income_assumptions.json");
            assertRequiredText(assumption, "confidence", "screening_income_assumptions.json");
            assertTrue(assumption.path("rent_to_income_multiplier").asDouble(0) > 0, "income multiplier must be positive");
            assertFalse(assumption.path("is_law").asBoolean(true), "income screening assumptions must not be labeled as laws");

            if (Math.abs(assumption.path("rent_to_income_multiplier").asDouble() - 3.0) < 0.001) {
                hasDefaultThreeX = true;
            }
        }

        assertTrue(hasDefaultThreeX, "income assumptions must include a 3x rent default scenario");
    }

    @Test
    public void verifyV3ApplicationRiskVocabularySupportsToolFirstPages() throws IOException {
        JsonNode root = readDataFile("application_risk_vocabulary.json");
        assertFreshV3Dataset(root, "application_risk_vocabulary.json");

        JsonNode terms = root.path("terms");
        assertTrue(terms.isArray() && terms.size() >= 10, "application risk vocabulary needs enough terms for tool-first page surfaces");

        for (JsonNode term : terms) {
            assertRequiredText(term, "term", "application_risk_vocabulary.json");
            assertRequiredText(term, "category", "application_risk_vocabulary.json");
            assertRequiredText(term, "user_question", "application_risk_vocabulary.json");
            assertRequiredText(term, "tool_prompt", "application_risk_vocabulary.json");
        }
    }

    private JsonNode readDataFile(String fileName) throws IOException {
        return OBJECT_MAPPER.readTree(new ClassPathResource("data/" + fileName).getInputStream());
    }

    private void assertFreshV3Dataset(JsonNode root, String fileName) {
        assertRequiredText(root, "dataset", fileName);
        assertRequiredText(root, "usage_note", fileName);
        assertRequiredText(root, "last_checked", fileName);

        LocalDate lastChecked = LocalDate.parse(root.path("last_checked").asText());
        assertFalse(
                lastChecked.isBefore(MINIMUM_V3_DATA_CHECK_DATE),
                fileName + " last_checked must be on or after " + MINIMUM_V3_DATA_CHECK_DATE
        );
    }

    private void assertSourceBackedRule(JsonNode rule, String fileName) {
        assertRequiredText(rule, "source_url", fileName);
        assertRequiredText(rule, "source_type", fileName);
        assertRequiredText(rule, "source_citation", fileName);
        assertRequiredText(rule, "product_risk_note", fileName);
        assertRequiredText(rule, "last_checked", fileName);
        assertRequiredText(rule, "confidence", fileName);

        assertTrue(rule.path("source_url").asText().startsWith("http"), fileName + " source_url must be a web URL");
        assertTrue(List.of("high", "medium", "low").contains(rule.path("confidence").asText()), fileName + " confidence must be high, medium, or low");

        LocalDate lastChecked = LocalDate.parse(rule.path("last_checked").asText());
        assertFalse(
                lastChecked.isBefore(MINIMUM_V3_DATA_CHECK_DATE),
                fileName + " rule last_checked must be on or after " + MINIMUM_V3_DATA_CHECK_DATE
        );
    }

    private void assertRequiredText(JsonNode node, String fieldName, String fileName) {
        assertTrue(
                node.hasNonNull(fieldName) && !node.path(fieldName).asText().isBlank(),
                fileName + " must include non-empty " + fieldName
        );
    }
}
