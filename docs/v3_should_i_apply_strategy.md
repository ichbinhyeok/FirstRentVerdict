# V3 Strategy: Should I Apply?

Decision date: `2026-04-24`

## Strategic Lock

V2 is retired:

- `bad credit + no cosigner approval`
- guide-led SEO
- manual approval support as the primary promise

V3 is now the working thesis:

- `Should I Apply? Apartment Risk Checker`
- a tool-first pre-application decision engine
- a renter checks whether applying would waste money, drain move-in cash, or create an unreasonable entry-risk scenario

## Why V2 Is Retired

The V2 problem was not implementation quality. The problem was channel and surface mismatch.

Observed failures:

- GSC showed `0 clicks / 217 impressions` for `2026-03-27 ~ 2026-04-23`.
- The chosen V2 money page, `/RentVerdict/guides/rent-with-bad-credit-no-cosigner`, had no sustained post-build demand.
- Core query filters such as `bad credit`, `cosigner`, `guarantor`, `approval`, and `cash` did not produce meaningful impressions.
- Google saw many surfaces as information pages, not tool pages.
- The site widened into guides, city signals, pet fees, guarantor explanations, and move-in cash without a single dominant tool action.

Conclusion:

- The V2 SEO wedge failed.
- The original data asset did not fail.
- The tool thesis did not fail.
- The next experiment must validate tool-led acquisition, not guide-led acquisition.

## V3 Product Frame

Primary user question:

> Should I apply for this apartment, or am I about to waste money?

Primary customer:

- a renter who has found a specific listing
- is at or near the application/payment step
- has limited tolerance for wasted application fees, admin fees, holding deposits, surprise deposits, or failed screening
- needs a decision before money leaves their account

This is narrower than:

- general movers
- generic rent-affordability researchers
- broad city-cost browsers
- long-term credit repair users

Value promise:

> First Rent Verdict helps renters decide whether to apply before paying non-refundable apartment fees by checking cash gap, fee risk, deposit pressure, and screening assumptions in one decision.

Operational product sentence:

> Apartment application risk checking before payment.

Primary product output:

- `Apply`
- `Pause`
- `Do Not Apply`

The product should explain the verdict through:

- application fee waste risk
- move-in cash gap
- deposit pressure
- pet fee pressure
- income-to-rent pressure
- post-move buffer risk
- specific questions to ask before applying

## Why This Is Different From V1

V1 asked:

- `How much cash do I need to move in?`

V3 asks:

- `Should I pay to apply for this apartment?`

The underlying data can overlap, but the user motivation is sharper.

V1 was a move-in affordability verdict.

V3 is a pre-application loss-prevention tool.

The difference matters because renters may not search for abstract affordability. They may react more strongly to avoiding wasted application fees, surprise deposits, and failed move-in attempts.

## Exposure Rule

V3 may have broad programmatic surfaces, but every surface must be tool-first.

Allowed:

- calculators
- checkers
- risk scores
- scenario pages
- prefilled city/rent/cash examples
- state-specific fee/deposit rule tools

Not allowed as primary surfaces:

- generic rent-market guides
- generic city SEO pages
- broad educational articles
- content hubs that do not begin with an interactive or precomputed decision

## Initial Tool Surfaces

Primary:

- `/RentVerdict/should-i-apply`

Supporting:

- `/RentVerdict/application-fee-risk-checker`
- `/RentVerdict/move-in-cash-gap-calculator`
- `/RentVerdict/security-deposit-calculator`
- `/RentVerdict/pet-fee-move-in-cost-calculator`
- `/RentVerdict/can-i-move-with/{cash}/to/{city-state}`
- `/RentVerdict/city/{city-state}/move-in-cost-calculator`

## 2026-04-25 Implementation Lock

Implemented the narrow-wedge / broad-surface pattern:

- core service: `ShouldIApplyService`
- core route: `/RentVerdict/should-i-apply`
- result route: form POST to `/RentVerdict/should-i-apply`
- result language: `Apply`, `Pause`, `Do Not Apply`
- root page reframed from V2 approval-rescue copy to V3 pre-application risk checking
- header navigation changed from guide-led surfaces to tool-led surfaces

Implemented broad tool-first entry surfaces:

- `/RentVerdict/application-fee-risk-checker`
- `/RentVerdict/move-in-cash-gap-calculator`
- `/RentVerdict/security-deposit-calculator`
- `/RentVerdict/pet-fee-move-in-cost-calculator`
- `/RentVerdict/should-i-apply-in/{city-state}`
- `/RentVerdict/city/{city-state}/move-in-cost-calculator`
- `/RentVerdict/can-i-apply-with/{cash}/for/{rent}/in/{city-state}`
- `/RentVerdict/can-i-move-with/{cash}/to/{city-state}`

The pages intentionally reuse the same checker. This keeps the wedge narrow while allowing wide indexable demand surfaces.

Sitemap behavior:

- static V3 tool pages are included
- selected city V3 tool pages are included
- selected cash/rent scenario pages are included
- existing sitemap limit remains controlled through configured city selection

Test coverage added:

- V3 tool pages load
- city and cash/rent scenario URLs prefill the checker
- V3 POST returns a noindex result page
- V3 data rules load into the repository
- sitemap contains the new V3 tool surfaces

## 2026-04-25 Engine And Design Tightening

The first V3 implementation used a simple weighted risk score. That is retired.

Current decision engine:

- `Do Not Apply` is produced by hard gate failure:
  - move-in cash gap
  - income below selected screening assumption
  - application fee above loaded fixed cap
  - deposit/prepaid rent above loaded cap
- `Pause` is produced by confirmation gate failure:
  - income unknown
  - post-move buffer below target
  - application fee rule unknown
  - deposit rule unknown
  - cost-only fee needs itemization
  - high pre-approval cash at risk
- `Apply` is produced only when all loaded cash, income, fee, and deposit gates clear.

This matches the product mission better than weights because a renter should not need a hidden score explanation. A failed gate maps directly to an action before payment.

JTE UI direction:

- JTE does not provide a shadcn-style component library.
- The project now uses internal JTE components under `app/src/main/jte/components` for V3 primitives:
  - `apply_panel.jte`
  - `apply_money_field.jte`
  - `apply_metric.jte`
  - `apply_cost_row.jte`
  - `apply_risk_row.jte`
- V3 checker/result pages were redesigned as an application risk console rather than legacy guide/hero pages.

## 2026-04-25 Customer Value Lock

The current best wedge is confirmed as:

- customer: renters immediately before paying to apply
- event: application checkout / leasing-office payment request
- job: avoid wasting money or trapping cash in a bad application
- artifact: gate-based application decision, not a guide

Why this fits current leverage:

- existing city rent, deposit, moving, pet, cash-buffer, and fee/deposit seed data map directly into a pre-payment decision
- no apartment supply acquisition is required
- the failed V2 SEO motion taught that generic guide pages made the site look informational instead of tool-first
- a renter near application payment has higher intent than a broad renter researching affordability

Implementation response:

- added a live cash-stack preview to the checker inspector
- the preview updates while the renter types:
  - cash after listing stack
  - listing stack
  - income needed
  - cash exposed before approval
- the preview is advisory; final output remains the server-side gate engine.

## 2026-04-25 Product-Grade Output Lock

The result page must not feel like a math receipt.

A renter does not pay for arithmetic. The product value is the decision and the path:

- what to do before paying
- the first blocker
- what must change for this listing to become apply-safe
- what to ask the property in writing
- which fee/deposit/rent levers to test if they still want the listing

Implementation response:

- added `primaryAction` and `nextBestMove` to make the first screen actionable
- added `constraintLabel` and `approvalPath` so the user sees the real bottleneck, not only totals
- added `negotiationMoves` so the output recommends concrete levers such as reducing upfront cash, splitting deposit/prepaid rent, confirming refundability, or changing search rent
- kept the cost stack and rule ledger as supporting evidence, not the main product artifact

Monetization implication:

- the raw result should stay free because it builds trust and segments intent
- the stronger downstream CTA is not "buy this calculation"
- the stronger CTA is a lead/review action around the user's next listing decision

## Current Data Assets

Existing structured data:

- `application_fee_rules.json`: state-level application and screening fee seed rules
- `application_risk_vocabulary.json`: V3 tool labels and pre-application risk terms
- `rent_data.json`: city-level 1BR rent distribution
- `security_deposit.json`: city practice and partial state law caps
- `deposit_prepaid_rules.json`: state-level deposit and prepaid-rent seed rules
- `moving_data.json`: local moving cost estimates
- `pet_data.json`: one-time and monthly pet costs
- `cash_buffer.json`: post-move buffer estimates
- `city_economic_facts.json`: ACS income, renter share, rent burden, rent-to-income context
- `screening_income_assumptions.json`: common income-screening threshold assumptions
- `state_migration_flows.json`: IRS state migration corridors
- `city_insights.json`: qualitative city context

The current dataset is enough to power:

- application fee risk flags in seed states
- deposit/prepaid-rent risk flags in seed states
- assumption-based income-to-rent pressure checks
- move-in cash gap
- post-move buffer risk
- pet fee pressure
- city-level cash pressure ranking
- scenario pages such as `Can I move with $5,000 to Miami?`

The current dataset is enough to build the first narrow V3 checker.

It is not enough to power a fully national legal/fee verdict system.

## Data To Acquire Next

Required to expand V3:

1. Application fee rules
   - complete state caps
   - city caps where applicable
   - screening fee refund rules
   - source freshness

2. Deposit and prepaid-rent rules
   - complete 50-state cap coverage
   - small-landlord exceptions
   - pet deposit inclusion rules
   - prepaid rent restrictions

3. Non-rent application costs
   - admin fee
   - holding fee
   - move-in fee
   - broker fee regions
   - renters insurance
   - utility setup deposits

4. Property-policy inputs
   - user-entered income requirement
   - user-entered application/admin/holding fees
   - user-entered deposit and prepaid rent stack
   - user-entered stated denial criteria

Already seeded:

1. Application fee rules
   - first state-level caps and cost-only rules
   - first screening fee refund/disclosure rules
   - source freshness

2. Deposit and prepaid-rent rules
   - first state cap coverage
   - first small-landlord exception notes
   - first pet deposit inclusion notes
   - first prepaid rent restriction notes

3. Income requirement assumptions
   - common 2.5x / 3x / 3.5x rent screening thresholds
   - label as screening assumptions, not law
   - allow user override

4. Risk vocabulary
   - terms renters search and recognize before applying
   - examples: `application fee`, `admin fee`, `holding deposit`, `move-in fee`, `security deposit`, `income requirement`, `3x rent`

Optional later:

- locator or property-management policy data
- guarantor/deposit-alternative provider acceptance
- city/neighborhood inventory signals
- state or city tenant-rights summaries

## Data Integrity Rules

- Legal and fee rules must keep source URLs and effective dates where known.
- If a rule is legal-adjacent, output must avoid legal advice framing.
- Missing data stays missing.
- Screening thresholds must be labeled as common assumptions unless sourced to a specific property policy.
- The checker can say `unknown` or `ask the property` when data is insufficient.

## Success Criteria

The first V3 experiment should not be judged by total page count.

Judge it by:

- visits to tool-first pages
- completion rate of checker inputs
- result-page engagement
- clicks on `questions to ask before applying`
- email/share/save events
- GSC impressions for tool-intent queries
- whether Google starts surfacing calculator/checker pages rather than guide pages

Early no-go signal:

- pages index as articles/guides again
- impressions continue to cluster around broad city/rent-market queries
- checker pages get impressions but no input starts

## Build Bias

Build the smallest useful V3:

- one primary checker
- one result page
- 3 to 5 programmatic tool surfaces
- no new broad guide hub

The purpose is to validate a sharper user action:

> Check before you apply.

## 2026-04-25 pSEO Tool-State Expansion

Decision:

- Expand SEO surface with tool-state pages, not generic articles.
- Every indexed scenario should open the same V3 application-risk engine with meaningful prefilled values.
- Page families should map directly to high-intent searches and feed users back into the checker/result/refine flow.

Implemented first expansion:

- cash/rent scenario family already existed:
  - `/RentVerdict/can-i-apply-with/{cash}/for/{rent}/in/{city-slug}`
- application fee scenario:
  - `/RentVerdict/application-fee/{fee}/in/{city-slug}`
- admin fee scenario:
  - `/RentVerdict/admin-fee/{fee}/in/{city-slug}`
- holding deposit scenario:
  - `/RentVerdict/holding-deposit/{amount}/in/{city-slug}`
- income/rent screen scenario:
  - `/RentVerdict/can-i-apply-with/{income}/income-for/{rent}/rent-in/{city-slug}`
- first/last/security deposit stack:
  - `/RentVerdict/first-last-security-deposit-in/{city-slug}`

Sitemap:

- Added these families for the selected sitemap city set.
- The default city limit remains configurable through `seo.sitemap.city-limit`.
- The new pages are not separate informational articles; they are prefilled checker states.

Quality rule:

- Expand by family only when the page changes the actual tool state.
- Avoid pages that only swap city text without changing the calculation, risk framing, or next action.
- Use GSC by family: expand families with impressions and submits, prune or noindex families with no response.

Quality uplift added:

- scenario URLs now include a family-specific top panel
- state fee/deposit rule coverage is exposed as badges
- same-city related scenario links connect the pSEO family cluster
- this is meant to help Google and users read the pages as tool states, not thin informational copies
