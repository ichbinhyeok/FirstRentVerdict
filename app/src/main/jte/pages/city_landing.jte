@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@import firstrentverdict.service.seo.CityContentGenerator.QnA
@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import java.util.List
@param CityPageContent pageData
@param List<CityEntry> relatedCities = java.util.Collections.emptyList()

@template.layout.main(
    title = pageData.pageTitle(),
    description = pageData.metaDescription(),
    canonical = "https://movecostinfo.com/RentVerdict/verdict/" + pageData.city().toLowerCase().replace(" ", "-") + "-" + pageData.state().toLowerCase(),
    content = @`
    <!-- JSON-LD FAQ Schema -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "FAQPage",
      "mainEntity": [

        @for(int i = 0; i < pageData.commonQuestions().size(); i++)
          {
            "@type": "Question",
            "name": "${pageData.commonQuestions().get(i).question()}",
            "acceptedAnswer": {
              "@type": "Answer",
              "text": "${pageData.commonQuestions().get(i).answer()}"
            }
          }${i < pageData.commonQuestions().size() - 1 ? "," : ""}
        @endfor
      ]
    }
    </script>

    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 2rem 1.5rem;">
        
        <!-- Breadcrumbs (SEO & Navigation) -->
        <nav style="margin-bottom: 2rem; font-size: 0.85rem; color: var(--text-tertiary);">
            <a href="/RentVerdict/" style="color: inherit; text-decoration: none; transition: color 0.2s;">Home</a> 
            <span style="margin: 0 0.5rem;">/</span> 
            <a href="/RentVerdict/cities" style="color: inherit; text-decoration: none; transition: color 0.2s;">Cities</a> 
            <span style="margin: 0 0.5rem;">/</span> 
            <span style="color: var(--text-secondary);">${pageData.city()}, ${pageData.state()}</span>
        </nav>

        <!-- JSON-LD Breadcrumb Schema -->
        <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [{
            "@type": "ListItem",
            "position": 1,
            "name": "Home",
            "item": "https://movecostinfo.com/RentVerdict/"
          },{
            "@type": "ListItem",
            "position": 2,
            "name": "Cities",
            "item": "https://movecostinfo.com/RentVerdict/cities"
          },{
            "@type": "ListItem",
            "position": 3,
            "name": "${pageData.city()}, ${pageData.state()}",
            "item": "https://movecostinfo.com/RentVerdict/verdict/${pageData.city().toLowerCase().replace(" ", "-")}-${pageData.state().toLowerCase()}"
          }]
        }
        </script>
        <div style="text-align: center; margin-bottom: 4rem;">
            <div class="badge-subtle" style="margin-bottom: 1rem;">2026 Rental Market Audit</div>
            <h1 style="font-size: 2.5rem; font-weight: 800; margin-bottom: 1rem; line-height: 1.2;">
                ${pageData.pageTitle()}
            </h1>
            
            <!-- Affordability Tier Badge (Thin Content Prevention) -->
            <div style="display: flex; justify-content: center; gap: 1rem; margin-bottom: 1.5rem; flex-wrap: wrap;">
                @if(pageData.affordabilityTier().equals("High-Cost"))
                    <span style="background: rgba(220, 38, 38, 0.1); color: #dc2626; padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600;">⚠️ High-Cost Market</span>
                @elseif(pageData.affordabilityTier().equals("Affordable"))
                    <span style="background: rgba(22, 163, 74, 0.1); color: #16a34a; padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600;">✓ Affordable Market</span>
                @else
                    <span style="background: rgba(202, 138, 4, 0.1); color: #ca8a04; padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600;">● Moderate Market</span>
                @endif
                
                @if(pageData.nationalComparison() >= 0)
                    <span style="background: var(--bg-surface); padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.85rem; color: var(--text-secondary);">
                        +${pageData.nationalComparison()}% vs National Avg
                    </span>
                @else
                    <span style="background: var(--bg-surface); padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.85rem; color: var(--text-secondary);">
                        ${pageData.nationalComparison()}% vs National Avg
                    </span>
                @endif
            </div>
            
            <p style="font-size: 1.2rem; color: var(--text-secondary); max-width: 700px; margin: 0 auto; margin-bottom: 2rem;">
                ${pageData.marketContextText()}
            </p>

            <div style="margin-top: 2.5rem;">
                <a href="/RentVerdict/" class="btn btn-primary btn-lg" style="text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem; box-shadow: 0 10px 15px -3px rgba(59, 130, 246, 0.3);">
                    <span>Run Personal Affordability Test</span>
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>
                </a>
                <div style="margin-top: 1rem; font-size: 0.85rem; color: var(--text-tertiary);">
                    <span style="color: var(--success);">●</span> Live 2026 ${pageData.city()} Data Loaded
                </div>
            </div>
        </div>

        <!-- Key Financial Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1.5rem; margin-bottom: 4rem;">
            <!-- Rent Card -->
            <div class="card" style="padding: 1.5rem; border-left: 4px solid var(--primary);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.5rem;">Median Rent</div>
                <div style="font-size: 2rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">
                    $${String.format("%,d", pageData.medianRent())}
                </div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">
                    Range: $${String.format("%,d", pageData.rentLow())} - $${String.format("%,d", pageData.rentHigh())}
                </div>
            </div>

            <!-- Income Card -->
            <div class="card" style="padding: 1.5rem; border-left: 4px solid var(--verdict-approved);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.5rem;">Income Goal (3x)</div>
                <div style="font-size: 2rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">
                    $${String.format("%,d", pageData.yearlyIncomeReq())}
                </div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">
                    Per Year / ~$${String.format("%,d", pageData.monthlyIncomeReq())} Mo
                </div>
            </div>

            <!-- Cash Card -->
            <div class="card" style="padding: 1.5rem; border-left: 4px solid var(--verdict-denied);">
                <div style="font-size: 0.9rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.5rem;">Est. Move-in Cash</div>
                <div style="font-size: 2rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">
                    $${String.format("%,d", pageData.totalUpfrontEstimate())}
                </div>
                <div style="font-size: 0.9rem; color: var(--text-secondary);">
                    Rent + Deposit + Moving
                </div>
            </div>
        </div>

        <!-- Cost Breakdown Table (SEO Content Depth) -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1.5rem;">Total Move-in Cost Breakdown</h2>
            <p style="margin-bottom: 1.5rem; color: var(--text-secondary);">
                Many first-time renters in ${pageData.city()} focus only on the monthly rent, missing the "Liquidity Wall"—the upfront cash required to get the keys.
            </p>
            <div style="overflow-x: auto;">
                <table style="width: 100%; border-collapse: collapse; min-width: 600px;">
                    <thead>
                        <tr style="border-bottom: 2px solid var(--border-subtle); text-align: left;">
                            <th style="padding: 1rem; color: var(--text-secondary);">Expense Item</th>
                            <th style="padding: 1rem; color: var(--text-secondary);">Low Est.</th>
                            <th style="padding: 1rem; color: var(--text-secondary);">Typical</th>
                            <th style="padding: 1rem; color: var(--text-secondary);">High Est.</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr style="border-bottom: 1px solid var(--border-subtle);">
                            <td style="padding: 1rem; font-weight: 600;">First Month Rent</td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.rentLow())}</td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.medianRent())}</td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.rentHigh())}</td>
                        </tr>
                        <tr style="border-bottom: 1px solid var(--border-subtle);">
                            <td style="padding: 1rem; font-weight: 600;">Security Deposit <span style="font-size: 0.8rem; font-weight: normal; color: var(--text-tertiary); display: block;">${pageData.depositNotes()}</span></td>
                            <td style="padding: 1rem;">$${String.format("%,d", (int)(pageData.rentLow() * 0.5))}</td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.typicalDeposit())}</td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.rentHigh() * 2)}</td>
                        </tr>
                        <tr style="border-bottom: 1px solid var(--border-subtle);">
                            <td style="padding: 1rem; font-weight: 600;">Moving Costs <span style="font-size: 0.8rem; font-weight: normal; color: var(--text-tertiary); display: block;">${pageData.movingAssumptions()}</span></td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.movingLow())}</td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.typicalMoving())}</td>
                            <td style="padding: 1rem;">$${String.format("%,d", pageData.movingHigh())}</td>
                        </tr>
                        <tr style="background: var(--bg-surface);">
                            <td style="padding: 1rem; font-weight: 700;">Est. Total Cash Needed</td>
                            <td style="padding: 1rem; font-weight: 700;">$${String.format("%,d", pageData.rentLow() + (int)(pageData.rentLow() * 0.5) + pageData.movingLow())}</td>
                            <td style="padding: 1rem; font-weight: 700; color: var(--primary);">$${String.format("%,d", pageData.totalUpfrontEstimate())}</td>
                            <td style="padding: 1rem; font-weight: 700;">$${String.format("%,d", pageData.rentHigh() * 3 + pageData.movingHigh())}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Risks Section -->
        <div style="margin-bottom: 4rem; padding: 2.5rem; background: var(--bg-surface); border-radius: 12px; border: 1px solid var(--border-subtle);">
            <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 1rem; color: var(--verdict-denied);">
                ⚠️ ${pageData.preVerdictHeadline()}
            </h2>
            <p style="font-size: 1.1rem; line-height: 1.7; color: var(--text-secondary);">
                ${pageData.riskNarrative()}
            </p>
        </div>

        <!-- Local Insight Section (Thin Content Prevention) -->
        <div style="margin-bottom: 4rem; padding: 2rem; background: linear-gradient(135deg, rgba(59, 130, 246, 0.05) 0%, rgba(147, 51, 234, 0.05) 100%); border-radius: 12px; border: 1px solid rgba(59, 130, 246, 0.1);">
            <h3 style="font-size: 1.1rem; font-weight: 700; margin-bottom: 0.75rem; color: var(--text-primary);">
                📍 Local Insight: ${pageData.city()}, ${pageData.state()}
            </h3>
            <p style="font-size: 1rem; line-height: 1.7; color: var(--text-secondary); margin-bottom: 1rem;">
                ${pageData.localInsight()}
            </p>
            
            <!-- Legal Summary -->
            <div style="padding-top: 1rem; border-top: 1px dashed rgba(59, 130, 246, 0.2);">
                <span style="font-size: 0.85rem; font-weight: 600; color: var(--text-tertiary);">⚖️ LEGAL NOTE:</span>
                <span style="font-size: 0.9rem; color: var(--text-secondary);">${pageData.legalSummary()}</span>
            </div>
        </div>

        <!-- Data Sources (E-E-A-T) -->
        @if(pageData.dataSources() != null && !pageData.dataSources().isEmpty())
        <div style="margin-bottom: 4rem; padding: 1.5rem; background: var(--bg-body); border: 1px solid var(--border-subtle); border-radius: 8px;">
            <h4 style="font-size: 0.8rem; font-weight: 600; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 0.75rem; letter-spacing: 0.05em;">Data Sources</h4>
            <ul style="list-style: none; padding: 0; margin: 0; display: flex; flex-wrap: wrap; gap: 0.5rem;">
                @for(String source : pageData.dataSources())
                    <li style="font-size: 0.8rem; color: var(--text-secondary); background: var(--bg-surface); padding: 0.25rem 0.75rem; border-radius: 12px;">${source}</li>
                @endfor
            </ul>
        </div>
        @endif

        <!-- Common Questions (QnA Section) -->
        <div style="margin-bottom: 4rem;">
            <h3 style="font-size: 0.9rem; font-weight: 600; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 2rem; letter-spacing: 0.05em;">
                Common Moving Questions
            </h3>
            
            @if(pageData.commonQuestions() != null)
                @for(firstrentverdict.service.seo.CityContentGenerator.QnA qna : pageData.commonQuestions())
                    <div style="margin-bottom: 2rem;">
                        <h4 style="font-size: 1.2rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">
                            ${qna.question()}
                        </h4>
                        <p style="font-size: 1rem; color: var(--text-secondary); line-height: 1.6;">
                            ${qna.answer()}
                        </p>
                    </div>
                @endfor
            @endif
        </div>

        <!-- MEGA CTA: The Main Product -->
        <div style="margin-bottom: 4rem; background: linear-gradient(135deg, var(--bg-surface) 0%, rgba(59, 130, 246, 0.03) 100%); border: 2px solid var(--primary); border-radius: 24px; padding: 4rem 2rem; text-align: center; position: relative; overflow: hidden; box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.05);">
            <!-- Decorative Element -->
            <div style="position: absolute; top: -50px; right: -50px; width: 150px; height: 150px; background: var(--primary); opacity: 0.05; border-radius: 50%;"></div>
            
            <div style="position: relative; z-index: 1;">
                <div class="badge-accent" style="margin-bottom: 1.5rem; display: inline-block;">THE VERDICT ENGINE</div>
                <h2 style="font-size: 2.25rem; font-weight: 800; margin-bottom: 1.5rem; letter-spacing: -0.02em;">
                    Should you move to ${pageData.city()}?
                </h2>
                <p style="font-size: 1.2rem; color: var(--text-secondary); margin-bottom: 2.5rem; max-width: 550px; margin-left: auto; margin-right: auto; line-height: 1.6;">
                    Don't guess with your savings. Our <strong>Real-time Simulator</strong> analyzes your cash vs. actual move-in costs in ${pageData.city()} to give you a definitive Approved or Denied judgment.
                </p>
                
                <div style="display: flex; flex-direction: column; align-items: center; gap: 1.5rem;">
                    <a href="/RentVerdict/" class="btn btn-primary btn-lg" style="text-decoration: none; padding: 1.25rem 3rem; font-size: 1.25rem; font-weight: 700; width: 100%; max-width: 400px; box-shadow: 0 20px 30px -10px rgba(59, 130, 246, 0.4);">
                        Launch 2026 Calculator
                    </a>
                    
                    <div style="display: flex; gap: 2rem; justify-content: center; flex-wrap: wrap;">
                        <div style="display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: var(--text-secondary);">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--success)" stroke-width="3"><polyline points="20 6 9 17 4 12"></polyline></svg>
                            Free Forever
                        </div>
                        <div style="display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: var(--text-secondary);">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--success)" stroke-width="3"><polyline points="20 6 9 17 4 12"></polyline></svg>
                            No Sign-up Required
                        </div>
                        <div style="display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: var(--text-secondary);">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--success)" stroke-width="3"><polyline points="20 6 9 17 4 12"></polyline></svg>
                            100% Data Privacy
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Block 3: Market Context -->
        <div style="margin-bottom: 4rem;">
            <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 1rem;">Regional Market Context</h2>
            <p style="font-size: 1.1rem; line-height: 1.7; color: var(--text-secondary); margin-bottom: 1.5rem;">
                ${pageData.marketContextText()}
            </p>
            <a href="/RentVerdict/guide/rent-affordability-rule" style="font-size: 0.95rem; color: var(--text-primary); font-weight: 500; text-decoration: underline;">
                Learn more about our 50/30/20 Liquidity Framework &rarr;
            </a>
        </div>

        <!-- Related Cities in Same State (Dynamic Internal Linking) -->
        <div style="margin-bottom: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);">
            <h3 style="font-size: 1rem; font-weight: 600; color: var(--text-tertiary); margin-bottom: 1rem;">MORE IN ${pageData.state()}</h3>
            <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                @if(!relatedCities.isEmpty())
                    @for(CityEntry related : relatedCities)
                        <a href="/RentVerdict/verdict/${related.city().toLowerCase().replace(" ", "-")}-${related.state().toLowerCase()}" 
                           style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px; transition: background 0.2s;"
                           onmouseover="this.style.background='var(--bg-surface)'"
                           onmouseout="this.style.background='transparent'">
                            ${related.city()}, ${related.state()}
                        </a>
                    @endfor
                @else
                    <a href="/RentVerdict/verdict/austin-tx" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">Austin, TX</a>
                    <a href="/RentVerdict/verdict/dallas-tx" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">Dallas, TX</a>
                    <a href="/RentVerdict/verdict/miami-fl" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">Miami, FL</a>
                    <a href="/RentVerdict/verdict/new-york-ny" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">New York, NY</a>
                @endif
            </div>
            <div style="margin-top: 1rem;">
                <a href="/RentVerdict/cities" style="color: var(--text-tertiary); font-size: 0.85rem; text-decoration: underline;">View all 100+ cities →</a>
            </div>
        </div>



    </div>
`)
