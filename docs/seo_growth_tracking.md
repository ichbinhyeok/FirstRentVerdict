# SEO Growth Tracking (FirstRentVerdict)

## Current Thesis

- `bad credit + no cosigner approval` as the primary SEO wedge is retired as of `2026-04-24`.
- The original data asset remains useful, but the V2 SEO/content framing is not the main growth path.
- V3 moves from information-style SEO surfaces to programmatic tool surfaces:
  - `Should I Apply? Apartment Risk Checker`
  - `application fee waste risk`
  - `move-in cash gap before applying`
- City pages and research pages must support tool entry, not behave like broad rent-market content.

## 2026-03-22 Baseline

- Search Console live readback confirmed that broad `average rent` / `rental market` city SEO should not be the main bet.
- The product/message was repositioned around:
  - `Can you get approved, and how much cash do you need to move in?`
- Strategic surfaces promoted:
  - `/RentVerdict/`
  - `/RentVerdict/guides`
  - `/RentVerdict/research/move-in-cost-index`
  - `/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment`
  - `/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit`
  - `/RentVerdict/guides/pet-deposit-and-pet-rent-negotiation-guide`
  - `/RentVerdict/guides/rent-with-bad-credit-no-cosigner`
- IA update that shipped:
  - `Approval Check`
  - `Move-In Cash`
  - `Get Approved`
  - `City Signals`
- Result-page UX update that shipped:
  - `Core Question`
  - `Adaptive Action Plan`
  - simulator-linked action plan updates
  - `Copy My Plan`
- Verification on 2026-03-22:
  - `./gradlew test` passed after the first pivot pass

## 2026-03-22 Manual Index Queue

Submit only the new strategic surfaces first.

1. `https://movecostinfo.com/RentVerdict/research/move-in-cost-index`
2. `https://movecostinfo.com/RentVerdict/`
3. `https://movecostinfo.com/RentVerdict/guides`
4. `https://movecostinfo.com/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment`
5. `https://movecostinfo.com/RentVerdict/guides/rent-with-bad-credit-no-cosigner`
6. `https://movecostinfo.com/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit`

Do not manually submit legacy `first-month-cost` URLs. Let the 301s consolidate and re-check those around `2026-04-05`.

## 2026-04-01 Regular Check

- Compared against the 2026-03-22 baseline, the last 28 days got weaker overall.
  - `2026-03-04 ~ 2026-03-31`: `2 clicks / 116 impressions / CTR 1.72% / avg position 19.64`
  - prior 28 days `2026-02-04 ~ 2026-03-03`: `3 clicks / 231 impressions / CTR 1.30% / avg position 5.61`
- Short-term signal is mixed, not dead.
  - `2026-03-25 ~ 2026-03-31`: `0 clicks / 29 impressions / avg position 4.41`
  - previous 7 days `2026-03-18 ~ 2026-03-24`: `1 click / 17 impressions / avg position 12.29`
  - interpretation: visibility quality may be stabilizing on a tiny sample, but click volume is still too small to trust.
- US-only footprint is still the main bottleneck.
  - `usa`: `2 clicks / 76 impressions / avg position 25.91`
  - page/query footprint is still concentrated in old city-market or legacy-style URLs:
    - `/RentVerdict/verdict/st-petersburg-fl`
    - `/RentVerdict/first-month-cost/1400/ks`
    - `/RentVerdict/verdict/salary-needed/pittsburgh-pa`
  - interpretation: the approval-first pivot has not propagated into GSC query-page matches yet.
- Indexation check on priority surfaces:
  - research hub: PASS, submitted and indexed, last crawl `2026-03-31T19:39:14Z`
  - cash guide: PASS, submitted and indexed, last crawl `2026-03-24T05:19:20Z`, FAQ rich results PASS
  - bad-credit guide: `URL is unknown to Google`
  - legacy `first-month-cost/1000/dc`: still PASS + indexed with last crawl `2026-02-10T19:24:16Z`
- Sitemap report still does not look clean.
  - `https://movecostinfo.com/sitemap.xml`
  - submitted `204`
  - indexed `0`
  - last submitted/downloaded `2026-03-22`
- No striking-distance keywords were found on `2026-04-01`.

## 2026-04-01 Verdict

- The project is not dead, but the first pivot is not validated yet.
- What worked:
  - research hub remains indexed and recrawled
  - cash guide entered the index and rich results
  - 7-day average position improved on a very small sample
- What did not work yet:
  - bad-credit guide is still undiscovered
  - US queries are still mapped to old city-market or legacy URLs
  - legacy `first-month-cost` still survives in the index
- Immediate next actions from the check:
  1. manually inspect and request indexing again for the bad-credit guide
  2. keep the manual index queue limited to the new strategic surfaces
  3. re-check legacy `first-month-cost` around `2026-04-05`
  4. if `2026-04-19` still shows no US query expansion into the new guides, treat the first pivot as unvalidated and cut SEO scope again

## 2026-04-01 Follow-up Build

- Strengthened internal-link concentration toward the approval-first cluster instead of city-style exploration.
- Updated city-page internal links so the strongest reusable routes now point to:
  - `/RentVerdict/guides/rent-with-bad-credit-no-cosigner`
  - `/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment`
  - `/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit`
- Added a `Start Here` block on the guides hub to force the intended sequence:
  - bad-credit playbook
  - guarantor vs deposit
  - calculator
- Added an approval-first cross-link module to guide article pages so every guide reinforces the same cluster.
- Added direct `bad-credit` and `guarantor` CTA links to:
  - credit landing pages
  - research hub
- Build verification:
  - `./gradlew test` initially hit a Gradle configuration-cache classloading issue
  - `./gradlew test --no-configuration-cache` passed on `2026-04-01`

## 2026-04-01 Second Pivot Pass

This pass makes the wedge narrower than the 2026-03-22 version.

- Home page narrowed from broad `approval + move-in cash` language to:
  - `bad credit + no cosigner approval`
- Home page changes:
  - hero headline now leads with the bad-credit / no-cosigner question
  - added a `Start Here` rescue strip
  - added direct CTA to manual route support
  - moved research lower as supporting evidence
- Result page changes:
  - weak-credit scenarios now use the narrower core question:
    - `Can you still get approved with bad credit and no cosigner?`
  - added direct `Request Manual Route` CTA in the persona card
  - added direct `Request Manual Route` CTA in the adaptive action-plan header
- Contact page changes:
  - repurposed from generic contact page to `Manual Approval Support`
  - added a prefilled support email flow
  - explicitly asks for city, rent, cash, credit tier, cosigner, pet, and move timeline
  - keeps data-correction and partnership routes as secondary paths
- Strategic meaning:
  - the site is no longer positioned as a general rent-information destination
  - it is now closer to an `apartment approval rescue tool`

## Operating Rule

- Do not expand city SEO right now.
- Do not create new broad rent-market content right now.
- Keep shipping only pages and CTAs that reinforce:
  - weak credit
  - no cosigner
  - guarantor vs deposit
  - move-in cash

## 2026-04-01 Deployment State

- Current state after today's work:
  - deploy this narrower approval-rescue version
  - manually request indexing for the bad-credit guide again
  - observe, do not widen scope
- Interpretation:
  - this is now a short observation window, not another large rewrite window
  - only small corrective edits should happen before the `2026-04-19` decision point

## 2026-04-12 Regular Check

- Compared against the prior 28-day window, the site lost volume but improved ranking quality.
  - `2026-03-16 ~ 2026-04-12`: `1 click / 182 impressions / avg position 7.70`
  - `2026-02-16 ~ 2026-03-15`: `4 clicks / 283 impressions / avg position 8.98`
- Recent 7-day signal improved on position, not on clicks.
  - `2026-04-06 ~ 2026-04-12`: `0 clicks / 67 impressions / avg position 2.09`
  - `2026-03-30 ~ 2026-04-05`: `0 clicks / 61 impressions / avg position 8.75`
- Main interpretation:
  - the pivot is now indexed
  - the click problem is not just ranking anymore
  - the next bottleneck is CTR, snippet fit, and offer clarity
- Key page movement:
  - home: `23 impressions / position 1.83`
  - guides hub: `31 impressions / position 2.61`
  - cash guide: `21 impressions / position 4.24`
  - bad-credit guide: `24 impressions / position 3.96`
  - only click in the 28-day window came from a scenario page:
    - `/RentVerdict/verdict/can-i-move-with/5000/to/philadelphia-pa`
- US-only read:
  - `1 click / 78 impressions / avg position 12.37`
  - visible US query footprint is still thin
  - confirmed sample:
    - `st petersburg rental market` -> `4 impressions / position 71.5`
    - `average deposit for apartment rentals in seattle` -> `1 impression / position 1`
- Indexation check:
  - research hub: PASS, last crawl `2026-04-12T00:03:44Z`
  - cash guide: PASS, last crawl `2026-04-03T11:22:02Z`, FAQ PASS
  - bad-credit guide: PASS, last crawl `2026-04-03T11:20:02Z`, FAQ PASS
  - legacy `first-month-cost/1000/dc`: still indexed, last crawl `2026-02-14T10:58:10Z`
- Sitemap report remains suspicious:
  - `https://movecostinfo.com/sitemap.xml`
  - submitted `87`
  - indexed `0`
  - last submitted/downloaded `2026-04-01`

## 2026-04-12 Verdict

- The pivot is working at the indexing and visibility layer.
- The pivot is not yet working at the click or demand-capture layer.
- Practical interpretation:
  - do not expand scope
  - do not publish new broad pages
  - compress harder around one money page and one scenario promise

## 2026-04-12 Focused Build

- Chosen money page:
  - `/RentVerdict/guides/rent-with-bad-credit-no-cosigner`
- Changes shipped on 2026-04-12:
  - tightened the title and excerpt to sound more like a direct approval page
  - changed the primary CTA from calculator-first to manual-route-first
  - added a dedicated manual approval support block above the article body
  - made the guides hub CTA more explicit:
    - `Get Approved With Bad Credit`
    - `Request Manual Route`
- Goal of this pass:
  - test whether a more transactional bad-credit promise can convert impressions into actual clicks or support requests before `2026-04-19`

## Next Check Dates

- `2026-04-05`
  - inspect legacy `first-month-cost` again
  - inspect sitemap report again
- `2026-04-12`
  - check whether US query/page matches begin to include the bad-credit guide
- `2026-04-19`
  - make the go/no-go decision on SEO scope

## Go / No-Go Criteria for 2026-04-19

- Positive:
  - the bad-credit guide is indexed
  - at least one new guide begins showing up in US query-page matches
  - clicks and impressions stop concentrating only on legacy or city-style pages
- Negative:
  - bad-credit guide still undiscovered
  - US footprint still dominated by legacy or city-market pages
  - no real query expansion into the new approval cluster

If the negative case holds on `2026-04-19`, reduce SEO scope again and treat SEO as a background channel rather than the primary growth engine.

## 2026-04-24 V2 Retirement Decision

V2 thesis retired:

- `bad credit + no cosigner approval`
- support wedge: `move-in cash`
- operating shape: guide/content pages plus calculator CTAs

Search Console readback for `sc-domain:movecostinfo.com`:

- recent 28 days `2026-03-27 ~ 2026-04-23`:
  - `0 clicks / 217 impressions / CTR 0% / avg position 7.71`
- prior 28 days `2026-02-27 ~ 2026-03-26`:
  - `5 clicks / 117 impressions / CTR 4.27% / avg position 22.21`
- focused build window after `2026-04-12`:
  - `2026-04-13 ~ 2026-04-23`: `0 clicks / 52 impressions / avg position 14.85`
  - prior window `2026-04-02 ~ 2026-04-12`: `0 clicks / 101 impressions / avg position 5.05`
- chosen money page:
  - `/RentVerdict/guides/rent-with-bad-credit-no-cosigner`
  - recent 28 days: `0 clicks / 24 impressions / avg position 3.96`
  - those impressions were concentrated on `2026-04-09 ~ 2026-04-10`
  - `2026-04-13 ~ 2026-04-23`: `0 impressions`
- explicit query filters returned no meaningful footprint:
  - `bad credit`: `0 impressions`
  - `cosigner`: `0 impressions`
  - `guarantor`: `0 impressions`
  - `approval`: `0 impressions`
  - `cash`: `0 impressions`
- US-only strategic surfaces did not show meaningful query/page expansion into the new guides.

Why V2 failed:

- The wedge became clearer, but the exposure surface became too narrow.
- The pages that expanded exposure were mostly information-style guides, so Google could treat the site as a rent-information site rather than a tool.
- The surfaced demand remained scattered across city, rent, pet, and legacy verdict URLs instead of the approval-rescue cluster.
- The product ended at advice and contact intent, not a strong pre-application decision event.
- The first bottleneck remained acquisition, not calculation accuracy.

What this does not mean:

- The original decision-engine problem is not dead.
- The local data asset is not dead.
- The tool thesis is not dead.
- The failed assumption was that guide/content SEO could validate the wedge and pull demand into the tool.

Locked learning:

- Do not expand generic city SEO.
- Do not publish more broad rent-market guides.
- Do not treat `bad credit + no cosigner` as the main SEO wedge unless a separate paid/direct-channel test proves demand first.
- Keep SEO as a background channel until tool-surface demand is proven.

## V3 Direction

V3 thesis:

- A renter does not primarily want another guide.
- A renter wants to know whether they should apply before they waste an application fee, deposit, admin fee, or moving cash.
- The original move-in cash pressure engine should be reframed as a pre-application risk checker.

V3 product promise:

- `Should I apply for this apartment, or am I about to waste money?`

V3 primary surface:

- `Should I Apply? Apartment Risk Checker`

Core verdict language:

- `Apply`
- `Pause`
- `Do Not Apply`

Core risk modules:

- application fee waste risk
- move-in cash gap
- deposit pressure
- income-to-rent risk
- pet fee pressure
- post-move buffer risk
- questions to ask before applying

V3 wedge:

- avoid wasting application fees and move-in cash before applying

V3 exposure rule:

- broad exposure is allowed only through tool-first pages.
- Every strategic page must begin with a calculator, checker, prefilled scenario, or result-style surface.
- Supporting explanation can exist below the tool, but the page must not read as a generic article first.

Example V3 tool surfaces:

- `/RentVerdict/should-i-apply`
- `/RentVerdict/application-fee-risk-checker`
- `/RentVerdict/move-in-cash-gap-calculator`
- `/RentVerdict/security-deposit-calculator`
- `/RentVerdict/pet-fee-move-in-cost-calculator`
- `/RentVerdict/can-i-move-with/5000/to/miami-fl`
- `/RentVerdict/city/miami-fl/move-in-cost-calculator`

Next operating rule:

- Before redesigning large parts of the product, verify and enrich the data needed for the V3 risk checker.
- The next build should reduce information-style pages and increase tool-surface clarity.
