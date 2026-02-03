@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@import firstrentverdict.service.seo.CityContentGenerator.QnA
@param CityPageContent pageData

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
        
        <!-- Hero Section -->
        <div style="text-align: center; margin-bottom: 4rem;">
            <div class="badge-subtle" style="margin-bottom: 1rem;">2026 Rental Market Audit</div>
            <h1 style="font-size: 2.5rem; font-weight: 800; margin-bottom: 1rem; line-height: 1.2;">
                ${pageData.pageTitle()}
            </h1>
            <p style="font-size: 1.2rem; color: var(--text-secondary); max-width: 700px; margin: 0 auto;">
                ${pageData.marketContextText()}
            </p>
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

            </div>
        </div>

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

        <!-- Internal Linking (Popular Cities Stub) -->
        <div style="margin-bottom: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);">
            <h3 style="font-size: 1rem; font-weight: 600; color: var(--text-tertiary); margin-bottom: 1rem;">POPULAR MARKETS</h3>
            <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="/RentVerdict/verdict/austin-tx" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">Austin, TX</a>
                <a href="/RentVerdict/verdict/dallas-tx" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">Dallas, TX</a>
                <a href="/RentVerdict/verdict/miami-fl" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">Miami, FL</a>
                <a href="/RentVerdict/verdict/new-york-ny" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;">New York, NY</a>
            </div>
        </div>

        <!-- Safe CTA -->
        <div style="background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 3rem; text-align: center;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 1rem;">Check Your Specific Fit</h2>
            <p style="font-size: 1.1rem; color: var(--text-secondary); margin-bottom: 2rem; max-width: 500px; margin-left: auto; margin-right: auto;">
                Every financial situation is unique. Use current 2026 market data to check if this move is safe for you.
            </p>
            <a href="/RentVerdict/" class="btn-primary" style="text-decoration: none; display: inline-block; padding: 1rem 2rem; font-size: 1.1rem;">Launch Financial Calculator</a>
        </div>

    </div>
`)
