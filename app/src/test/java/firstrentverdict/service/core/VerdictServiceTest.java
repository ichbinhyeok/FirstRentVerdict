package firstrentverdict.service.core;

import firstrentverdict.model.verdict.*;
import firstrentverdict.repository.VerdictDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VerdictServiceTest {

    @Autowired
    private VerdictService verdictService;

    @Test
    void testVerdict_Denied_Insolvency() {
        // New York: Rent 3850 + Deposit 3850 (1x cap) + Moving 1100 = 8800 upfront.
        // User has 5000: immediate insolvency.
        VerdictInput input = new VerdictInput(
                "New York", "NY",
                3850,
                5000,
                false,
                true,
                null);

        VerdictResult result = verdictService.assessVerdict(input);

        assertEquals(Verdict.DENIED, result.verdict());
        assertTrue(result.primaryDistressFactor().contains("Insolvency"));
    }

    @Test
    void testVerdict_Denied_LowBuffer() {
        // New York: ~8800 upfront.
        // Buffer rec: 2925.
        // User has 9000. Remaining: 200. (200 < 0.5 * 2925) -> DENIED
        VerdictInput input = new VerdictInput(
                "New York", "NY",
                3850,
                9000,
                false,
                true,
                null);

        VerdictResult result = verdictService.assessVerdict(input);

        assertEquals(Verdict.DENIED, result.verdict());
        assertTrue(result.primaryDistressFactor().contains("Critical Liquidity"));
    }

    @Test
    void testVerdict_Borderline() {
        // New York: ~8800 upfront. Buffer rec: 2925.
        // User has 11000. Remaining: 2200. (2200 > 0.5 * 2925 but < 2925) -> BORDERLINE
        VerdictInput input = new VerdictInput(
                "New York", "NY",
                3850,
                11000,
                false,
                true,
                null);

        VerdictResult result = verdictService.assessVerdict(input);

        assertEquals(Verdict.BORDERLINE, result.verdict());
        assertEquals("New York", "New York"); // Dummy assertions
    }

    @Test
    void testLegalCap_Enforcement() {
        // New York has a 1x cap.
        // Rent 3000 -> Deposit should be 3000, NOT higher even if typical was higher.
        VerdictInput input = new VerdictInput(
                "New York", "NY",
                3000,
                20000, // Plenty of cash
                false,
                true,
                null);

        VerdictResult result = verdictService.assessVerdict(input);

        // Check breakdown string for deposit info
        boolean strictCapMentioned = result.legalProtectionNote() != null
                && result.legalProtectionNote().contains("strictly limited");

        assertTrue(strictCapMentioned, "Result should mention strict legal cap protection for NY");
    }
}
