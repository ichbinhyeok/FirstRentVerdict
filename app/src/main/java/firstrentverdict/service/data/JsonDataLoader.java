package firstrentverdict.service.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstrentverdict.model.dtos.*;
import firstrentverdict.repository.VerdictDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class JsonDataLoader implements CommandLineRunner {

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

        System.out.println("✅ Data Loading Complete. Total cities supported: " + repository.getAllCities().size());
    }

    private void loadCities() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/cities.json");
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                CitiesData data = objectMapper.readValue(is, CitiesData.class);
                data.cities().forEach(repository::addCity);
                System.out.println("Loaded " + data.cities().size() + " cities.");
            }
        } else {
            System.err.println("❌ cities.json not found!");
        }
    }

    private void loadRentData() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/rent_data.json");
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                RentData data = objectMapper.readValue(is, RentData.class);
                data.cities().forEach(repository::addRent);
                System.out.println("Loaded rent data for " + data.cities().size() + " cities.");
            }
        }
    }

    private void loadSecurityDepositData() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/security_deposit.json");
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                SecurityDepositWrapper wrapper = objectMapper.readValue(is, SecurityDepositWrapper.class);
                if (wrapper.cities() != null) {
                    wrapper.cities().forEach(repository::addSecurityDeposit);
                    System.out.println("Loaded security deposit data for " + wrapper.cities().size() + " cities.");
                }
            }
        }
    }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    private record SecurityDepositWrapper(List<SecurityDepositData> cities) {
    }

    private void loadMovingData() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/moving_data.json");
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                MovingData data = objectMapper.readValue(is, MovingData.class);
                data.cities().forEach(repository::addMoving);
                System.out.println("Loaded moving data for " + data.cities().size() + " cities.");
            }
        }
    }

    private void loadPetData() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/pet_data.json");
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                PetData data = objectMapper.readValue(is, PetData.class);
                data.cities().forEach(repository::addPet);
                System.out.println("Loaded pet data for " + data.cities().size() + " cities.");
            }
        }
    }

    private void loadCashBufferData() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/cash_buffer.json");
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                CashBufferData data = objectMapper.readValue(is, CashBufferData.class);
                data.cities().forEach(repository::addCashBuffer);
                System.out.println("Loaded cash buffer data for " + data.cities().size() + " cities.");
            }
        }
    }
}
