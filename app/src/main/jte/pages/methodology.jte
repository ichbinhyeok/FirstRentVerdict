@param String baseUrl = ""

@template.layout.main(
    title = "First Rent Verdict Methodology: Application Risk, Cash, Fees, and Deposits",
    description = "How First Rent Verdict calculates renter application risk using cash stack, income screen, before-approval fees, deposits, and documented assumptions.",
    canonical = baseUrl + "/RentVerdict/methodology",
    content = @`
    <main class="trust-page">
        <section class="trust-hero">
            <span>Methodology</span>
            <h1>How the application risk screen works.</h1>
            <p>The model is intentionally conservative. It does not predict landlord approval. It checks whether paying now creates avoidable cash loss, move-in pressure, or unanswered screening risk.</p>
        </section>

        <section class="trust-panel">
            <span>Decision gates</span>
            <div class="trust-method-list">
                <div>
                    <strong>1. Cash gate</strong>
                    <p>Estimates rent, deposits, prepaid rent, utilities, pet costs, move-in charges, and a post-move cash buffer. If the stack drains cash below the buffer, the result moves toward pause or do not apply.</p>
                </div>
                <div>
                    <strong>2. Income gate</strong>
                    <p>Compares gross monthly income with the selected rent-to-income assumption, usually 2.5x to 3.5x rent. This is a screening proxy, not an approval guarantee.</p>
                </div>
                <div>
                    <strong>3. Fee gate</strong>
                    <p>Flags application fees, admin fees, holding deposits, broker fees, and move-in fees paid before approval. Refund or credit terms reduce risk only when entered or confirmed.</p>
                </div>
                <div>
                    <strong>4. Deposit gate</strong>
                    <p>Checks security deposit and prepaid rent pressure against available state rule coverage and the renter's entered cash stack.</p>
                </div>
            </div>
        </section>

        <section class="trust-grid">
            <article>
                <span>Output language</span>
                <h2>Apply, pause, or do not pay yet</h2>
                <p>Apply means the entered numbers clear the model and written confirmation is still recommended. Pause means one or more terms should be verified first. Do not apply means the current numbers create a hard cash, policy, or written-confirmation blocker.</p>
            </article>
            <article>
                <span>Known limits</span>
                <h2>The model cannot see the actual lease</h2>
                <p>Property-specific screening, concessions, local ordinances, lease clauses, and refund enforcement can differ from the dataset. Users should verify every fee and due date in writing before paying.</p>
            </article>
        </section>

        <section class="trust-note">
            <strong>Important boundary</strong>
            <p>First Rent Verdict is not a law firm, financial advisor, lender, broker, or housing authority. The tool provides informational risk screening only.</p>
        </section>
    </main>
`)
