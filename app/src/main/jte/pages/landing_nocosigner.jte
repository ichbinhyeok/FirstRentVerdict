@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@import firstrentverdict.service.seo.CityContentGenerator.QnA
@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import java.util.List
@param CityPageContent pageData
@param List<CityEntry> relatedCities = java.util.Collections.emptyList()
@param Integer savings = null
@param String canonicalUrl = null
@param String baseUrl = ""

@template.layout.main(
    title = pageData.pageTitle(),
    description = pageData.metaDescription(),
    canonical = (canonicalUrl != null ? canonicalUrl : baseUrl + "/RentVerdict/verdict/" + pageData.city().toLowerCase().replace(" ", "-").replace(".", "") + "-" + pageData.state().toLowerCase()),
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
                    <a href="/RentVerdict/" class="btn btn-primary btn-lg" style="padding: 1.25rem 3.5rem; font-size: 1.25rem; font-weight: 800; width: 100%; max-width: 420px; box-shadow: 0 20px 40px rgba(99, 102, 241, 0.4);">
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

        @template.components.internal_links(
            city = pageData.city(),
            state = pageData.state(),
            relatedCities = relatedCities,
            intentCategory = "approval",
            baseUrl = baseUrl
        )

    </div>
`
)


