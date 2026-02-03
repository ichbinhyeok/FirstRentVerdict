@template.layout.main(
    title = "Methodology: How First Rent Verdict Works (50/30/20 Rule)",
    description = "Learn how we calculate your rent affordability using the 50/30/20 budgeting rule, upfront costs, and cash buffer analysis. Transparent methodology with cited data sources.",
    canonical = "https://movecostinfo.com/RentVerdict/methodology",
    content = @`
    <div style="max-width: 800px; margin: 4rem auto; padding: 0 1.5rem;">
        
        <!-- Header -->
        <div style="text-align: center; margin-bottom: 4rem;">
            <div style="font-size: 0.8rem; letter-spacing: 0.15em; text-transform: uppercase; color: var(--text-tertiary); margin-bottom: 0.5rem;">
                Behind The Verdict
            </div>
            <h1 style="font-size: 2.5rem; font-weight: 800; margin-bottom: 1rem;">Our Methodology</h1>
            <p style="font-size: 1.1rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto; line-height: 1.6;">
                How we determine if you can truly afford that apartment—beyond just the monthly rent.
            </p>
        </div>

        <!-- Section 1: Core Philosophy -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">1. The 50/30/20 Foundation</h2>
            <p style="font-size: 1.1rem; line-height: 1.8; color: var(--text-secondary); margin-bottom: 1.5rem;">
                Our core framework is built on the <strong>50/30/20 budgeting rule</strong>, a widely-accepted personal finance standard:
            </p>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; margin-bottom: 2rem;">
                <div style="background: var(--bg-surface); padding: 1.5rem; border-radius: 12px; border-left: 4px solid #16a34a;">
                    <div style="font-size: 2rem; font-weight: 800; color: #16a34a;">50%</div>
                    <div style="font-weight: 600; margin-bottom: 0.5rem;">Needs</div>
                    <div style="font-size: 0.9rem; color: var(--text-secondary);">Housing, utilities, groceries, insurance</div>
                </div>
                <div style="background: var(--bg-surface); padding: 1.5rem; border-radius: 12px; border-left: 4px solid #ca8a04;">
                    <div style="font-size: 2rem; font-weight: 800; color: #ca8a04;">30%</div>
                    <div style="font-weight: 600; margin-bottom: 0.5rem;">Wants</div>
                    <div style="font-size: 0.9rem; color: var(--text-secondary);">Entertainment, dining, subscriptions</div>
                </div>
                <div style="background: var(--bg-surface); padding: 1.5rem; border-radius: 12px; border-left: 4px solid #3b82f6;">
                    <div style="font-size: 2rem; font-weight: 800; color: #3b82f6;">20%</div>
                    <div style="font-weight: 600; margin-bottom: 0.5rem;">Savings</div>
                    <div style="font-size: 0.9rem; color: var(--text-secondary);">Emergency fund, investments, debt payoff</div>
                </div>
            </div>
            <p style="font-size: 1rem; line-height: 1.7; color: var(--text-secondary);">
                However, most rent calculators only check if your monthly rent fits within 30% of income. 
                <strong>We go further.</strong>
            </p>
        </div>

        <!-- Section 2: Liquidity Over Income -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">2. Liquidity Over Income</h2>
            <div style="background: rgba(220, 38, 38, 0.05); border: 1px solid rgba(220, 38, 38, 0.2); border-radius: 12px; padding: 2rem; margin-bottom: 2rem;">
                <p style="font-size: 1.1rem; line-height: 1.7; color: var(--text-primary); margin: 0;">
                    <strong>The Hidden Danger:</strong> Many renters can afford the <em>monthly</em> rent but go broke on <em>move-in day</em>.
                </p>
            </div>
            <p style="font-size: 1.1rem; line-height: 1.8; color: var(--text-secondary); margin-bottom: 1.5rem;">
                First Rent Verdict focuses on <strong>upfront liquidity</strong>—the cash you need on move-in day—not just your monthly paycheck. Our algorithm calculates:
            </p>
            <div style="background: var(--bg-panel); padding: 2rem; border-radius: 8px; font-family: 'SF Mono', 'Menlo', monospace; font-size: 0.95rem; margin-bottom: 2rem;">
                <div style="margin-bottom: 1rem;"><strong>Total Upfront Cost</strong> = First Month Rent + Security Deposit + Moving Costs + Pet Fees (if applicable)</div>
                <div style="margin-bottom: 1rem;"><strong>Remaining Cash</strong> = Your Available Cash − Total Upfront Cost</div>
                <div style="border-top: 1px dashed var(--border-subtle); padding-top: 1rem; margin-top: 1rem;">
                    <strong>Verdict</strong> = Remaining Cash vs. Recommended Emergency Buffer
                </div>
            </div>
        </div>

        <!-- Section 3: The Three Verdicts -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">3. The Three Verdicts</h2>
            <div style="display: grid; gap: 1.5rem;">
                <div style="background: rgba(22, 163, 74, 0.05); border: 1px solid rgba(22, 163, 74, 0.2); border-radius: 12px; padding: 1.5rem;">
                    <div style="font-size: 1.5rem; font-weight: 800; color: #16a34a; margin-bottom: 0.5rem;">✓ APPROVED</div>
                    <p style="color: var(--text-secondary); margin: 0;">
                        Your remaining cash after move-in <strong>exceeds</strong> the recommended emergency buffer. 
                        You can proceed with confidence.
                    </p>
                </div>
                <div style="background: rgba(202, 138, 4, 0.05); border: 1px solid rgba(202, 138, 4, 0.2); border-radius: 12px; padding: 1.5rem;">
                    <div style="font-size: 1.5rem; font-weight: 800; color: #ca8a04; margin-bottom: 0.5rem;">⚠ BORDERLINE</div>
                    <p style="color: var(--text-secondary); margin: 0;">
                        Your remaining cash is <strong>positive but below</strong> the recommended buffer. 
                        Proceed with caution—unexpected expenses could destabilize you.
                    </p>
                </div>
                <div style="background: rgba(220, 38, 38, 0.05); border: 1px solid rgba(220, 38, 38, 0.2); border-radius: 12px; padding: 1.5rem;">
                    <div style="font-size: 1.5rem; font-weight: 800; color: #dc2626; margin-bottom: 0.5rem;">✕ DENIED</div>
                    <p style="color: var(--text-secondary); margin: 0;">
                        Your remaining cash is <strong>negative or critically low</strong>. 
                        This move would put you at immediate financial risk.
                    </p>
                </div>
            </div>
        </div>

        <!-- Section 4: Data Sources -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">4. Data Sources & Methodology</h2>
            <p style="font-size: 1.1rem; line-height: 1.8; color: var(--text-secondary); margin-bottom: 2rem;">
                Every number in your verdict is backed by real market data. Here's where we get our information:
            </p>
            <div style="display: grid; gap: 1rem;">
                <div style="display: flex; gap: 1rem; align-items: flex-start; padding: 1rem; background: var(--bg-surface); border-radius: 8px;">
                    <div style="font-size: 1.2rem;">🏠</div>
                    <div>
                        <div style="font-weight: 600; margin-bottom: 0.25rem;">Rent Data</div>
                        <div style="font-size: 0.9rem; color: var(--text-secondary);">Zumper National Rent Report, Zillow Observed Rent Index (ZORI), Apartment List Research</div>
                    </div>
                </div>
                <div style="display: flex; gap: 1rem; align-items: flex-start; padding: 1rem; background: var(--bg-surface); border-radius: 8px;">
                    <div style="font-size: 1.2rem;">🔒</div>
                    <div>
                        <div style="font-weight: 600; margin-bottom: 0.25rem;">Security Deposit Laws</div>
                        <div style="font-size: 0.9rem; color: var(--text-secondary);">State housing authority databases, Nolo Legal Encyclopedia, individual city ordinances</div>
                    </div>
                </div>
                <div style="display: flex; gap: 1rem; align-items: flex-start; padding: 1rem; background: var(--bg-surface); border-radius: 8px;">
                    <div style="font-size: 1.2rem;">📦</div>
                    <div>
                        <div style="font-weight: 600; margin-bottom: 0.25rem;">Moving Costs</div>
                        <div style="font-size: 0.9rem; color: var(--text-secondary);">American Moving & Storage Association, HomeAdvisor, regional cost-of-living indices</div>
                    </div>
                </div>
                <div style="display: flex; gap: 1rem; align-items: flex-start; padding: 1rem; background: var(--bg-surface); border-radius: 8px;">
                    <div style="font-size: 1.2rem;">💰</div>
                    <div>
                        <div style="font-weight: 600; margin-bottom: 0.25rem;">Emergency Buffer</div>
                        <div style="font-size: 0.9rem; color: var(--text-secondary);">MIT Living Wage Calculator, BLS Consumer Expenditure Survey, 1.5x monthly non-rent essentials</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Section 5: Who Is This For -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">5. Who Is This For?</h2>
            <p style="font-size: 1.1rem; line-height: 1.8; color: var(--text-secondary); margin-bottom: 1.5rem;">
                First Rent Verdict is designed for:
            </p>
            <ul style="list-style: none; padding: 0;">
                <li style="display: flex; gap: 0.75rem; margin-bottom: 1rem; color: var(--text-secondary);">
                    <span style="color: #16a34a;">✓</span> First-time renters moving out of parents' home
                </li>
                <li style="display: flex; gap: 0.75rem; margin-bottom: 1rem; color: var(--text-secondary);">
                    <span style="color: #16a34a;">✓</span> Recent graduates starting their first job in a new city
                </li>
                <li style="display: flex; gap: 0.75rem; margin-bottom: 1rem; color: var(--text-secondary);">
                    <span style="color: #16a34a;">✓</span> Anyone relocating and unsure about upfront move-in costs
                </li>
                <li style="display: flex; gap: 0.75rem; margin-bottom: 1rem; color: var(--text-secondary);">
                    <span style="color: #16a34a;">✓</span> Parents helping their children plan a safe first apartment
                </li>
            </ul>
        </div>

        <!-- Disclaimer -->
        <div style="background: var(--bg-panel); padding: 2rem; border-radius: 12px; margin-bottom: 4rem;">
            <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1rem;">⚠️ Important Disclaimer</h3>
            <p style="font-size: 0.95rem; line-height: 1.7; color: var(--text-secondary); margin: 0;">
                First Rent Verdict provides <strong>estimates</strong> based on 2026 market averages. 
                Actual costs vary by landlord, property, and individual circumstances. 
                This tool is for informational purposes only and does not constitute financial, legal, or real estate advice. 
                Always verify specific terms with your landlord before signing a lease.
            </p>
        </div>

        <!-- CTA -->
        <div style="text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 3rem;">
            <a href="/RentVerdict/" class="btn-primary" style="display: inline-block; padding: 1rem 2rem;">
                Try the Calculator →
            </a>
        </div>
    </div>
`)
