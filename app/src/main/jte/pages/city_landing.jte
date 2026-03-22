@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@import firstrentverdict.service.seo.CityContentGenerator.QnA
@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import java.util.List
@param CityPageContent pageData
@param List<CityEntry> relatedCities = java.util.Collections.emptyList()
@param String fromState = null
@param List<String> topOriginStates = java.util.Collections.emptyList()
@param Integer savings = null
@param String canonicalUrl = null
@param boolean noindex = false
@param String baseUrl = ""

@template.layout.main(
    title = pageData.pageTitle(),
    description = pageData.metaDescription(),
    canonical = (canonicalUrl != null ? canonicalUrl : baseUrl + "/RentVerdict/verdict/" + pageData.city().toLowerCase().replace(" ", "-").replace(".", "") + "-" + pageData.state().toLowerCase()),
    noindex = noindex,
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
        {"@type": "ListItem", "position": 1, "name": "Home", "item": "${baseUrl}/RentVerdict/"},
        {"@type": "ListItem", "position": 2, "name": "Cities", "item": "${baseUrl}/RentVerdict/cities"},
        {"@type": "ListItem", "position": 3, "name": "${pageData.city()}, ${pageData.state()}"}
      ]
    }
    </script>
    
    @template.components.mobile_cta_bar(baseUrl = baseUrl)

    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 3rem 1.5rem;">
        
        <!-- Breadcrumbs -->
        <nav style="margin-bottom: 2.5rem; font-size: 0.825rem; color: var(--text-tertiary); display: flex; gap: 0.5rem; align-items: center;">
            <a href="/RentVerdict/" style="color: inherit;">Home</a> 
            <span>/</span> 
            <a href="/RentVerdict/cities" style="color: inherit;">Cities</a> 
            <span>/</span> 
            <span style="color: var(--text-secondary); font-weight: 500;">${pageData.city()}, ${pageData.state()}</span>
        </nav>

        <!-- Hero Section: Premium Contextual Header -->
        <header style="margin-bottom: 5rem; text-align: left;">
            <div style="display: flex; gap: 0.75rem; margin-bottom: 1.5rem;">
                <span class="benefit-pill" style="border-color: var(--accent-primary); color: var(--accent-primary);">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                    2026 AUDITED DATA
                </span>
                @if(pageData.affordabilityTier().equals("Affordable"))
                    <span class="benefit-pill" style="border-color: var(--success); color: var(--success);">AFFORDABLE MARKET</span>
                @endif
            </div>

            <h1 style="font-size: clamp(2rem, 5vw, 3.25rem); font-weight: 800; line-height: 1.1; margin-bottom: 1.5rem; letter-spacing: -0.03em;">
                <span class="gradient-text">${pageData.pageTitle()}</span>
            </h1>

            <p style="font-size: 1.25rem; color: var(--text-secondary); line-height: 1.6; max-width: 750px;">
                ${pageData.marketContextText()}
            </p>
        </header>

        <!-- AEO: Quick Answer Box -->
        <div style="background: var(--bg-surface); padding: 1.5rem 2rem; border-radius: var(--radius-lg); margin-bottom: 4rem; border: 1px solid var(--border-subtle); display: flex; align-items: flex-start; gap: 1rem; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);">
            <div style="font-size: 1.5rem; line-height: 1;">Tip</div>
            <div>
                <strong style="display: block; font-size: 0.9rem; color: var(--accent-primary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.5rem;">Quick Answer</strong>
                <p style="font-size: 1.1rem; line-height: 1.6; color: var(--text-primary); font-weight: 500;">
                    To securely move into a median-priced apartment in ${pageData.city()}, ${pageData.state()}, you typically need <strong style="color: var(--text-primary);">$${String.format("%,d", pageData.totalUpfrontEstimate())}</strong> upfront. This consists of the first month's rent ($${String.format("%,d", pageData.medianRent())}), a standard security deposit ($${String.format("%,d", pageData.typicalDeposit())}), and estimated moving logistics ($${String.format("%,d", pageData.typicalMoving())}).
                </p>
            </div>
        </div>

        <section style="margin-bottom: 4rem; background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: var(--radius-lg); padding: 1.5rem 1.75rem;">
            <h2 style="font-size: 1.2rem; margin-bottom: 1rem; color: var(--text-primary);">Quick Cost Facts: ${pageData.city()}, ${pageData.state()}</h2>
            <ul style="list-style: none; padding: 0; margin: 0; display: grid; gap: 0.75rem;">
                <li><strong>Average 1BR rent:</strong> $${String.format("%,d", pageData.medianRent())} per month.</li>
                <li><strong>Typical move-in cash:</strong> around $${String.format("%,d", pageData.totalUpfrontEstimate())} (rent + deposit + moving).</li>
                <li><strong>Target gross income:</strong> about $${String.format("%,d", pageData.monthlyIncomeReq())}/month for standard 3x rent screening.</li>
            </ul>
        </section>

        <!-- Specialized Intent Blocks -->
        @if(savings != null)
            <div class="glass-morphism" style="padding: 2rem; border-radius: var(--radius-lg); margin-bottom: 4rem; border-left: 5px solid var(--accent-primary);">
                <h3 style="margin-bottom: 1rem;">Savings Analysis for ${pageData.city()}</h3>
                <p style="font-size: 1.1rem; color: var(--text-secondary);">${pageData.localInsight()}</p>
            </div>
        @elseif(pageData.pageTitle().contains("Pet"))
            <div class="glass-morphism" style="padding: 2rem; border-radius: var(--radius-lg); margin-bottom: 4rem; border-left: 5px solid #f59e0b;">
                <h3 style="margin-bottom: 1rem;">Pet-Friendly Policy Audit</h3>
                <p style="font-size: 1.1rem; color: var(--text-secondary);">${pageData.localInsight()}</p>
                <div style="margin-top: 1rem; font-size: 0.9rem; font-style: italic; color: var(--text-tertiary);">* Based on typical private and corporate rental practices in ${pageData.city()}.</div>
            </div>
        @endif

        <!-- Financial Summary Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1.5rem; margin-bottom: 5rem;">
            <div class="card glass-morphism" style="border: none; padding: 2rem;">
                <div class="financial-label">Median Rent</div>
                <div class="financial-value" style="font-size: 2.5rem;">$${String.format("%,d", pageData.medianRent())}</div>
                <div class="financial-value-sm">p25: $${String.format("%,d", pageData.rentLow())} | p75: $${String.format("%,d", pageData.rentHigh())}</div>
            </div>

            <div class="card" style="background: var(--text-primary); border: none; padding: 2rem; color: white;">
                <div class="financial-label" style="color: rgba(255,255,255,0.6);">Total Move-in Cash</div>
                <div class="financial-value" style="color: white; font-size: 2.5rem;">$${String.format("%,d", pageData.totalUpfrontEstimate())}</div>
                <div class="financial-value-sm" style="color: rgba(255,255,255,0.8);">First + Deposit + Movers</div>
            </div>
        </div>

        @if(pageData.medianHouseholdIncome() != null)
            <section style="margin-bottom: 5rem; background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: 16px; padding: 2rem;">
                <h2 style="font-size: 1.6rem; margin-bottom: 1.25rem;">Economic Reality Check for ${pageData.city()}</h2>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem;">
                    <div style="padding: 1rem; background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 10px;">
                        <div style="font-size: 0.78rem; color: var(--text-tertiary); text-transform: uppercase;">Median Income</div>
                        <div style="font-size: 1.4rem; font-weight: 800; color: var(--text-primary);">$${String.format("%,d", pageData.medianHouseholdIncome())}</div>
                    </div>
                    @if(pageData.annualRentToIncomePct() != null)
                    <div style="padding: 1rem; background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 10px;">
                        <div style="font-size: 0.78rem; color: var(--text-tertiary); text-transform: uppercase;">Annual Rent / Income</div>
                        <div style="font-size: 1.4rem; font-weight: 800; color: var(--text-primary);">${String.format("%.1f", pageData.annualRentToIncomePct())}%</div>
                    </div>
                    @endif
                    @if(pageData.rentBurdenSharePct() != null)
                    <div style="padding: 1rem; background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 10px;">
                        <div style="font-size: 0.78rem; color: var(--text-tertiary); text-transform: uppercase;">Rent-Burdened Renters (35%+)</div>
                        <div style="font-size: 1.4rem; font-weight: 800; color: var(--text-primary);">${String.format("%.1f", pageData.rentBurdenSharePct())}%</div>
                    </div>
                    @endif
                    @if(pageData.renterSharePct() != null)
                    <div style="padding: 1rem; background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 10px;">
                        <div style="font-size: 0.78rem; color: var(--text-tertiary); text-transform: uppercase;">Renter Share</div>
                        <div style="font-size: 1.4rem; font-weight: 800; color: var(--text-primary);">${String.format("%.1f", pageData.renterSharePct())}%</div>
                    </div>
                    @endif
                </div>
                <div style="margin-top: 1rem; color: var(--text-secondary); font-size: 0.95rem;">
                    Pressure signal: <strong>${pageData.economicSignal().toUpperCase()}</strong> (source: US Census ACS place-level data).
                </div>
            </section>
        @endif

        @if(fromState != null && topOriginStates != null && !topOriginStates.isEmpty())
            <section style="margin-bottom: 5rem; background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 16px; padding: 1.25rem 1.5rem;">
                <div style="font-size: 0.8rem; font-weight: 700; text-transform: uppercase; color: var(--text-tertiary); margin-bottom: 0.75rem;">
                    IRS Corridor Check (${fromState} -> ${pageData.state()})
                </div>
                <div style="display: flex; flex-wrap: wrap; gap: 0.5rem; margin-bottom: 0.75rem;">
                    @for(String stateCode : topOriginStates)
                        <span class="benefit-pill">${stateCode}</span>
                    @endfor
                </div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">
                    @if(topOriginStates.contains(fromState.toUpperCase()))
                        Your origin state (<strong>${fromState.toUpperCase()}</strong>) is one of the top inbound corridors for ${pageData.state()}.
                    @else
                        Your origin state (<strong>${fromState.toUpperCase()}</strong>) is not in the top inbound corridors, so compare local screening norms more carefully.
                    @endif
                </div>
            </section>
        @endif

        <!-- The "Liquidity Wall" Visual Breakdown -->
        <section style="margin-bottom: 6rem;">
            <h2 style="font-size: 2rem; margin-bottom: 2rem;">Itemized Move-in Invoice</h2>
            @template.components.invoice_receipt(pageData = pageData)
        </section>

        <!-- Expert Strategy & Local Tips -->
        <section style="margin-bottom: 6rem; display: grid; grid-template-columns: repeat(auto-fit, minmax(350px, 1fr)); gap: 2rem;">
            <div style="background: var(--bg-panel); padding: 2.5rem; border-radius: 16px; border: 1px solid var(--border-subtle);">
                <div style="color: var(--accent-primary); font-weight: 800; font-size: 0.75rem; text-transform: uppercase; margin-bottom: 1rem;">Expert Strategy</div>
                <h3 style="font-size: 1.5rem; margin-bottom: 1.5rem;">The ${pageData.city()} Renter's Blueprint</h3>
                <p style="line-height: 1.7; color: var(--text-secondary); margin-bottom: 1.5rem;">${pageData.renterAdvice()}</p>
                <div style="padding: 1rem; background: var(--bg-secondary); border-radius: 8px; font-size: 0.9rem; color: var(--text-primary);">
                    <strong>Timing Tip:</strong> ${pageData.seasonalTip()}
                </div>
            </div>
            <div style="background: var(--bg-panel); padding: 2.5rem; border-radius: 16px; border: 1px solid var(--border-subtle);">
                <div style="color: #10b981; font-weight: 800; font-size: 0.75rem; text-transform: uppercase; margin-bottom: 1rem;">Local Protections</div>
                <h3 style="font-size: 1.5rem; margin-bottom: 1.5rem;">Tenant Rights & Law</h3>
                <p style="line-height: 1.7; color: var(--text-secondary); margin-bottom: 1.5rem;">${pageData.legalSummary()}</p>
                <div style="padding: 1rem; background: rgba(16, 185, 129, 0.1); border-radius: 8px; font-size: 0.9rem; color: #059669;">
                    <strong>Regulatory Note:</strong> Local ordinances in ${pageData.state()} may cap security deposits to 1-2 months' rent.
                </div>
            </div>
        </section>

        <!-- Premium CTA Card -->
        <section style="margin-bottom: 6rem;">
            <div style="background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%); padding: 4rem 2rem; border-radius: var(--radius-xl); text-align: center; color: white; box-shadow: 0 40px 60px -15px rgba(0,0,0,0.3);">
                <div class="badge-accent" style="margin-bottom: 1.5rem; background: rgba(99, 102, 241, 0.2); color: #818cf8; border: 1px solid rgba(99, 102, 241, 0.4);">THE VERDICT ENGINE</div>
                <h2 style="font-size: 2.25rem; font-weight: 800; color: white; margin-bottom: 1.5rem; letter-spacing: -0.02em;">Should you move to ${pageData.city()}?</h2>
                <p style="font-size: 1.15rem; color: rgba(255,255,255,0.7); max-width: 600px; margin: 0 auto 3rem; line-height: 1.6;">
                    Analyzing $${String.format("%,d", pageData.medianRent())} rent is just the start. Our simulator checks your actual savings vs. regional liquidity risks to give you a definitive Approved or Denied judgment.
                </p>
                <div style="display: flex; flex-direction: column; align-items: center; gap: 1.5rem;">
                    <a href="/RentVerdict/" class="btn btn-primary btn-lg" onclick="gtag('event', 'cta_click_audit', {'event_category': 'acquisition', 'page_type': 'city_landing'});" style="padding: 1.25rem 3.5rem; font-size: 1.25rem; font-weight: 800; width: 100%; max-width: 420px; box-shadow: 0 20px 40px rgba(99, 102, 241, 0.4);">
                        AUDIT MY POTENTIAL
                    </a>
                    <div style="display: flex; gap: 2rem; color: rgba(255,255,255,0.5); font-size: 0.85rem; font-weight: 500;">
                        <span>- FREE ANALYSIS</span>
                        <span>- 100% PRIVATE</span>
                        <span>- NO SIGN-UP</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- QnA Section -->
        @template.components.faq_section(questions = pageData.commonQuestions())

        <!-- Explore Specialized Scenarios (Internal Link Silo) -->
        <section style="margin-bottom: 4rem; padding-top: 2rem;">
            <h3 style="font-size: 1.1rem; font-weight: 700; margin-bottom: 1.5rem; color: var(--text-primary);">Explore Specialized Scenarios</h3>
            <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                <a href="/RentVerdict/verdict/can-i-move-with/5000/to/${pageData.city().toLowerCase().replace(" ", "-").replace(".", "")}-${pageData.state().toLowerCase()}" style="padding: 1rem; border: 1px solid var(--border-subtle); border-radius: 8px; text-decoration: none; color: var(--text-primary); font-weight: 500; display: flex; justify-content: space-between; align-items: center; background: var(--bg-surface); transition: border-color 0.2s ease;">
                    <span>Can I move to ${pageData.city()} with $5,000?</span>
                    <span style="color: var(--accent-primary);">&#10003;</span>
                </a>
                <a href="/RentVerdict/verdict/credit/poor/${pageData.city().toLowerCase().replace(" ", "-").replace(".", "")}-${pageData.state().toLowerCase()}" style="padding: 1rem; border: 1px solid var(--border-subtle); border-radius: 8px; text-decoration: none; color: var(--text-primary); font-weight: 500; display: flex; justify-content: space-between; align-items: center; background: var(--bg-surface); transition: border-color 0.2s ease;">
                    <span>Renting in ${pageData.city()} with poor credit</span>
                    <span style="color: var(--accent-primary);">&#10003;</span>
                </a>
                <a href="/RentVerdict/verdict/with-pet/${pageData.city().toLowerCase().replace(" ", "-").replace(".", "")}-${pageData.state().toLowerCase()}" style="padding: 1rem; border: 1px solid var(--border-subtle); border-radius: 8px; text-decoration: none; color: var(--text-primary); font-weight: 500; display: flex; justify-content: space-between; align-items: center; background: var(--bg-surface); transition: border-color 0.2s ease;">
                    <span>Moving to ${pageData.city()} with pets</span>
                    <span style="color: var(--accent-primary);">&#10003;</span>
                </a>
            </div>
        </section>

        @template.components.internal_links(
            city = pageData.city(),
            state = pageData.state(),
            relatedCities = relatedCities,
            intentCategory = "cost",
            baseUrl = baseUrl
        )

    </div>
`
)


