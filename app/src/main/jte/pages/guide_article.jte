@import java.util.Map
@param Map<String, String> guide
@param String baseUrl = "https://movecostinfo.com"

@template.layout.main(
    title = guide.get("title"),
    description = guide.get("excerpt"),
    canonical = baseUrl + "/RentVerdict/guides/" + guide.get("slug"),
    content = @`
    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 3rem 1.5rem;">
        <!-- Breadcrumbs -->
        <nav style="margin-bottom: 2.5rem; font-size: 0.825rem; color: var(--text-tertiary); display: flex; gap: 0.5rem; align-items: center;">
            <a href="/RentVerdict/" style="color: inherit;">Home</a> 
            <span>/</span>
            <a href="/RentVerdict/guides" style="color: inherit;">Guides</a> 
            <span>/</span> 
            <span style="color: var(--text-secondary); font-weight: 500;">${guide.get("title")}</span>
        </nav>

        <header style="margin-bottom: 4rem;">
            <h1 style="font-size: clamp(2rem, 5vw, 3.25rem); font-weight: 800; line-height: 1.1; margin-bottom: 1.5rem; letter-spacing: -0.03em;">
                <span class="gradient-text">${guide.get("title")}</span>
            </h1>
            <p style="font-size: 1.25rem; color: var(--text-secondary); line-height: 1.6; max-width: 750px;">
                ${guide.get("excerpt")}
            </p>
            <div style="margin-top: 1rem; color: var(--text-tertiary); font-size: 0.85rem; text-transform: uppercase;">
                LATEST AUDIT: 2026 EDITION
            </div>
        </header>

        <article style="line-height: 1.8; color: var(--text-secondary); margin-bottom: 6rem; font-size: 1.125rem;">
            <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; margin-bottom: 2rem; border: 1px solid var(--border-subtle);">
                <p>Welcome to the guide. In 2026, algorithmic leasing systems process 80% of all major metro rental applications. By default, anything outside their standard parameters is rejected.</p>
                <p>However, understanding the mechanics of these algorithms—and where human negotiation can still override them—is the key to securing an apartment.</p>
            </div>

            <h2 style="font-size: 2rem; color: var(--text-primary); margin-bottom: 1.5rem; margin-top: 3rem;">Actionable Strategies</h2>
            <p>Your local laws directly affect how much they can charge you to compensate for perceived risk. Many states have capped these deposits, but corporate entities still try to bypass them with non-refundable "fees."</p>
            <ul style="list-style-type: disc; padding-left: 2rem; margin-bottom: 2rem;">
                <li>Identify if your moving destination has state-wide security deposit caps.</li>
                <li>Avoid "surety bonds" masquerading as simple deposits unless absolutely necessary.</li>
                <li>Show proof of out-sized liquidity (e.g. proof of 6-months rent in a checking account) to bypass credit requirements.</li>
            </ul>

            <h2 style="font-size: 2rem; color: var(--text-primary); margin-bottom: 1.5rem; margin-top: 3rem;">Regional Hubs</h2>
            <p>If you're planning a move, check out the specific rules and financial traps for your target market using our Rent Verdict Audits:</p>
            <div style="display: flex; flex-wrap: wrap; gap: 0.75rem; margin-top: 2rem;">
                <a href="/RentVerdict/cities" class="btn btn-outline-primary">Browse All City Audits</a>
                <a href="/RentVerdict/" class="btn btn-primary">Run the Simulator</a>
            </div>
        </article>
    </div>
    `
)
