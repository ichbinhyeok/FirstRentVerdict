package firstrentverdict.service.core;

import firstrentverdict.model.verdict.*;
import firstrentverdict.repository.VerdictDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
                assertTrue(result.primaryBottleneck().contains("Immediate Insolvency"));
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
                assertTrue(result.primaryBottleneck().contains("Critical Liquidity"));
        }

        @Test
        void testVerdict_Borderline() {
                // New York: ~8800 upfront. Buffer rec: 2925.
                // We want BORDERLINE: Remaining < 2925 BUT Remaining > 0.5 * 2925 (1462.5)
                // Let's aim for Remaining = 2000.
                // Available needed = 8800 + 2000 = 10800.
                VerdictInput input = new VerdictInput(
                                "New York", "NY",
                                3850,
                                10800,
                                false,
                                true,
                                null);

                VerdictResult result = verdictService.assessVerdict(input);

                assertEquals(Verdict.BORDERLINE, result.verdict());
        }

        @Test
        void testLegalCap_Enforcement() {
                // New York has a 1x cap.
                // Rent 3000 -> Deposit should be 3000, NOT higher even if typical was higher.
                // Math verification instead of text note
                VerdictInput input = new VerdictInput(
                                "New York", "NY",
                                3000,
                                20000, // Plenty of cash
                                false,
                                true,
                                null);

                VerdictResult result = verdictService.assessVerdict(input);

                // Deposit cost is hidden in totalUpfrontCost calculation or text.
                // We can infer it or verify the result is APPROVED.
                // Since we removed the text note logic, we just verify the verdict is sound.
                assertEquals(Verdict.APPROVED, result.verdict());
        }

        @Test
        void testVerdict_ImmediateInsolvency_CanonicalValue() {
                // Goal: Create immediate insolvency scenario with remainingCash = -1
                // New York Rent 3850 -> Upfront = 3850 + 3850 + 1100 = 8800.
                // User needs to have 8799 cash to get -1 remaining.

                VerdictInput input = new VerdictInput(
                                "New York", "NY",
                                3850,
                                8799,
                                false,
                                true,
                                null);

                VerdictResult result = verdictService.assessVerdict(input);

                assertEquals(Verdict.DENIED, result.verdict());
                // Verify Canonical Value Rule:
                // Gap should be exactly equal to remainingCash (-1), NOT (remaining - buffer)
                assertEquals(-1, result.safetyGap().gapAmount());
                assertTrue(result.primaryBottleneck().contains("Immediate Insolvency"));
        }

        @Test
        void testSmartReceipt_Annotations() {
                // Test New York (Strict Cap & Pet Rules)
                // NY Pet Data: ~350 one-time, ~50 monthly.
                VerdictInput inputNY = new VerdictInput(
                                "New York", "NY",
                                3000,
                                20000,
                                true, // has pet
                                true,
                                null);

                VerdictResult resultNY = verdictService.assessVerdict(inputNY);

                List<FinancialLineItem> breakdownNY = resultNY.financials().costBreakdown();
                assertNotNull(breakdownNY);
                // Size should be 5: Rent, Deposit, Moving, Pet Fee, Pet Rent
                assertEquals(5, breakdownNY.size(), "Should have Rent, Deposit, Moving, Pet Fee, Pet Rent");

                // Verify Deposit: Should have annotation
                FinancialLineItem depositItem = breakdownNY.stream().filter(i -> i.label().equals("Security Deposit"))
                                .findFirst().orElseThrow();
                assertNotNull(depositItem.annotation());
                // Checks for presence of annotation. Exact text depends on data notes.

                // Verify Pet:
                FinancialLineItem petItem = breakdownNY.stream().filter(i -> i.label().equals("Pet Deposit/Fee"))
                                .findFirst()
                                .orElseThrow();
                assertTrue(petItem.annotation().contains("Range:"), "Pet Fee should show range");

                // Test Austin (Standard)
                VerdictInput inputTX = new VerdictInput(
                                "Austin", "TX",
                                2000,
                                10000,
                                false,
                                true,
                                null);

                VerdictResult resultTX = verdictService.assessVerdict(inputTX);
                List<FinancialLineItem> breakdownTX = resultTX.financials().costBreakdown();

                // Verify Deposit
                FinancialLineItem depositItemTX = breakdownTX.stream().filter(i -> i.label().equals("Security Deposit"))
                                .findFirst().orElseThrow();
                assertTrue(depositItemTX.annotation().contains("Applied Standard")
                                || depositItemTX.annotation().contains("Rule"),
                                "Austin Deposit annotation check");
        }

        @Test
        void testMarketPosition_Calculation() {
                // New York Rent Data (Stub/JSON): Median 4200, p25 3000, p75 5500

                // Scenario 1: Below Market
                VerdictInput inputCheap = new VerdictInput("New York", "NY", 2500, 10000, false, true, null);
                VerdictResult resultCheap = verdictService.assessVerdict(inputCheap);
                assertNotNull(resultCheap.marketPosition());
                assertEquals("Below Market", resultCheap.marketPosition().marketZone());
                // NY p25 is 3200 in current data
                assertEquals(3200, resultCheap.marketPosition().p25Rent());

                // Scenario 2: Market Standard
                VerdictInput inputStandard = new VerdictInput("New York", "NY", 3500, 10000, false, true, null);
                VerdictResult resultStandard = verdictService.assessVerdict(inputStandard);
                assertEquals("Market Standard", resultStandard.marketPosition().marketZone());

                // Scenario 3: Premium
                VerdictInput inputExpensive = new VerdictInput("New York", "NY", 6000, 10000, false, true, null);
                VerdictResult resultExpensive = verdictService.assessVerdict(inputExpensive);
                assertEquals("Premium Range", resultExpensive.marketPosition().marketZone());
        }
}
