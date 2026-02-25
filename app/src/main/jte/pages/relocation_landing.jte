@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@import firstrentverdict.service.seo.CityContentGenerator.QnA
@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import java.util.List
@param CityPageContent pageData
@param List<CityEntry> relatedCities = java.util.Collections.emptyList()
@param String canonicalUrl = null

@template.layout.main(
    title = pageData.pageTitle(),
    description = pageData.metaDescription(),
    canonical = (canonicalUrl != null ? canonicalUrl : "https://movecostinfo.com/RentVerdict/verdict/moving-to/" + pageData.city().toLowerCase().replace(" ", "-") + "-" + pageData.state().toLowerCase()),
    content = @`
    
    <!-- FAQ Schema -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "FAQPage",
      "mainEntity": [
        @for(int i=0; i < pageData.commonQuestions().size(); i++)
        {
          "@type": "Question",
          "name": "$unsafe{pageData.commonQuestions().get(i).question().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "")}",
          "acceptedAnswer": {
            "@type": "Answer",
            "text": "$unsafe{pageData.commonQuestions().get(i).answer().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "")}"
          }
        }${i < pageData.commonQuestions().size() - 1 ? "," : ""}
        @endfor
      ]
    }
    </script>
    
    <!-- BreadcrumbList Schema -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "BreadcrumbList",
      "itemListElement": [
        {"@type": "ListItem", "position": 1, "name": "Home", "item": "https://movecostinfo.com/RentVerdict/"},
        {"@type": "ListItem", "position": 2, "name": "${pageData.city()}", "item": "https://movecostinfo.com/RentVerdict/verdict/${pageData.city().toLowerCase().replace(" ", "-")}-${pageData.state().toLowerCase()}"},
        {"@type": "ListItem", "position": 3, "name": "Relocation Guide"}
      ]
    }
    </script>
    
    <!-- HowTo Schema -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "HowTo",
      "name": "How to Relocate to ${pageData.city()}, ${pageData.state()} Securely",
      "description": "A step-by-step cost and strategy guide for moving interstate to ${pageData.city()}.",
      "estimatedCost": {
        "@type": "MonetaryAmount",
        "currency": "USD",
        "value": "${pageData.totalUpfrontEstimate() + 1500}"
      },
      "step": [
        {
          "@type": "HowToStep",
          "name": "Budget for the 'Hidden Move Factor'",
          "text": "Calculate your base move-in liquidity: First month's rent ($${String.format("%,d", pageData.medianRent())}), security deposits ($${String.format("%,d", pageData.typicalDeposit())}), and cross-country logistics ($${String.format("%,d", pageData.typicalMoving())})."
        },
        {
          "@type": "HowToStep",
          "name": "Implement the 2-Week Temporary Route",
          "text": "Never sign a lease sight-unseen. Budget for 14 days of temporary housing (e.g., Airbnb) in ${pageData.city()} to tour units in person and avoid long-distance scams."
        },
        {
          "@type": "HowToStep",
          "name": "Prepare Transfer Documents",
          "text": "Local landlords view out-of-state movers as higher risk. Have your offer letter or income transfer documents ready before touring to bypass local income verification barriers."
        }
      ]
    }
    </script>
    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 2rem 1.5rem;">
        
        <!-- Breadcrumbs -->
        <nav style="margin-bottom: 2rem; font-size: 0.85rem; color: var(--text-tertiary);">
            <a href="/RentVerdict/" style="color: inherit; text-decoration: none;">Home</a> 
            <span style="margin: 0 0.5rem;">/</span> 
            <a href="/RentVerdict/verdict/${pageData.city().toLowerCase().replace(" ", "-")}-${pageData.state().toLowerCase()}" style="color: inherit; text-decoration: none;">${pageData.city()}</a> 
            <span style="margin: 0 0.5rem;">/</span> 
            <span style="color: var(--text-secondary);">Relocation Guide</span>
        </nav>

        <div style="margin-bottom: 4rem;">
            <div style="display: inline-flex; align-items: center; gap: 0.75rem; margin-bottom: 1.5rem;">
                <div style="background: rgba(59, 130, 246, 0.1); color: var(--primary); padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 700;">
                    2026 RELOCATION BLUEPRINT
                </div>
                <div style="font-size: 0.8rem; color: var(--text-tertiary); display: flex; align-items: center; gap: 0.25rem;">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"></polyline></svg>
                    Data Verified by Analysts
                </div>
            </div>
            
            <h1 style="font-size: 2.75rem; font-weight: 800; margin-bottom: 1.5rem; line-height: 1.1; letter-spacing: -0.02em;">
                Moving to ${pageData.city()}, ${pageData.state()}: The Essential Cost Audit
            </h1>
            
            <p style="font-size: 1.25rem; color: var(--text-secondary); line-height: 1.6; margin-bottom: 2rem;">
                Relocating to ${pageData.city()} involves more than just rent. Between cross-country movers, utility deposits, and temporary housing, the <span style="color: var(--text-primary); font-weight: 600;">"hidden move factor"</span> can add $2,000+ to your budget.
            </p>
        </div>

        <!-- AEO: Quick Answer Box -->
        <div style="background: var(--bg-surface); padding: 1.5rem 2rem; border-radius: var(--radius-lg); margin-bottom: 4rem; border: 1px solid var(--border-subtle); display: flex; align-items: flex-start; gap: 1rem; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);">
            <div style="font-size: 1.5rem; line-height: 1;">💡</div>
            <div>
                <strong style="display: block; font-size: 0.9rem; color: var(--primary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.5rem;">Quick Answer</strong>
                <p style="font-size: 1.1rem; line-height: 1.6; color: var(--text-primary); font-weight: 500;">
                    A standard interstate relocation to ${pageData.city()} generally costs about <strong style="color: var(--text-primary);">$${String.format("%,d", pageData.totalUpfrontEstimate() + 1500)}</strong> in total liquidity. This estimate accounts for long-distance movers ($${String.format("%,d", pageData.typicalMoving())}), your first month’s rent ($${String.format("%,d", pageData.medianRent())}), security deposits, and a 14-day temporary housing buffer.
                </p>
                <div style="margin-top: 0.75rem; font-size: 0.85rem; color: var(--text-tertiary);">Sources: US Census ACS, Local Rental Surveys</div>
            </div>
        </div>

        <!-- Key Financial Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1.5rem; margin-bottom: 4rem;">
            <div class="card" style="padding: 1.5rem; border-left: 4px solid var(--primary);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 0.5rem;">Est. Moving Cost</div>
                <div style="font-size: 1.75rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">$${String.format("%,d", pageData.typicalMoving())}</div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">Standard 2-bedroom relocation</div>
            </div>
            <div class="card" style="padding: 1.5rem; border-left: 4px solid var(--verdict-approved);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 0.5rem;">Wait Time</div>
                <div style="font-size: 1.75rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">14-21 Days</div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">Avg. apartment search duration</div>
            </div>
            <div class="card" style="padding: 1.5rem; border-left: 4px solid var(--verdict-denied);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 0.5rem;">Total Liquidity</div>
                <div style="font-size: 1.75rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">$${String.format("%,d", pageData.totalUpfrontEstimate() + 1500)}</div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">Including temp housing buffer</div>
            </div>
        </div>

        <!-- Relocation Article Section -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">Avoiding the ${pageData.city()} Relocation Trap</h2>
            <div style="font-size: 1.1rem; line-height: 1.7; color: var(--text-secondary);">
                <p style="margin-bottom: 1.5rem;">
                    Moving out-of-state to ${pageData.city()} means you are a "Cold Lead" to landlords. Without local employment verification or an in-person tour, you face higher scrutiny and a higher risk of rental scams.
                </p>
                <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; margin-bottom: 1.5rem; border: 1px solid var(--border-subtle);">
                    <h3 style="font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 1rem;">📦 Essential Relocation Checklist</h3>
                    <ul style="list-style: none; padding: 0;">
                        <li style="margin-bottom: 1rem; display: flex; gap: 0.75rem;">
                            <span style="font-size: 1.25rem;">🏠</span>
                            <span><strong>The 2-Week Airbnb Rule:</strong> Never sign a lease sight-unseen in ${pageData.city()}. Budget for a short-term rental while you tour actual units.</span>
                        </li>
                        <li style="margin-bottom: 1rem; display: flex; gap: 0.75rem;">
                            <span style="font-size: 1.25rem;">📄</span>
                            <span><strong>Transfer Proof:</strong> Have your offer letter or income transfer documents ready. ${pageData.state()} landlords won't wait for your first local paycheck.</span>
                        </li>
                        <li style="display: flex; gap: 0.75rem;">
                            <span style="font-size: 1.25rem;">💰</span>
                            <span><strong>Utility Deposits:</strong> Many local utilities require a security deposit for new out-of-state residents without a local payment history.</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- CTA Section -->
        <div style="background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); color: white; border-radius: 24px; padding: 4rem 2rem; text-align: center; margin-bottom: 4rem; box-shadow: 0 20px 25px -5px rgba(79, 70, 229, 0.3);">
            <h2 style="font-size: 2.25rem; font-weight: 800; margin-bottom: 1.5rem; color: white;">Is your budget relocation-ready?</h2>
            <p style="font-size: 1.2rem; color: rgba(255, 255, 255, 0.9); margin-bottom: 2.5rem; max-width: 550px; margin-left: auto; margin-right: auto;">
                Moving costs are the #1 reason for post-lease financial stress. Use our simulator to see if your cash buffer is actually enough for ${pageData.city()}.
            </p>
            <a href="/RentVerdict/" class="btn btn-primary btn-lg" style="background: white; color: #4f46e5; border: none; padding: 1.25rem 3rem; font-size: 1.25rem; font-weight: 700;">
                Check My Move-In Costs
            </a>
            <div style="margin-top: 1.5rem; font-size: 0.875rem; opacity: 0.7;">
                Free 2026 Market Data • Accurate Estimates
            </div>
        </div>

        <!-- Related Cities -->
        <div style="margin-top: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);">
            <h3 style="font-size: 0.9rem; font-weight: 700; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 1.5rem;">Other Destinations in ${pageData.state()}</h3>
            <div style="display: flex; gap: 0.75rem; flex-wrap: wrap;">
                @for(CityEntry related : relatedCities)
                    <a href="/RentVerdict/verdict/moving-to/${related.city().toLowerCase().replace(" ", "-")}-${related.state().toLowerCase()}" 
                       style="text-decoration: none; padding: 0.5rem 1rem; border: 1px solid var(--border-subtle); border-radius: 20px; font-size: 0.875rem; color: var(--text-secondary);">
                        ${related.city()}
                    </a>
                @endfor
            </div>
        </div>
    </div>
`)
