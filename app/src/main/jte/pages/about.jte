@param String baseUrl = ""

@template.layout.main(
    title = "About First Rent Verdict: Team, Standards, and Trust Policy",
    description = "First Rent Verdict is an independent renter decision tool. Learn how the virtual operating team, data checks, and review standards work.",
    canonical = baseUrl + "/RentVerdict/about",
    content = @`
    <main class="trust-page">
        <section class="trust-hero">
            <span>About First Rent Verdict</span>
            <h1>A renter risk tool, not an approval promise.</h1>
            <p>First Rent Verdict helps renters slow down before paying application fees, admin fees, holding deposits, and move-in cash. The product turns listing numbers into a practical risk screen: apply, pause, or do not pay yet.</p>
        </section>

        <section class="trust-grid">
            <article>
                <span>Operating model</span>
                <h2>Built by a virtual product and data team</h2>
                <p>First Rent Verdict is operated as an independent software project by a small virtual team model. The named functions below describe the review roles used to build and maintain the product. They are not claims that licensed attorneys, financial advisors, brokers, or property managers reviewed each result.</p>
            </article>
            <article>
                <span>Boundary</span>
                <h2>Guidance, not legal or financial advice</h2>
                <p>The tool does not guarantee approval, rental availability, refund rights, or legal outcomes. It highlights risk factors to verify with the property manager, lease documents, local authority, or a qualified professional.</p>
            </article>
        </section>

        <section class="trust-panel">
            <span>Virtual team roles</span>
            <div class="trust-role-list">
                <div>
                    <strong>Product desk</strong>
                    <p>Defines the renter decision flow and keeps pages focused on before-you-pay actions.</p>
                </div>
                <div>
                    <strong>Data desk</strong>
                    <p>Maintains city rent, fee, deposit, and screening assumptions from documented sources.</p>
                </div>
                <div>
                    <strong>Risk desk</strong>
                    <p>Reviews wording so the product flags uncertainty instead of making legal or approval claims.</p>
                </div>
                <div>
                    <strong>User review desk</strong>
                    <p>Uses test cases and renter feedback to find confusing outputs, missing inputs, and weak explanations.</p>
                </div>
            </div>
        </section>

        <section class="trust-grid">
            <article>
                <span>What we optimize for</span>
                <h2>Clear next questions before payment</h2>
                <p>The best output is not a dramatic verdict. It is a short list of numbers and written confirmations the renter should get before paying.</p>
            </article>
            <article>
                <span>What we avoid</span>
                <h2>No fake certainty</h2>
                <p>We avoid claims like approved, illegal, guaranteed refundable, or financially safe unless the user supplied enough verified facts. Most outputs are risk screens, not final answers.</p>
            </article>
        </section>

        <section class="trust-links">
            <a href="/RentVerdict/methodology">Methodology</a>
            <a href="/RentVerdict/data-sources">Data sources</a>
            <a href="/RentVerdict/corrections">Correction policy</a>
            <a href="/RentVerdict/terms">Terms</a>
        </section>
    </main>
`)
