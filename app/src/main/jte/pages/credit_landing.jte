@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@import firstrentverdict.service.seo.CityContentGenerator.QnA
@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import java.util.List
@param CityPageContent pageData
@param String tier
@param List<CityEntry> relatedCities = java.util.Collections.emptyList()
@param String canonicalUrl = null

@template.layout.main(
    title = pageData.pageTitle(),
    description = pageData.metaDescription(),
    content = @`
    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 2rem 1.5rem;">
        
        <!-- Breadcrumbs -->
        <nav style="margin-bottom: 2rem; font-size: 0.85rem; color: var(--text-tertiary);">
            <a href="/RentVerdict/" style="color: inherit; text-decoration: none;">Home</a> 
            <span style="margin: 0 0.5rem;">/</span> 
            <a href="/RentVerdict/verdict/${pageData.city().toLowerCase().replace(" ", "-")}-${pageData.state().toLowerCase()}" style="color: inherit; text-decoration: none;">${pageData.city()}</a> 
            <span style="margin: 0 0.5rem;">/</span> 
            <span style="color: var(--text-secondary);">${tier} Credit Guide</span>
        </nav>

        <div style="margin-bottom: 4rem;">
            <div style="display: inline-block; background: rgba(220, 38, 38, 0.1); color: #dc2626; padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 700; margin-bottom: 1.5rem;">
                STRATEGY GUIDE: ${tier.toUpperCase()} CREDIT
            </div>
            <h1 style="font-size: 2.75rem; font-weight: 800; margin-bottom: 1.5rem; line-height: 1.1; letter-spacing: -0.02em;">
                Renting in ${pageData.city()} with ${tier} Credit: Approval Odds & Cost Audit
            </h1>
            
            <p style="font-size: 1.25rem; color: var(--text-secondary); line-height: 1.6; margin-bottom: 2rem;">
                In ${pageData.city()}, ${pageData.state()}, a ${tier} credit score doesn't mean an automatic rejection, but it <span style="color: var(--text-primary); font-weight: 600;">doubles your upfront costs.</span> Most landlords in this market require higher security deposits to offset credit risk.
            </p>
        </div>

        <!-- Key Stats for Credit Impact -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1.5rem; margin-bottom: 4rem;">
            <div class="card" style="padding: 1.5rem; border-top: 4px solid #dc2626;">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 0.5rem;">Risk Tier</div>
                <div style="font-size: 1.75rem; font-weight: 700; color: #dc2626; margin-bottom: 0.5rem;">${tier}</div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">Market strictness: High</div>
            </div>
            <div class="card" style="padding: 1.5rem; border-top: 4px solid var(--primary);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 0.5rem;">Est. Deposit</div>
                <div style="font-size: 1.75rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">$${String.format("%,d", (int)(pageData.medianRent() * 2))}</div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">2x rent typical for ${tier} credit</div>
            </div>
            <div class="card" style="padding: 1.5rem; border-top: 4px solid var(--verdict-approved);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 0.5rem;">Total Upfront</div>
                <div style="font-size: 1.75rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">$${String.format("%,d", pageData.totalUpfrontEstimate() + pageData.medianRent())}</div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">Including higher risk deposit</div>
            </div>
        </div>

        <!-- The "Credit Wall" Article Section -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">Navigating the ${pageData.city()} "Credit Wall"</h2>
            <div style="font-size: 1.1rem; line-height: 1.7; color: var(--text-secondary);">
                <p style="margin-bottom: 1.5rem;">
                    Our 2026 market audit shows that ${pageData.city()} landlords are increasingly using algorithmic screening. For applicants with ${tier} credit, this often results in a "Conditional Approval"—meaning you're approved only if you pay a maximum security deposit (often 2 months' rent).
                </p>
                <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; margin-bottom: 1.5rem; border: 1px solid var(--border-subtle);">
                    <h3 style="font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 1rem;">💡 Expert Strategy for ${tier} Credit</h3>
                    <ul style="list-style: none; padding: 0;">
                        <li style="margin-bottom: 1rem; display: flex; gap: 0.75rem;">
                            <span style="color: var(--primary);">01.</span>
                            <span><strong>Target Individual Landlords:</strong> Large corporate complexes in ${pageData.state()} use rigid software. Private owners are more likely to accept a letter of explanation.</span>
                        </li>
                        <li style="margin-bottom: 1rem; display: flex; gap: 0.75rem;">
                            <span style="color: var(--primary);">02.</span>
                            <span><strong>Proof of Savings:</strong> Since your credit is ${tier}, your liquidity is your best friend. Showing $10,000+ in a savings account can negate credit fears.</span>
                        </li>
                        <li style="display: flex; gap: 0.75rem;">
                            <span style="color: var(--primary);">03.</span>
                            <span><strong>The "3.5x Rule":</strong> While 3x income is standard, high-risk candidates should target apartments where their income is 3.5x the rent to appear safer.</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Interactive CTA -->
        <div style="background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%); color: white; border-radius: 24px; padding: 4rem 2rem; text-align: center; margin-bottom: 4rem; box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);">
            <h2 style="font-size: 2rem; font-weight: 800; margin-bottom: 1.5rem; color: white;">Can you actually afford this?</h2>
            <p style="font-size: 1.125rem; color: rgba(255, 255, 255, 0.9); margin-bottom: 2.5rem; max-width: 500px; margin-left: auto; margin-right: auto;">
                Don't guess with your future. Use our real-time simulator to see if your specific cash-on-hand can overcome your ${tier} credit score in ${pageData.city()}.
            </p>
            <a href="/RentVerdict/" class="btn btn-primary btn-lg" style="background: white; color: #0f172a; border: none; padding: 1.25rem 3rem; font-size: 1.25rem; font-weight: 700;">
                Run Approval Test Now
            </a>
            <div style="margin-top: 1.5rem; font-size: 0.875rem; opacity: 0.6;">
                100% Free • No Sign-up • Instant Verdict
            </div>
        </div>

        <!-- Related Cities -->
        <div style="margin-top: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);">
            <h3 style="font-size: 0.9rem; font-weight: 700; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 1.5rem;">Compare with Nearby Cities</h3>
            <div style="display: flex; gap: 0.75rem; flex-wrap: wrap;">
                @for(CityEntry related : relatedCities)
                    <a href="/RentVerdict/verdict/credit/${tier.toLowerCase()}/${related.city().toLowerCase().replace(" ", "-")}-${related.state().toLowerCase()}" 
                       style="text-decoration: none; padding: 0.5rem 1rem; border: 1px solid var(--border-subtle); border-radius: 20px; font-size: 0.875rem; color: var(--text-secondary);">
                        ${related.city()}
                    </a>
                @endfor
            </div>
        </div>
    </div>
`)
