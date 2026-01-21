package firstrentverdict.service.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstrentverdict.model.dtos.*;
import firstrentverdict.repository.VerdictDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataLoadingService {

    private final VerdictDataRepository repository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public DataLoadingService(VerdictDataRepository repository, ResourceLoader resourceLoader,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadData() throws IOException {
        System.out.println("Starting Data Loading...");
        loadCities();
        loadRent();
        loadCashBuffer();
        loadMoving();
        loadPet();
        loadSecurityDeposit();
        System.out.println("Data Loading Completed Successfully.");
    }

    private void loadCities() throws IOException {
        CitiesData data = loadJson("cities.json", CitiesData.class);
        data.cities().forEach(repository::addCity);
        System.out.println("Loaded " + data.cities().size() + " cities.");
    }

    private void loadRent() throws IOException {
        RentData data = loadJson("rent_1br.json", RentData.class);
        data.cities().forEach(repository::addRent);
        System.out.println("Loaded Rent data for " + data.cities().size() + " cities.");
    }

    private void loadCashBuffer() throws IOException {
        CashBufferData data = loadJson("cash_buffer.json", CashBufferData.class);
        data.cities().forEach(repository::addCashBuffer);
        System.out.println("Loaded Cash Buffer data for " + data.cities().size() + " cities.");
    }

    private void loadMoving() throws IOException {
        MovingData data = loadJson("moving_cost.json", MovingData.class);
        data.cities().forEach(repository::addMoving);
        System.out.println("Loaded Moving Cost data for " + data.cities().size() + " cities.");
    }

    private void loadPet() throws IOException {
        PetData data = loadJson("pet_cost.json", PetData.class);
        data.cities().forEach(repository::addPet);
        System.out.println("Loaded Pet Cost data for " + data.cities().size() + " cities.");
    }

    private void loadSecurityDeposit() throws IOException {
        SecurityDepositRaw raw = loadJson("security_deposit.json", SecurityDepositRaw.class);

        // Index State Laws
        Map<String, SecurityDepositRaw.StateLaw> stateLawMap = new HashMap<>();
        if (raw.stateLaws() != null) {
            for (SecurityDepositRaw.StateLaw law : raw.stateLaws()) {
                stateLawMap.put(law.state(), law);
            }
        }

        // Merge and Add Cities
        if (raw.cities() != null) {
            for (SecurityDepositRaw.CityDeposit city : raw.cities()) {
                SecurityDepositRaw.StateLaw law = stateLawMap.get(city.state());
                Double legalCap = (law != null) ? law.legalCapMultiplier() : null;

                SecurityDepositData combined = new SecurityDepositData(
                        city.city(),
                        city.state(),
                        legalCap,
                        city.cityPractice().typicalMultipliers(),
                        city.cityPractice().highRiskMultipliers(),
                        city.cityPractice().notes());
                repository.addSecurityDeposit(combined);
            }
            System.out.println("Loaded Security Deposit data for " + raw.cities().size() + " cities.");
        }
    }

    private <T> T loadJson(String filename, Class<T> clazz) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/data/" + filename);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, clazz);
        }
    }
}
