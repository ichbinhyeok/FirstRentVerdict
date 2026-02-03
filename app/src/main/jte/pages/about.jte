@template.layout.main(
    title = "About Verdict Labs - First Rent Verdict Team",
    description = "Meet Verdict Labs, the data analysts behind First Rent Verdict. We use objective market data to protect renters from financial risk.",
    canonical = "https://movecostinfo.com/RentVerdict/about",
    content = @`
    <div style="max-width: 800px; margin: 4rem auto; padding: 0 1.5rem;">
        
        <!-- Header -->
        <div style="text-align: center; margin-bottom: 4rem;">
            <div style="font-size: 0.8rem; letter-spacing: 0.15em; text-transform: uppercase; color: var(--text-tertiary); margin-bottom: 0.5rem;">
                The Team Behind The Verdict
            </div>
            <h1 style="font-size: 2.5rem; font-weight: 800; margin-bottom: 1rem;">About Verdict Labs</h1>
            <p style="font-size: 1.2rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto; line-height: 1.6;">
                We are data analysts and engineers dedicated to solving the rental affordability crisis through mathematical transparency.
            </p>
        </div>

        <!-- Mission -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">Our Mission</h2>
            <p style="font-size: 1.1rem; line-height: 1.8; color: var(--text-secondary); margin-bottom: 1.5rem;">
                <strong>First Rent Verdict</strong> was built with a single goal: to protect tenants from financial insolvency. 
            </p>
            <p style="font-size: 1.1rem; line-height: 1.8; color: var(--text-secondary);">
                Too many renters sign leases based on "monthly rent" alone, ignoring the hidden dangers of upfront liquidity requirements (security deposits, moving costs) and post-move cash buffers. We exist to visualize these hidden risks before you sign.
            </p>
        </div>

        <!-- Data Sources (E-E-A-T Authority) -->
        <div style="background: var(--bg-surface); padding: 2.5rem; border-radius: 12px; border: 1px solid var(--border-subtle); margin-bottom: 4rem;">
            <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 1.5rem;">Data Integrity & Sources</h2>
            <p style="font-size: 1rem; color: var(--text-secondary); margin-bottom: 1.5rem;">
                Our verdicts are not opinions. They are calculated using real-time market data aggregated from trusted public and private sources:
            </p>
            <ul style="list-style: none; padding: 0; display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
                <li style="display: flex; align-items: center; gap: 0.5rem; color: var(--text-primary); font-weight: 500;">
                    <span style="color: var(--primary);">✓</span> <a href="https://www.zillow.com/research/data/" target="_blank" rel="nofollow" style="color: inherit; text-decoration: underline; text-decoration-color: var(--border-subtle);">Zillow Research Data</a>
                </li>
                <li style="display: flex; align-items: center; gap: 0.5rem; color: var(--text-primary); font-weight: 500;">
                    <span style="color: var(--primary);">✓</span> <a href="https://www.apartmentlist.com/research/category/data-rent-estimates" target="_blank" rel="nofollow" style="color: inherit; text-decoration: underline; text-decoration-color: var(--border-subtle);">Apartment List Trends</a>
                </li>
                <li style="display: flex; align-items: center; gap: 0.5rem; color: var(--text-primary); font-weight: 500;">
                    <span style="color: var(--primary);">✓</span> <a href="https://www.census.gov/topics/housing/data.html" target="_blank" rel="nofollow" style="color: inherit; text-decoration: underline; text-decoration-color: var(--border-subtle);">US Census Bureau</a>
                </li>
                <li style="display: flex; align-items: center; gap: 0.5rem; color: var(--text-primary); font-weight: 500;">
                    <span style="color: var(--primary);">✓</span> State Housing Authorities
                </li>
            </ul>
        </div>

        <!-- Contact & Team -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">Contact Us</h2>
            <p style="font-size: 1.1rem; line-height: 1.8; color: var(--text-secondary); margin-bottom: 2rem;">
                Feedback, data correction requests, or partnership inquiries? We'd love to hear from you.
            </p>
            
            <div style="display: flex; flex-direction: column; gap: 1rem;">
                <div style="display: flex; align-items: center; gap: 1rem;">
                    <div style="font-weight: 600; min-width: 100px;">Email</div>
                    <a href="mailto:spainhyeok@gmail.com" style="color: var(--primary);">spainhyeok@gmail.com</a>
                </div>
                <div style="display: flex; align-items: center; gap: 1rem;">
                    <div style="font-weight: 600; min-width: 100px;">Location</div>
                    <div style="color: var(--text-secondary);">Seoul, Republic of Korea & Global Remote Team</div>
                </div>
            </div>
        </div>

        <div style="margin-top: 4rem; text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 2rem;">
            <p style="font-size: 0.9rem; color: var(--text-tertiary);">
                <strong>Disclaimer:</strong> Verdict Labs is an independent analytical entity. We are not a lender, broker, or financial advisor.
            </p>
            <div style="margin-top: 2rem;">
                <a href="/RentVerdict/" class="btn-primary">Back to Calculator</a>
            </div>
        </div>
    </div>
`)
