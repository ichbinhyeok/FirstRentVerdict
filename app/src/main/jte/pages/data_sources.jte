@param String baseUrl = ""

@template.layout.main(
    title = "First Rent Verdict Data Sources and Update Policy",
    description = "Data source categories, update policy, and limitations for First Rent Verdict's rent, fee, deposit, and screening assumptions.",
    canonical = baseUrl + "/RentVerdict/data-sources",
    content = @`
    <main class="trust-page">
        <section class="trust-hero">
            <span>Data sources</span>
            <h1>Where the tool gets its assumptions.</h1>
            <p>First Rent Verdict combines user-entered listing numbers with reference data. The reference data is used to estimate risk, not to certify the exact terms of a specific apartment.</p>
        </section>

        <section class="trust-panel">
            <span>Source categories</span>
            <div class="trust-method-list">
                <div>
                    <strong>Rent and city baselines</strong>
                    <p>Market rent references, city-level rent datasets, and internal normalized rent files. Users should replace estimates with the actual listing rent whenever possible.</p>
                </div>
                <div>
                    <strong>Application fee and deposit rules</strong>
                    <p>State-level rule notes collected from public regulatory, attorney general, housing authority, and statute references where available. Coverage is incomplete by design and shown as loaded or not loaded.</p>
                </div>
                <div>
                    <strong>Screening income assumptions</strong>
                    <p>Common renter screening ratios such as 2.5x, 3x, or 3.5x rent. These are assumptions, not universal landlord rules.</p>
                </div>
                <div>
                    <strong>Risk vocabulary</strong>
                    <p>Plain-English explanations for terms such as holding deposit, refundable, credited, before approval, and cash to clear.</p>
                </div>
            </div>
        </section>

        <section class="trust-grid">
            <article>
                <span>Freshness</span>
                <h2>Dataset dates matter</h2>
                <p>When a dataset is stale, missing, or state coverage is not loaded, the product should expose that uncertainty rather than pretending to know. Users should verify current local rules before relying on a result.</p>
            </article>
            <article>
                <span>Source conflict</span>
                <h2>Specific listing terms win</h2>
                <p>If a property gives a written fee schedule or lease clause that differs from an estimate, the written property-specific terms should be entered into the checker and independently reviewed.</p>
            </article>
        </section>

        <section class="trust-note">
            <strong>Report a data issue</strong>
            <p>Send correction requests through the correction policy page with the city, state, URL, field, expected value, and source link.</p>
            <a href="/RentVerdict/corrections">Correction policy</a>
        </section>
    </main>
`)
