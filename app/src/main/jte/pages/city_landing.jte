@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent

@param CityPageContent pageData

@template.layout.main(
    title = pageData.pageTitle(),
    description = pageData.metaDescription(),
    canonical = "https://movecostinfo.com/verdict/" + pageData.city().toLowerCase().replace(" ", "-") + "-" + pageData.state().toLowerCase(),
    content = @`
    <div style="max-width: 800px; margin: 4rem auto; padding: 0 1.5rem;">
        
        <!-- Header -->
        <header style="text-align: center; margin-bottom: 3rem;">
            <div style="font-size: 0.85rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.5rem;">
                2026 Rental Market Data
            </div>
            <h1 style="font-size: 2.5rem; font-weight: 800; letter-spacing: -0.02em; line-height: 1.1; margin-bottom: 1rem;">
                Rental Financial Overview:<br>
                <span style="color: var(--text-secondary);">${pageData.city()}, ${pageData.state()}</span>
            </h1>
            <p style="font-size: 1.1rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto; line-height: 1.6;">
                Objective financial requirements for prospective tenants, based on local economic indicators.
            </p>
        </header>

        <!-- Soft Verdict / Market Insight Box (Pre-Verdict Context) -->
        <div style="background: var(--bg-surface); border-left: 4px solid var(--text-primary); padding: 2rem; margin-bottom: 3rem; border-radius: 4px; box-shadow: 0 4px 20px rgba(0,0,0,0.03);">
            <h2 style="font-size: 1.4rem; font-weight: 700; color: var(--text-primary); margin-bottom: 1rem; line-height: 1.3;">
                ${pageData.preVerdictHeadline()}
            </h2>
            <p style="font-size: 1.05rem; color: var(--text-secondary); line-height: 1.6;">
                ${pageData.riskNarrative()}
            </p>
        </div>

        <!-- Feature Grid (Data Blocks) -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 1.5rem; margin-bottom: 4rem;">
            
            <!-- Block 1: Income -->
            <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; border: 1px solid var(--border-subtle);">
                <div style="font-size: 0.9rem; font-weight: 600; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 1rem;">
                    Typical Income Requirement
                </div>
                <div style="font-size: 2.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">
                    $${String.format("%,d", pageData.monthlyIncomeReq())}<span style="font-size: 1rem; color: var(--text-tertiary); font-weight: 400;">/mo</span>
                </div>
                <div style="font-size: 1rem; color: var(--text-secondary); margin-bottom: 1.5rem;">
                    or approx. <strong>$${String.format("%,d", pageData.yearlyIncomeReq())}/yr</strong>
                </div>
                <p style="font-size: 0.9rem; color: var(--text-secondary); line-height: 1.5;">
                    ${pageData.incomeLogicText()}
                </p>
            </div>

            <!-- Block 2: Upfront Cash -->
            <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; border: 1px solid var(--border-subtle);">
                <div style="font-size: 0.9rem; font-weight: 600; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 1rem;">
                    Est. Move-in Cash Needed
                </div>
                <div style="font-size: 2.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">
                    $${String.format("%,d", pageData.totalUpfrontEstimate())}
                </div>
                <div style="font-size: 0.9rem; color: var(--text-secondary); line-height: 1.6;">
                    Includes <strong>First Month Rent</strong> ($${pageData.medianRent()}), 
                    <strong>Security Deposit</strong> (~$${pageData.typicalDeposit()}), and 
                    <strong>Moving Costs</strong> (~$${pageData.typicalMoving()}).
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
