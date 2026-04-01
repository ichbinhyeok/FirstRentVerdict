@import firstrentverdict.controller.ResearchController.CostIndexRow
@import java.time.LocalDate
@import java.time.format.DateTimeFormatter
@import java.util.List

@param LocalDate reportMonth
@param int sampleSize
@param List<CostIndexRow> expensiveRows
@param List<CostIndexRow> affordableRows
@param List<CostIndexRow> pressureRows
@param String baseUrl = ""

!{
    String monthLabel = reportMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    String reportDateIso = reportMonth.toString();
    String articleHeadline = ("Monthly Move-In Cost Index - " + monthLabel)
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "");
    String articleDate = reportDateIso
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "");
    String articleUrl = (baseUrl + "/RentVerdict/research/move-in-cost-index")
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "");
}

@template.layout.main(
    title = "Monthly Move-In Cost Index (" + monthLabel + "): Apartment Approval and Cash Pressure",
    description = "See the latest city-level move-in cost index: rent, deposit, and upfront cash requirements across major US cities.",
    canonical = baseUrl + "/RentVerdict/research/move-in-cost-index",
    content = @`
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "Article",
      "headline": "$unsafe{articleHeadline}",
      "datePublished": "$unsafe{articleDate}",
      "dateModified": "$unsafe{articleDate}",
      "author": {
        "@type": "Organization",
        "name": "First Rent Verdict"
      },
      "publisher": {
        "@type": "Organization",
        "name": "First Rent Verdict"
      },
      "mainEntityOfPage": "$unsafe{articleUrl}"
    }
    </script>

    <div class="container" style="max-width: 1100px; margin: 0 auto; padding: 3rem 1.5rem;">
        <nav style="margin-bottom: 2rem; font-size: 0.825rem; color: var(--text-tertiary); display: flex; gap: 0.5rem; align-items: center;">
            <a href="/RentVerdict/" style="color: inherit;">Home</a>
            <span>/</span>
            <span style="color: var(--text-secondary); font-weight: 500;">Research</span>
        </nav>

        <header style="margin-bottom: 2.5rem;">
            <div class="badge badge-outline" style="margin-bottom: 1rem;">Monthly Data Release</div>
            <h1 style="font-size: clamp(2rem, 5vw, 3rem); font-weight: 800; line-height: 1.1; margin-bottom: 1rem;">
                Move-In Cost Index: ${monthLabel}
            </h1>
            <p style="font-size: 1.1rem; color: var(--text-secondary); max-width: 860px; line-height: 1.7;">
                This index tracks estimated move-in liquidity across ${sampleSize} audited US cities using median rent, typical deposit multipliers, and local moving costs.
            </p>
        </header>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem; margin-bottom: 2rem;">
            <div class="card" style="padding: 1.2rem 1.4rem;">
                <div style="font-size: 0.75rem; color: var(--text-tertiary); text-transform: uppercase;">Coverage</div>
                <div style="font-size: 1.6rem; font-weight: 800;">${sampleSize} Cities</div>
            </div>
            <div class="card" style="padding: 1.2rem 1.4rem;">
                <div style="font-size: 0.75rem; color: var(--text-tertiary); text-transform: uppercase;">Highest Upfront (Sample)</div>
                <div style="font-size: 1.2rem; font-weight: 700;">
                    @if(!expensiveRows.isEmpty())
                        ${expensiveRows.get(0).city()}, ${expensiveRows.get(0).state()}
                    @else
                        N/A
                    @endif
                </div>
            </div>
            <div class="card" style="padding: 1.2rem 1.4rem;">
                <div style="font-size: 0.75rem; color: var(--text-tertiary); text-transform: uppercase;">Lowest Upfront (Sample)</div>
                <div style="font-size: 1.2rem; font-weight: 700;">
                    @if(!affordableRows.isEmpty())
                        ${affordableRows.get(0).city()}, ${affordableRows.get(0).state()}
                    @else
                        N/A
                    @endif
                </div>
            </div>
        </div>

        <section style="margin-bottom: 3rem;">
            <h2 style="font-size: 1.4rem; margin-bottom: 1rem;">Top 20 Highest Move-In Cash Markets</h2>
            <div style="overflow-x: auto;">
                <table style="width: 100%; border-collapse: collapse; font-size: 0.95rem;">
                    <thead>
                        <tr style="text-align: left; border-bottom: 1px solid var(--border-default);">
                            <th style="padding: 0.75rem 0.5rem;">City</th>
                            <th style="padding: 0.75rem 0.5rem;">Median Rent</th>
                            <th style="padding: 0.75rem 0.5rem;">Deposit</th>
                            <th style="padding: 0.75rem 0.5rem;">Moving</th>
                            <th style="padding: 0.75rem 0.5rem;">Total Upfront</th>
                        </tr>
                    </thead>
                    <tbody>
                        @for(CostIndexRow row : expensiveRows)
                            <tr style="border-bottom: 1px solid var(--border-subtle);">
                                <td style="padding: 0.65rem 0.5rem;">
                                    <a href="/RentVerdict/verdict/${row.slug()}" style="color: inherit;">${row.city()}, ${row.state()}</a>
                                </td>
                                <td style="padding: 0.65rem 0.5rem;">$${String.format("%,d", row.rent())}</td>
                                <td style="padding: 0.65rem 0.5rem;">$${String.format("%,d", row.deposit())}</td>
                                <td style="padding: 0.65rem 0.5rem;">$${String.format("%,d", row.moving())}</td>
                                <td style="padding: 0.65rem 0.5rem; font-weight: 700;">$${String.format("%,d", row.upfront())}</td>
                            </tr>
                        @endfor
                    </tbody>
                </table>
            </div>
        </section>

        <section style="margin-bottom: 3rem;">
            <h2 style="font-size: 1.4rem; margin-bottom: 1rem;">Top 20 Lowest Move-In Cash Markets</h2>
            <div style="overflow-x: auto;">
                <table style="width: 100%; border-collapse: collapse; font-size: 0.95rem;">
                    <thead>
                        <tr style="text-align: left; border-bottom: 1px solid var(--border-default);">
                            <th style="padding: 0.75rem 0.5rem;">City</th>
                            <th style="padding: 0.75rem 0.5rem;">Median Rent</th>
                            <th style="padding: 0.75rem 0.5rem;">Deposit</th>
                            <th style="padding: 0.75rem 0.5rem;">Moving</th>
                            <th style="padding: 0.75rem 0.5rem;">Total Upfront</th>
                        </tr>
                    </thead>
                    <tbody>
                        @for(CostIndexRow row : affordableRows)
                            <tr style="border-bottom: 1px solid var(--border-subtle);">
                                <td style="padding: 0.65rem 0.5rem;">
                                    <a href="/RentVerdict/verdict/${row.slug()}" style="color: inherit;">${row.city()}, ${row.state()}</a>
                                </td>
                                <td style="padding: 0.65rem 0.5rem;">$${String.format("%,d", row.rent())}</td>
                                <td style="padding: 0.65rem 0.5rem;">$${String.format("%,d", row.deposit())}</td>
                                <td style="padding: 0.65rem 0.5rem;">$${String.format("%,d", row.moving())}</td>
                                <td style="padding: 0.65rem 0.5rem; font-weight: 700;">$${String.format("%,d", row.upfront())}</td>
                            </tr>
                        @endfor
                    </tbody>
                </table>
            </div>
        </section>

        <section style="margin-bottom: 3rem;">
            <h2 style="font-size: 1.4rem; margin-bottom: 1rem;">Highest Rent-to-Income Pressure (ACS)</h2>
            <div style="display: grid; gap: 0.75rem;">
                @for(CostIndexRow row : pressureRows)
                    <div class="card" style="padding: 0.9rem 1rem; display: flex; justify-content: space-between; gap: 1rem; align-items: center;">
                        <div>
                            <a href="/RentVerdict/verdict/${row.slug()}" style="color: inherit; font-weight: 700;">${row.city()}, ${row.state()}</a>
                            <div style="font-size: 0.85rem; color: var(--text-tertiary);">Pressure: ${row.pressureLabel()}</div>
                        </div>
                        <div style="font-weight: 700;">
                            @if(row.rentToIncomePct() != null)
                                ${String.format("%.1f", row.rentToIncomePct())}%
                            @else
                                N/A
                            @endif
                        </div>
                    </div>
                @endfor
            </div>
        </section>

        <div style="display: flex; flex-wrap: wrap; gap: 0.75rem;">
            <a href="/RentVerdict/" class="btn btn-primary">Run Your Own Affordability Check</a>
            <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-secondary">Open Bad Credit Playbook</a>
            <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-secondary">Compare Approval Backstops</a>
            <a href="/RentVerdict/cities" class="btn btn-secondary">Browse All City Audits</a>
        </div>
    </div>
    `
)
