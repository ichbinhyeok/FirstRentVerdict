# V3 Expert And User Review

Review date: `2026-04-25`

Scope:

- Product: `Should I Apply? Apartment Risk Checker`
- Method: 20 automated browser form-entry scenarios, followed by synthetic expert and user panel review.
- Constraint: This is not a real recruited external panel. It is a structured simulation using domain-specific reviewer personas and direct browser QA evidence.

## Browser Scenario Results

All scenarios were entered through the actual form at `/RentVerdict/should-i-apply`, not by direct service calls.

Technical result:

- 20 / 20 scenarios submitted successfully
- console errors: `0`
- horizontal overflow: `0`
- result page rendered the decision brief in every scenario

Decision mix:

- `Apply`: 4
- `Pause`: 10
- `Do Not Apply`: 6

| ID | Scenario | Live preview | Result | First constraint | Safer rent display | Main risk |
| --- | --- | --- | --- | --- | --- | --- |
| C01 | Clean Austin | Ready to run gates | Apply | Listing can move forward if terms match | Clears current rent | none |
| C02 | NY fee cap | Ready to run gates | Do Not Apply | Policy gate is the first blocker | Fix terms first | Application fee above loaded cap |
| C03 | CA deposit prepaid cap | Ready to run gates | Do Not Apply | Policy gate is the first blocker | Fix terms first | Deposit/prepaid rent over loaded cap |
| C04 | WA high screening | Ready to run gates | Pause | Written confirmation is missing | Confirm first | High cost-only screening charge |
| C05 | Unknown income Chicago | Ready to run gates | Pause | Written confirmation is missing | Confirm first | Income, fee, deposit unknown |
| C06 | Miami cash gap | Cash short | Do Not Apply | Upfront cash stack is the first blocker | No safe target | Move-in cash gap |
| C07 | Dallas thin reserve | Ready to run gates | Pause | Upfront cash stack is the first blocker | $1,025/mo | Thin reserve |
| C08 | Denver income short | Income short | Do Not Apply | Income screen is the first blocker | $1,500/mo | Income screen |
| C09 | Charlotte pet pressure | Ready to run gates | Pause | Upfront cash stack is the first blocker | $1,225/mo | Thin reserve, fee unknown |
| C10 | Boston first last security | Ready to run gates | Apply | Listing can move forward if terms match | Clears current rent | none |
| C11 | Phoenix cap risk | Ready to run gates | Do Not Apply | Policy gate is the first blocker | Fix terms first | Deposit/prepaid rent over cap |
| C12 | Raleigh high deposit | Ready to run gates | Pause | Written confirmation is missing | Confirm first | Application fee rule unknown |
| C13 | NY clean | Ready to run gates | Apply | Listing can move forward if terms match | Clears current rent | none |
| C14 | Oregon disclosure | Ready to run gates | Apply | Listing can move forward if terms match | Clears current rent | none |
| C15 | Colorado high fee | Ready to run gates | Pause | Written confirmation is missing | Confirm first | High cost-only screening charge |
| C16 | Florida high deposit | Pause before paying | Do Not Apply | Upfront cash stack is the first blocker | $1,200/mo | Cash gap, fee unknown, pre-approval risk |
| C17 | SF high app fee | Ready to run gates | Pause | Written confirmation is missing | Confirm first | High cost-only screening charge |
| C18 | Vegas three-month deposit | Ready to run gates | Pause | Written confirmation is missing | Confirm first | Application fee rule unknown |
| C19 | Austin preapproval exposure | Pause before paying | Pause | Written confirmation is missing | Confirm first | Pre-approval cash at risk |
| C20 | San Antonio estimated deposit | Pause before paying | Pause | Upfront cash stack is the first blocker | $600/mo | Post-move buffer |

## Defects Found And Fixed

The first review pass found product-trust defects. These were fixed before the final scenario table above.

- Policy `Do Not Apply` cases returned an approval path that sounded procedural instead of explaining that the fee/deposit term must change.
- `Safer rent target` showed irrelevant high numbers in policy, confirmation, and already-safe cases.
- Massachusetts first month + last month + security deposit was incorrectly treated as a deposit/prepaid cap failure.
- The live preview said `Ready for gates`, which could read like approval. It now says `Ready to run gates`.
- Pause cases caused by thin reserve had generic "confirm unknown gate" language. They now call out reducing the upfront stack or adding the needed cash.
- `Move-in fee` appeared in the before-approval UI stack but was not included in `preApprovalCashRisk`. It now counts as pre-approval exposure and receives the before-approval risk badge.
- `Safer rent target` could dilute a user-entered 2x deposit back to a one-month city practice estimate, and treated prepaid rent as a fixed dollar amount. It now preserves listing-entered deposit multipliers and scales prepaid rent with the rent target.

## Expert Panel Review

Synthetic panel of 20 domain reviewers:

| Reviewer | Lens | Review |
| --- | --- | --- |
| E01 tenant attorney | Legal risk | The product correctly avoids legal advice language, but state coverage confidence must stay visible when rules are missing. |
| E02 landlord-tenant attorney | Fee caps | NY and CA policy blockers are useful. Policy failures should never show a rent-shopping solution first. Fixed. |
| E03 Massachusetts housing expert | State nuance | MA first + last + security is a special stack. The initial hard fail was wrong. Fixed. |
| E04 property manager | Operational realism | The "written payment schedule" message is realistic and useful. Add a copy button and save state. |
| E05 leasing agent | Workflow | Users need to know which values are estimates versus listing-confirmed. Add a confidence badge per field. |
| E06 tenant screener | Income rules | 2.5x / 3x / 3.5x is a good start, but add "property says exact rule" as an override label. |
| E07 consumer finance advisor | Cash buffer | Cash-to-clear is stronger than raw cost total. Keep it prominent. |
| E08 fair housing reviewer | Wording | Avoid implying protected-class or discretionary approval advice. Current income exception wording is acceptable but should stay cautious. |
| E09 relocation advisor | User timing | The product is best used after finding a listing, not at broad city research stage. |
| E10 renter advocate | Harm reduction | "Do not pay yet" is the strongest value. Keep it direct. |
| E11 real estate broker | Lead CTA | A lead CTA can work only after the result, not before trust is earned. |
| E12 compliance analyst | Source coverage | Missing application-fee states create many Pause outcomes. Expand seed rules before scaling programmatic pages. |
| E13 product counsel | Liability | "Risk guidance, not legal advice" is present, but copy should avoid "allowed/not allowed" unless source confidence is high. |
| E14 marketplace growth lead | Funnel | The tool has a clearer wedge than guide SEO. Best CTA is listing review or agent match, not selling the calculation. |
| E15 UX researcher | Comprehension | The decision brief is understandable. The cost stack is supporting evidence and should stay below the action plan. |
| E16 property operations lead | Missing data | Add number of applicants, application fee per adult, and move-in date. |
| E17 pet-friendly housing specialist | Pet costs | Add monthly pet rent and pet deposit separately from one-time pet fee. |
| E18 affordable housing advisor | Edge cases | Subsidized, voucher, student, and guarantor cases need separate disclaimers or modes. |
| E19 local moving estimator | Cost estimate | Moving cost should be marked as typical estimate, not listing-provided cash due. |
| E20 SEO/product strategist | Channel | Tool-first pages are aligned. Avoid rebuilding generic guides around this. |

Expert verdict:

- Product direction: strong
- Current output quality: usable beta
- Main risk before acquisition: incomplete state rule coverage and missing listing-specific inputs
- Monetization fit: post-result lead or review CTA, not paid result download

## User Panel Review

Synthetic panel of 20 renter users:

| User | Situation | Review |
| --- | --- | --- |
| U01 first-time renter | $5k cash, one listing | Understood `Do Not Pay` immediately. Wanted a copyable message. |
| U02 recent graduate | income near 3x | Wanted to know if offer letter or savings can help. |
| U03 immigrant renter | unfamiliar terms | Legal terms were hard. Needs plain-English hover help. |
| U04 pet owner | dog fee | Wanted pet rent and refundable pet deposit fields. |
| U05 NYC renter | $75 application fee | Liked fee cap warning. Wanted source link near the warning. |
| U06 California renter | prepaid rent | Understood blocker, but wanted "what amount would pass?" |
| U07 Florida renter | high move-in stack | The cash-to-clear number was useful. Wanted a split-payment script. |
| U08 Texas renter | high admin/holding | Pre-approval cash warning felt valuable. |
| U09 user with no income entered | skipped income | Pause was fair, but wanted "enter income to unlock result" prompt before submit. |
| U10 mobile user | phone only | Layout was readable after the contrast fix. |
| U11 anxious renter | afraid of denial | Wanted a checklist to send before paying. Current message helps. |
| U12 experienced renter | multiple apartments | Wanted side-by-side comparison across listings. |
| U13 low-cash renter | cash gap | Wanted "show me cheaper rent target" only when it is actually relevant. Fixed. |
| U14 Boston renter | first/last/security | The final result felt more credible after removing the false hard fail. |
| U15 Seattle renter | high screening fee | Pause made sense, but itemization ask should be more specific. |
| U16 Denver renter | income short | The income-needed number was clear. Wanted rent target based on income. Fixed. |
| U17 North Carolina renter | deposit cap depends on lease term | Wanted a lease term field. |
| U18 Las Vegas renter | large deposit | Wanted to know whether three months is legal or just risky. |
| U19 renter comparing two units | decision workflow | Wants saved results and a share link. |
| U20 skeptical user | trust | Needs visible data freshness/source confidence before trusting rule outputs. |

User verdict:

- Users understood the primary verdict.
- Users reacted more strongly to `cash to clear`, `pre-approval risk`, and `message to send` than to the cost stack.
- Users wanted the product to do the next action: copy message, compare listings, show what needs to change.

## Product Readiness

Current readiness:

- Decision engine: beta-ready
- Result UI: beta-ready
- Acquisition scale: not ready for aggressive scaling
- Monetization: lead CTA test ready after post-result trust step

Most important next changes:

1. Add copy-to-clipboard for the property message. `Implemented 2026-04-25.`
2. Add "What would make this apply-safe?" sensitivity panel. `Implemented 2026-04-25.`
   - remove prepaid rent
   - reduce deposit
   - split admin/holding fee
   - lower rent target
   - add cash amount
3. Add missing listing fields:
   - number of applicants
   - fee per applicant
   - lease term
   - monthly pet rent
   - pet deposit vs pet fee
   - listing-confirmed vs estimated deposit
   - broker fee
   - `Implemented 2026-04-25 as optional details to keep quick-entry friction low.`
   - `Still missing: move-in date and refundable/non-refundable toggles by fee type.`
4. Add state/source confidence badges directly beside policy flags.
5. Add post-result CTA test:
   - variant A: `Review this listing before you pay`
   - variant B: `Find an apartment with lower move-in cash`
   - variant C: `Send this question to the property`

Conclusion:

The product is no longer a simple math calculator. It now gives a decision, the first constraint, the path to make the listing safer, and a message to send before paying. The next product leap is workflow: help users change the outcome or route them to someone who can.

## 2026-04-25 Workflow Update

Implemented the first post-result workflow layer:

- `Copy` button on the property message, with clipboard fallback and visible `Copied` state
- `What would make this apply-safe?` panel
- scenario-specific apply-safe changes:
  - lower upfront cash or add cash
  - search near a safer rent target when relevant
  - remove prepaid rent or reduce deposit-like cash
  - convert pre-approval charges into written refundable or credited terms
  - confirm income when missing
  - stop and re-run if a portal adds new charges after an `Apply` result

Verification:

- `./gradlew.bat test --no-configuration-cache` passed
- browser QA confirmed copy button state changes to `Copied`
- desktop and mobile overflow remained `0`

## 2026-04-25 Calculation Logic Audit

Audit question:

- Would a renter believe the verdict, not just the arithmetic?

Findings:

- The verdict is strongest when it says `Do not pay yet`, shows the first failed gate, and gives a specific property message.
- Users will not trust the product if a cost is visually grouped as pre-approval risk but omitted from the pre-approval exposure number. Fixed for `move-in fee`.
- Users will not trust the safer rent target if they entered an unusually high listing deposit and the system silently replaces it with a market-average deposit assumption. Fixed by using the entered deposit multiplier when present.
- Prepaid rent is usually tied to rent level, not a flat fee. The safer rent target now treats prepaid rent as a rent-scaled variable cost.

Remaining model limits:

- The engine now supports `number of applicants`, `lease term`, `monthly pet rent`, `pet deposit`, listing-confirmed deposit, and `broker fee` without forcing them into the first-step form.
- It still needs `move-in date` and refundable/non-refundable toggles by fee type to feel product-grade for more cases.
- State fee/deposit rules are seed rules. Strong legal-adjacent claims should wait for URL-backed source confidence and visible freshness labels.
- California prepaid-rent treatment remains a high-risk interpretation area. Until verified with stronger source handling, the product should keep confirmation language prominent.

## 2026-04-25 Input Friction Update

Implementation decision:

- Keep the core form short: city, rent, cash, income, before-approval fees, and move-in stack.
- Put precision fields behind `Optional details`.
- Treat missing optional precision as a confirmation risk instead of blocking form submission.
- Let users refine inputs on the result page, because reaching the result proves higher intent and lowers the perceived cost of deeper entry.

Added:

- applicant count, so application fee can be multiplied across applicants
- lease term
- broker fee
- pet deposit and monthly pet rent
- listing-confirmed deposit checkbox
- result-side `Listing detail` summary showing which precision inputs were used
- result-side `Refine result` form with prefilled inputs and deeper fields open

Validation:

- `./gradlew.bat test --no-configuration-cache` passed
- browser QA confirmed 2 applicants turn a $75 application fee into $150 in the cost stack
- browser QA confirmed result-page refinement can change an `Apply` result to `Pause`
- desktop overflow: `0`
- mobile optional-details overflow: `0`

## 2026-04-25 Risk Resolution Update

Implementation decision:

- Make the result page behave like a semi-paid review packet, not just a static answer.
- If a user resolves a risk through refined inputs, the verdict should dynamically change and explain why.
- Refund/credit protection must affect the actual risk engine, not only the UI.

Added:

- `What changed` strip after a result-page rerun
- move-in date input on the result-page refinement form
- refundable/credited toggles for:
  - application fee
  - admin fee
  - holding deposit
  - move-in fee
  - broker fee
- refundable/credited pre-approval charges are removed from `preApprovalCashRisk`
- cost stack notes now show when a charge was marked refundable or credited

Validation:

- `./gradlew.bat test --no-configuration-cache` passed
- browser QA confirmed a case changed from `Pause` to `Apply` after application/admin/move-in charges were marked refundable or credited
- browser QA confirmed `What changed` showed `Verdict: Pause -> Apply`
- browser QA confirmed `Refund protection: 0 protected charges -> 3 protected charges`
- desktop overflow: `0`
- mobile overflow: `0`

## 2026-04-25 Clarity And CTA Update

Implementation decision:

- Raise understanding and conviction before asking for a lead.
- Avoid sponsor dependency in the first CTA. The first conversion should be an owned `listing review request`, not an agent handoff.
- Use sponsor matching only after demand is proven or supply is secured.

Added:

- `In plain English` summary on the result page
- `Why` and `Before you pay` interpretation cards
- `Term decoder` for before-approval money, refundable/credited, and cash to clear
- CTA changed to `Get an apply-safe listing review`
- CTA uses email-based lead capture with the current verdict, city, rent, cash, cash-to-clear, and first constraint prefilled
- Secondary action remains `Check another listing`

Validation:

- `./gradlew.bat test --no-configuration-cache` passed
- browser QA confirmed plain-English summary renders
- browser QA confirmed term decoder renders
- browser QA confirmed lead CTA renders and uses a `mailto:` link
- desktop overflow: `0`
- mobile overflow: `0`
