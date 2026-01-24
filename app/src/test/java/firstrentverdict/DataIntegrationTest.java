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
        // NY State Law Cap is 1.0 from state_laws in JSON
        assertEquals(Double.valueOf(1.0), deposit.get().legalCapMultiplier(), "NY Legal Cap should be 1.0");

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
        assertNull(deposit.get().legalCapMultiplier(), "TX Legal Cap should be null (no cap)");
    }
}
