# V3 Data Audit and Acquisition Plan

Audit date: `2026-04-24`

## Purpose

This document records the current data asset quality and the next data needed for the V3 product:

- `Should I Apply? Apartment Risk Checker`
- `Application fee waste risk`
- `Move-in cash gap before applying`

The goal is to avoid repeating the V2 failure mode where the product direction changed faster than the data and measurement strategy.

## Current Data Inventory

Existing files:

- `application_fee_rules.json`: 8 state-level application/screening fee seed rules
- `application_risk_vocabulary.json`: 16 V3 tool-label and risk-language terms
- `cash_buffer.json`: 100 city records
- `cities.json`: 100 city records
- `city_coordinates.json`: 109 city records
- `city_economic_facts.json`: 100 city records
- `city_insights.json`: 100 city records
- `deposit_prepaid_rules.json`: 12 state-level deposit/prepaid rent seed rules
- `moving_data.json`: 100 city records
- `pet_data.json`: 100 city records
- `rent_data.json`: 100 city records
- `screening_income_assumptions.json`: 3 income-screening assumption scenarios
- `security_deposit.json`: 100 city records
- `state_migration_flows.json`: 51 state records

## V3 Seed Data Added

Added on `2026-04-24`:

- `application_fee_rules.json`
  - states: `CA`, `NY`, `WA`, `OR`, `CO`, `VA`, `TX`, `MA`
  - covers application/screening fee caps, disclosure rules, refund triggers, reusable screening report notes, source URLs, source type, source citation, confidence, and `last_checked`
- `deposit_prepaid_rules.json`
  - states: `CA`, `NY`, `MA`, `TX`, `AZ`, `NC`, `FL`, `NV`, `VA`, `WA`, `OR`, `CO`
  - covers deposit cap type, cap formula, prepaid rent notes, return deadlines, interest notes, source URLs, confidence, and `last_checked`
- `screening_income_assumptions.json`
  - assumptions: `2.5x rent`, `3x rent`, `3.5x rent`
  - explicitly marks each threshold as `is_law: false`
- `application_risk_vocabulary.json`
  - terms for tool labels, result explanations, and tool-first SEO surfaces

Validation added in `DataConsistencyTest`:

- V3 legal-adjacent datasets must have fresh `last_checked` values on or after `2026-04-24`.
- Source-backed rules must have `source_url`, `source_type`, `source_citation`, `product_risk_note`, `confidence`, and per-rule `last_checked`.
- `source_url` must be a web URL.
- `confidence` must be one of `high`, `medium`, or `low`.
- Income screening assumptions must not be labeled as laws.
- Application-risk vocabulary must include user-facing tool prompts.

Runtime wiring added on `2026-04-25`:

- `JsonDataLoader` now loads V3 application fee rules, deposit/prepaid rules, income assumptions, and risk vocabulary.
- `VerdictDataRepository` exposes state-level lookup methods for V3 fee and deposit rules.
- `ShouldIApplyService` uses the V3 data to produce application fee flags, deposit/prepaid-rent flags, income assumption checks, cash gap checks, and ask-before-paying questions.
- `DataIntegrationTest` verifies that V3 rules load into the repository.

Engine audit update on `2026-04-25`:

- The V3 checker no longer uses a weighted risk score.
- It uses explicit gates:
  - hard blockers for cash gap, income miss, loaded fee-cap breach, loaded deposit/prepaid-rent cap breach
  - pause gates for missing/uncertain rule data, unknown income, weak post-move buffer, and high pre-approval cash at risk
- This keeps legal-adjacent data uncertainty visible instead of hiding it inside a numeric score.

## Structural Audit

City-key coverage:

- `cash_buffer.json`: 100 unique city/state keys, 0 missing vs base city list
- `city_economic_facts.json`: 100 unique city/state keys, 0 missing
- `city_insights.json`: 100 unique city/state keys, 0 missing
- `moving_data.json`: 100 unique city/state keys, 0 missing
- `pet_data.json`: 100 unique city/state keys, 0 missing
- `rent_data.json`: 100 unique city/state keys, 0 missing
- `security_deposit.json`: 100 unique city/state keys, 0 missing
- `city_coordinates.json`: 109 unique city/state keys, 9 extra cities outside the base list

Extra coordinate cities:

- `Port St. Lucie, FL`
- `Huntsville, AL`
- `Tacoma, WA`
- `San Bernardino, CA`
- `Hialeah, FL`
- `Frisco, TX`
- `Modesto, CA`
- `Cape Coral, FL`
- `Fontana, CA`

Range sanity checks:

- rent range order issues: `0`
- moving cost range order issues: `0`
- pet one-time fee range order issues: `0`
- pet monthly rent range order issues: `0`
- cash buffer formula issues: `0`
- economic percentage range issues: `0`

Year coverage:

- rent: `2025`
- moving: `2025`
- pet: `2025`
- cash buffer: `2025`
- ACS economic facts: `2023`

## Source Audit

Strongest source layer:

- `city_economic_facts.json`
  - 100 records
  - 200 source items
  - 200 URL source items
  - uses Census ACS API URLs

Weak or non-verifiable source layers:

- `rent_data.json`
  - 200 source labels
  - 0 URL source items
- `moving_data.json`
  - 200 source labels
  - 0 URL source items
- `pet_data.json`
  - 200 source labels
  - 0 URL source items
- `cash_buffer.json`
  - 300 source labels
  - 0 URL source items
- `security_deposit.json`
  - 100 source labels
  - 0 URL source items
- `city_insights.json`
  - no source field
- `city_coordinates.json`
  - no source field

Interpretation:

- The current dataset is usable for a prototype and for relative city-level estimates.
- It is not yet strong enough for high-trust legal-adjacent claims or public-facing fee-rule verdicts.
- V3 should show uncertainty when legal or fee-rule data is missing.

## Remaining Critical Gaps For V3

### 1. Application Fee Rules

Seed coverage now exists for the first 8 states, but this is not complete 50-state coverage.

Needed fields:

- `jurisdiction`
- `state`
- `city`
- `fee_type`
- `cap_type`
- `cap_amount`
- `cap_formula`
- `refund_required_when`
- `portable_screening_report_rule`
- `disclosure_required`
- `exceptions`
- `source_url`
- `effective_date`
- `last_checked`
- `confidence`

Seed source candidates:

- California Civil Code section 1950.6: `https://leginfo.legislature.ca.gov/faces/codes_displaySection.xhtml?lawCode=CIV&sectionNum=1950.6.`
- New York Real Property Law section 238-a: `https://law.justia.com/codes/new-york/rpp/article-7/238-a/`
- Washington RCW 59.18.257: `https://app.leg.wa.gov/rcw/default.aspx?cite=59.18.257`
- Oregon ORS 90.295: `https://www.oregonlegislature.gov/bills_laws/ors/ors090.html`
- Colorado HB19-1106 rental application fees: `https://leg.colorado.gov/bills/HB19-1106`
- Massachusetts apartment finder and broker fee guidance: `https://www.mass.gov/info-details/apartment-finders-fees-brokers-fees-and-signing-a-lease`

Initial source findings:

- California:
  - screening fee may cover actual out-of-pocket screening cost and reasonable processing time
  - statutory base cap is `$30`, CPI-adjusted annually
  - landlord/agent should not charge when no rental unit is available within a reasonable period
  - unused fee must be returned if no credit report or reference check is obtained
  - source: California Civil Code section 1950.6
- New York:
  - application processing fees are generally barred except background and credit check reimbursement
  - check fee cap is the lesser of actual cost or `$20`
  - fee must be waived if the applicant provides a recent background/credit check
  - source: New York Real Property Law section 238-a
- Washington:
  - landlord must disclose screening criteria and consumer reporting details before screening
  - screening charge is limited to actual/customary tenant screening costs after required disclosures
  - landlord websites must say whether comprehensive reusable tenant screening reports are accepted
  - source: RCW 59.18.257
- Oregon:
  - applicant screening charge is cost-only
  - only one applicant screening charge may be required within a 60-day period for that landlord
  - written screening criteria, fee amount, deposits, rent, insurance requirements, and availability context must be disclosed before accepting the charge
  - refund is required within 30 days in listed unscreened/withdrawn scenarios
  - source: ORS 90.295
- Colorado:
  - application fee must be used entirely for processing costs
  - landlord must disclose anticipated expenses or itemize actual expenses
  - unused portions should be refunded with good-faith effort within 20 days
  - source: HB19-1106
- Massachusetts:
  - broker/finder fee rules are important for V3 because Boston-area renters often face broker-fee confusion
  - use state guidance and Chapter 186 Section 15B before producing any fee-risk verdict for Massachusetts
  - source: Mass.gov apartment finder/broker fee guidance and M.G.L. Chapter 186 Section 15B

### 2. Complete Deposit And Prepaid Rent Rules

State-law coverage is still incomplete:

- `security_deposit.json` has city practice for 100 cities.
- It has explicit state-law records only for:
  - `NY`
  - `CA`
  - `IL`
  - `TX`
- `deposit_prepaid_rules.json` now adds 12 seed state rules, but this is not yet full 50-state coverage.

Needed fields:

- `state`
- `deposit_cap_multiplier`
- `deposit_cap_formula`
- `prepaid_rent_limit`
- `pet_deposit_included_in_cap`
- `small_landlord_exception`
- `furnished_exception`
- `return_deadline_days`
- `interest_required`
- `source_url`
- `effective_date`
- `last_checked`
- `confidence`

Seed source candidates:

- California AB 12 / Civil Code 1950.5: `https://leginfo.legislature.ca.gov/faces/billNavClient.xhtml?bill_id=202320240AB12`
- New York General Obligations Law section 7-108: `https://www.nysenate.gov/legislation/laws/GOB/7-108`
- Massachusetts General Laws chapter 186 section 15B: `https://malegislature.gov/Laws/GeneralLaws/PartII/TitleI/Chapter186/Section15b`
- Texas State Law Library security deposit guide: `https://guides.sll.texas.gov/landlord-tenant-law/security-deposits`

### 3. Income Requirement Assumptions

Seed assumptions now exist.

This should not be represented as law unless tied to a specific property policy.

Needed fields:

- `threshold_label`
- `rent_to_income_multiplier`
- `description`
- `use_case`
- `is_law`
- `source_url`
- `confidence`

Initial assumption set:

- `2.5x rent`
- `3x rent`
- `3.5x rent`

Product rule:

- Always let the user override this.
- Label it as a screening assumption.
- Do not imply that every landlord uses the same threshold.

### 4. Non-Rent Application And Move-In Fees

Partially missing today.

Needed categories:

- application fee
- admin fee
- holding fee
- move-in fee
- broker fee
- utility setup deposit
- renters insurance
- parking fee

This is important because V3's user motivation is not abstract affordability. It is avoiding cash loss before applying.

### 5. Search And UX Vocabulary

Seed vocabulary now exists as structured data.

Needed terms:

- `application fee`
- `admin fee`
- `holding deposit`
- `move-in fee`
- `security deposit`
- `first month's rent`
- `last month's rent`
- `3x rent`
- `income requirement`
- `tenant screening`
- `portable tenant screening report`

This vocabulary should drive tool labels, page titles, and result explanations.

## V3 Data Priority

Priority 1:

- application fee rules for top states in existing 100-city set
- complete deposit/prepaid-rent rules for top states
- income multiplier assumptions

Priority 2:

- broker fee and move-in fee rules in high-friction markets
- utility setup and renters insurance assumptions
- application fee vocabulary and user-facing labels

Priority 3:

- landlord policy data
- locator/provider acceptance data
- guarantor/deposit alternative provider acceptance

Do not block V3 on Priority 3. That becomes supply-side infrastructure, not the first product test.

## Product Integrity Rules

- V3 may produce `Apply`, `Pause`, or `Do Not Apply`.
- It must not produce legal advice.
- If fee-law data is missing, say `Unknown - ask the property before paying`.
- If source confidence is low, the result must not sound definitive.
- Existing qualitative `city_insights.json` should not drive verdicts unless sourced and verified.
- Data with source labels but no URLs can support prototype estimates but should not support strong legal-adjacent claims.

## Audit Conclusion

The original dataset is clean enough to power a city-level move-in risk engine.

The new V3 seed datasets are clean enough to start a narrow pre-application checker.

They are not yet complete enough to power a fully national legal/fee verdict system.

The next data work should focus on:

1. expanding application fee rules beyond the 8 seed states
2. expanding deposit and prepaid-rent caps beyond the 12 seed states
3. adding city-level broker/admin/move-in fee rules in high-friction markets
4. replacing weak source-label-only datasets with URL-backed sources where verdict confidence depends on them

This supports the V3 product promise:

> Check before you apply.
