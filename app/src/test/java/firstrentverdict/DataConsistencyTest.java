package firstrentverdict;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstrentverdict.model.dtos.*;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.data.JsonDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class DataConsistencyTest {

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
}
