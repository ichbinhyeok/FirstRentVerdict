package firstrentverdict.service.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstrentverdict.model.dtos.*;
import firstrentverdict.repository.VerdictDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class JsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(JsonDataLoader.class);

    private final VerdictDataRepository repository;
    private final ObjectMapper objectMapper;

    public JsonDataLoader(VerdictDataRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        loadCities();
        loadRentData();
        loadSecurityDepositData();
        loadMovingData();
        loadPetData();
        loadCashBufferData();
        loadCityCoordinates();
        loadCityInsights();
        loadCityEconomicFacts();
        loadStateMigrationFlows();

        log.info("Data loading complete. Total cities supported: {}", repository.getAllCities().size());
    }

    private void loadCities() throws Exception {
        CitiesData data = readRequired("cities data", CitiesData.class, "data/cities.json");
        data.cities().forEach(repository::addCity);
        log.info("Loaded {} cities.", data.cities().size());
    }

    private <T> T readRequired(String label, Class<T> type, String... candidatePaths) throws Exception {
        T data = readOptional(label, type, candidatePaths);
        if (data == null) {
            throw new IllegalStateException("Missing required resource for " + label);
        }
        return data;
    }

    private <T> T readOptional(String label, Class<T> type, String... candidatePaths) throws Exception {
        for (String path : candidatePaths) {
            ClassPathResource resource = new ClassPathResource(path);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    log.info("Loading {} from {}", label, path);
                    return objectMapper.readValue(is, type);
                }
            }
        }
        log.warn("Could not find {}. Tried: {}", label, java.util.Arrays.toString(candidatePaths));
        return null;
    }

    private void loadRentData() throws Exception {
        RentData data = readRequired("rent data", RentData.class, "data/rent_data.json");
        data.cities().forEach(repository::addRent);
        log.info("Loaded rent data for {} cities.", data.cities().size());
    }

    private void loadSecurityDepositData() throws Exception {
        SecurityDepositWrapper wrapper = readRequired(
                "security deposit data",
                SecurityDepositWrapper.class,
                "data/security_deposit.json");

        if (wrapper.state_laws() != null) {
            wrapper.state_laws().forEach(repository::addStateLaw);
        }

        if (wrapper.cities() != null) {
            wrapper.cities().forEach(repository::addSecurityDeposit);
        }
        log.info(
                "Loaded security deposit data for {} cities and {} states.",
                wrapper.cities() != null ? wrapper.cities().size() : 0,
                wrapper.state_laws() != null ? wrapper.state_laws().size() : 0);
    }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    private record SecurityDepositWrapper(
            List<StateLawData.StateLaw> state_laws,
            List<SecurityDepositData> cities) {
    }

    private void loadMovingData() throws Exception {
        MovingData data = readRequired("moving data", MovingData.class, "data/moving_data.json");
        data.cities().forEach(repository::addMoving);
        log.info("Loaded moving data for {} cities.", data.cities().size());
    }

    private void loadPetData() throws Exception {
        PetData data = readRequired("pet data", PetData.class, "data/pet_data.json");
        data.cities().forEach(repository::addPet);
        log.info("Loaded pet data for {} cities.", data.cities().size());
    }

    private void loadCashBufferData() throws Exception {
        CashBufferData data = readRequired("cash buffer data", CashBufferData.class, "data/cash_buffer.json");
        data.cities().forEach(repository::addCashBuffer);
        log.info("Loaded cash buffer data for {} cities.", data.cities().size());
    }

    private void loadCityCoordinates() throws Exception {
        CityCoordinates data = readRequired("city coordinates", CityCoordinates.class, "data/city_coordinates.json");
        data.cities().forEach(repository::addCityCoordinate);
        log.info("Loaded coordinates for {} cities.", data.cities().size());
    }

    private void loadCityInsights() throws Exception {
        CityInsightData data = readOptional(
                "city insights",
                CityInsightData.class,
                "data/city_insights.json");

        if (data == null || data.data() == null || data.data().isEmpty()) {
            log.warn("City insights were not loaded; SEO pages will use generic local insights.");
            return;
        }

        data.data().forEach(repository::addCityInsight);
        log.info("Loaded qualitative insights for {} cities.", data.data().size());
    }

    private void loadCityEconomicFacts() throws Exception {
        CityEconomicFactsData data = readOptional(
                "city economic facts",
                CityEconomicFactsData.class,
                "data/city_economic_facts.json");

        if (data == null || data.cities() == null || data.cities().isEmpty()) {
            log.warn("City economic facts were not loaded; SEO pages will use base market metrics only.");
            return;
        }

        data.cities().stream()
                .filter(fact -> fact.missingReason() == null)
                .forEach(repository::addCityEconomicFact);

        log.info("Loaded economic facts for {} cities.", data.cities().size());
    }

    private void loadStateMigrationFlows() throws Exception {
        StateMigrationFlowsData data = readOptional(
                "state migration flows",
                StateMigrationFlowsData.class,
                "data/state_migration_flows.json");

        if (data == null || data.states() == null || data.states().isEmpty()) {
            log.warn("State migration flows were not loaded; moving-pair sitemap uses fallback selection.");
            return;
        }

        data.states().forEach(repository::addStateMigrationFlow);
        log.info("Loaded migration flows for {} destination states.", data.states().size());
    }
}
