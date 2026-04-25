# V3 pSEO GSC Tracking

Date: `2026-04-25`

Goal:

- Grow SERP surface without repeating the V2 mistake of broad, dispersed, information-first pages.
- Treat every pSEO URL as a prefilled state of the same application-risk checker.
- Expand or prune by page family, not by isolated page anecdotes.

## Current Indexed Families

| Family | URL pattern | Search intent | Tool state |
| --- | --- | --- | --- |
| Cash/rent | `/can-i-apply-with/{cash}/for/{rent}/in/{city}` | Can I apply with this cash for this rent? | rent + cash |
| Application fee | `/application-fee/{fee}/in/{city}` | Should I pay this application fee? | rent + application fee |
| Admin fee | `/admin-fee/{fee}/in/{city}` | Should I pay this admin fee before approval? | rent + admin fee |
| Holding deposit | `/holding-deposit/{amount}/in/{city}` | Is this holding deposit risky? | rent + holding deposit |
| Income/rent | `/can-i-apply-with/{income}/income-for/{rent}/rent-in/{city}` | Can this income clear this rent? | rent + gross income |
| First/last/security | `/first-last-security-deposit-in/{city}` | Can I handle first, last, and security? | rent + security deposit + prepaid rent |

## Sitemap Experiment

Current setting:

- `seo.sitemap.city-limit: 50`
- New family URLs are generated for the selected sitemap city set.

Approximate new V3 tool-state surface:

- 3 cash/rent pages per city
- 2 application fee pages per city
- 1 admin fee page per city
- 1 holding deposit page per city
- 2 income/rent pages per city
- 1 first/last/security page per city

At 50 cities, this is roughly 500 V3 tool-state URLs before legacy/core pages.

## GSC Family Dashboard

Track weekly by family:

| Family | Indexed | Impressions | CTR | Avg position | Clicks | Run gates | Result reached | Refine used | CTA clicks | Decision |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | --- |
| Cash/rent |  |  |  |  |  |  |  |  |  |  |
| Application fee |  |  |  |  |  |  |  |  |  |  |
| Admin fee |  |  |  |  |  |  |  |  |  |  |
| Holding deposit |  |  |  |  |  |  |  |  |  |  |
| Income/rent |  |  |  |  |  |  |  |  |  |  |
| First/last/security |  |  |  |  |  |  |  |  |  |  |

## Expansion Rules

Minimum page quality before expansion:

- family-specific first viewport, not only a changed H1
- visible state rule badges when fee/deposit rule coverage exists
- related same-city scenario links so each URL behaves like one state inside the checker, not an orphan page
- prefilled form values must materially change the calculation path

Expand a family when:

- it gets impressions across multiple cities
- CTR is at least `1%`
- tool submit rate is non-zero
- result-page engagement exists

Improve titles/H1 when:

- impressions exist
- CTR is below `1%`
- average position is not terrible but clicks are weak

Improve first viewport when:

- clicks exist
- `Run gates` rate is weak
- users bounce before result

Prune or noindex a family when:

- pages index but get no impressions after a reasonable crawl window
- impressions are broad/info-only and do not lead to tool starts
- pages behave like city-guide pages instead of checker entry points

## Family-Specific Hypotheses

Cash/rent:

- Likely broadest reach.
- Watch whether users actually submit or only browse.

Application fee:

- Strongest fit for `before you pay` positioning.
- Best early family for listing review CTA.

Admin fee:

- High anxiety but lower search vocabulary consistency.
- Test title variants around `apartment admin fee before approval`.

Holding deposit:

- Needs refundability language.
- Watch for state-specific queries.

Income/rent:

- Clear calculation intent.
- May attract affordability traffic, so keep the application-screen framing visible.

First/last/security:

- Strong in states/markets where this stack is common.
- Needs strong state nuance and legal-adjacent caution.

## Rule

Do not scale a page family because it is easy to generate.

Scale only when the family creates a different user decision state and produces measurable SERP or product engagement.
