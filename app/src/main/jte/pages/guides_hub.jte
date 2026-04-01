@import java.util.List
@import firstrentverdict.content.GuideCatalog.GuideEntry
@param List<GuideEntry> guides
@param String pageTitle
@param String metaDescription
@param String baseUrl = ""

@template.layout.main(
    title = pageTitle,
    description = metaDescription,
    canonical = baseUrl + "/RentVerdict/guides",
    content = @`
    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 3rem 1.5rem;">
        
        <!-- Breadcrumbs -->
        <nav style="margin-bottom: 2.5rem; font-size: 0.825rem; color: var(--text-tertiary); display: flex; gap: 0.5rem; align-items: center;">
            <a href="/RentVerdict/" style="color: inherit;">Home</a> 
            <span>/</span> 
            <span style="color: var(--text-secondary); font-weight: 500;">Guides</span>
        </nav>

        <header style="margin-bottom: 4rem;">
            <h1 style="font-size: clamp(2rem, 5vw, 3.25rem); font-weight: 800; line-height: 1.1; margin-bottom: 1.5rem; letter-spacing: -0.03em;">
                <span class="gradient-text">${pageTitle}</span>
            </h1>
            <p style="font-size: 1.25rem; color: var(--text-secondary); line-height: 1.6; max-width: 750px;">
                ${metaDescription}
            </p>
        </header>

        <section style="margin-bottom: 2rem; background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 1rem 1.25rem;">
            <h2 style="font-size: 0.95rem; margin-bottom: 0.75rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-tertiary);">Core Decision Links</h2>
            <div style="display: flex; flex-wrap: wrap; gap: 0.5rem;">
                <a href="/RentVerdict/" class="btn btn-intent">Run Calculator</a>
                <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-intent">Bad Credit + No Cosigner</a>
                <a href="/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment" class="btn btn-intent">Move-In Cash Guide</a>
                <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-intent">Guarantor vs Deposit</a>
                <a href="/RentVerdict/guides/pet-deposit-and-pet-rent-negotiation-guide" class="btn btn-intent">Pet Fee Negotiation</a>
                <a href="/RentVerdict/research/move-in-cost-index" class="btn btn-intent">Monthly Cost Index</a>
                <a href="/RentVerdict/cities" class="btn btn-intent">Supporting Cities</a>
            </div>
        </section>

        <section style="margin-bottom: 2rem; background: linear-gradient(135deg, rgba(245, 158, 11, 0.08) 0%, rgba(251, 191, 36, 0.04) 100%); border: 1px solid rgba(245, 158, 11, 0.25); border-radius: 12px; padding: 1.25rem 1.5rem;">
            <div style="font-size: 0.8rem; margin-bottom: 0.5rem; text-transform: uppercase; letter-spacing: 0.05em; color: #b45309; font-weight: 700;">Start Here</div>
            <h2 style="font-size: 1.2rem; margin-bottom: 0.5rem; color: var(--text-primary);">If credit is the blocker, do not start with city pages.</h2>
            <p style="margin: 0 0 1rem; color: var(--text-secondary); line-height: 1.6;">
                Start with the approval playbook, then compare guarantor vs deposit, then run the calculator. This is the shortest route to a real yes.
            </p>
            <div style="display: flex; flex-wrap: wrap; gap: 0.75rem;">
                <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-primary">Open Bad Credit Playbook</a>
                <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-secondary">Compare Approval Backstops</a>
            </div>
        </section>

        <section style="margin-bottom: 2rem;">
            <div style="display: flex; flex-wrap: wrap; gap: 0.5rem;" id="intent-filters">
                <button type="button" data-intent-filter="all" class="btn btn-primary btn-sm">All</button>
                <button type="button" data-intent-filter="approval" class="btn btn-secondary btn-sm">Approval</button>
                <button type="button" data-intent-filter="cost" class="btn btn-secondary btn-sm">Cost</button>
                <button type="button" data-intent-filter="recovery" class="btn btn-secondary btn-sm">Recovery</button>
                <button type="button" data-intent-filter="policy" class="btn btn-secondary btn-sm">Policy</button>
            </div>
        </section>

        <div style="display: grid; gap: 2rem;" id="guide-grid">
            @for(GuideEntry guide : guides)
                !{
                    String intentLabel = "Approval";
                    if ("cost".equals(guide.intentCategory())) {
                        intentLabel = "Cost";
                    } else if ("recovery".equals(guide.intentCategory())) {
                        intentLabel = "Recovery";
                    } else if ("policy".equals(guide.intentCategory())) {
                        intentLabel = "Policy";
                    }
                }
                <div class="card glass-morphism guide-card" data-intent="${guide.intentCategory()}" style="padding: 2rem; border-radius: var(--radius-lg); border-left: 5px solid var(--accent-primary);">
                    <div style="font-size: 0.75rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 0.5rem;">
                        Persona: ${guide.persona()}
                    </div>
                    <h2 style="font-size: 1.5rem; margin-bottom: 1rem; font-weight: 700;">
                        <a href="/RentVerdict/guides/${guide.slug()}" style="color: var(--text-primary); text-decoration: none;">${guide.title()}</a>
                    </h2>
                    <p style="line-height: 1.6; color: var(--text-secondary); margin-bottom: 1rem;">${guide.excerpt()}</p>
                    <div style="display: flex; flex-wrap: wrap; gap: 0.5rem; margin-bottom: 1rem;">
                        <span class="badge badge-outline">Intent: ${intentLabel}</span>
                        <span class="badge badge-outline">Keyword: ${guide.primaryKeyword()}</span>
                    </div>
                    <p style="font-size: 0.9rem; color: var(--text-tertiary); margin-bottom: 1.5rem;">${guide.searchIntent()}</p>
                    <a href="/RentVerdict/guides/${guide.slug()}" style="font-weight: 700; color: var(--accent-primary); text-decoration: none;">Read Guide &rarr;</a>
                </div>
            @endfor
        </div>
    </div>

    <script>
        (function () {
            const filterButtons = document.querySelectorAll('[data-intent-filter]');
            const cards = document.querySelectorAll('.guide-card');
            if (!filterButtons.length || !cards.length) return;

            function setFilter(intent) {
                cards.forEach((card) => {
                    const show = intent === 'all' || card.getAttribute('data-intent') === intent;
                    card.style.display = show ? 'block' : 'none';
                });

                filterButtons.forEach((btn) => {
                    const isActive = btn.getAttribute('data-intent-filter') === intent;
                    btn.className = isActive ? 'btn btn-primary btn-sm' : 'btn btn-secondary btn-sm';
                });
            }

            filterButtons.forEach((button) => {
                button.addEventListener('click', () => setFilter(button.getAttribute('data-intent-filter')));
            });
        })();
    </script>
    `
)


