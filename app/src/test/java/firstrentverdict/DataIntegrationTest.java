package firstrentverdict;

import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.model.dtos.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataIntegrationTest {

    @Autowired
    private VerdictDataRepository repository;

    @Test
    void verifyDataLoading_NewYork() {
        // Rent
        Optional<RentData.CityRent> rent = repository.getRent("New York", "NY");
        assertTrue(rent.isPresent(), "Rent data for NY should exist");
        assertEquals(4200, rent.get().median());

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
}
