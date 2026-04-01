# SEO Growth Tracking (FirstRentVerdict)

## Current Thesis

- Broad `city rent info` SEO is not the main growth path.
- The surviving wedge is:
  - `bad credit + no cosigner approval`
- The supporting wedge is:
  - `move-in cash`
- City pages and research pages now act as supporting evidence, not the main promise.

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
