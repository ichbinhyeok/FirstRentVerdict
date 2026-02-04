@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict
@import firstrentverdict.model.verdict.VerdictInput
@import firstrentverdict.model.verdict.MarketPosition
@import firstrentverdict.model.verdict.RegionalContext
@import firstrentverdict.model.verdict.SafetyGap
@import firstrentverdict.model.verdict.FinancialLineItem
@import java.util.List
@import java.lang.Math

@param VerdictResult result
@param VerdictInput input
@param boolean noindex = true
@param String pageType = "GENERAL"
@param String scenarioTitle = null

!{
    String metaDesc = "";
    String cityState = input.city() + ", " + input.state();
    int estDeposit = result.financials().totalUpfrontCost();
    
    if ("GENERAL".equals(pageType)) {
        metaDesc = "True cost of renting in " + cityState + ". See hidden fees, deposit requirements (" + String.format("$%,d", estDeposit) + "), and market strictness.";
    } else if (pageType.startsWith("CREDIT_")) {
        String tierName = pageType.replace("CREDIT_", ""); 
        metaDesc = "Renting in " + cityState + " with " + tierName + " Credit? We analyzed local risks. Expect ~" + String.format("$%,d", estDeposit) + " upfront. See approval strategy.";
    } else if ("RELOCATION".equals(pageType)) {
        metaDesc = "Moving to " + cityState + "? Essential relocation cost breakdown. See how much cash you really need before you move. 2026 update.";
    } else {
        metaDesc = "First Rent Verdict analysis for " + cityState + ". Check your approval odds.";
    }
}

@template.layout.main(
    title = (scenarioTitle != null ? scenarioTitle : "Verdict Report - First Rent Verdict"),
    description = metaDesc,
    noindex = noindex,
    content = @`
    
    <!-- Schema.org JSON-LD -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "FAQPage",
      "mainEntity": [{
        "@type": "Question",
        "name": "How much do I need to move to ${cityState}?",
        "acceptedAnswer": {
          "@type": "Answer",
          "text": "Based on current market data, you should budget approximately ${String.format("$%,d", result.financials().totalUpfrontCost())} for upfront costs, which includes the first month's rent, security deposit, and potential broker or admin fees."
        }
      }, {
        "@type": "Question",
        "name": "Can I rent in ${cityState} with ${pageType.startsWith("CREDIT_") ? pageType.replace("CREDIT_", "").toLowerCase() + " credit" : "bad credit"}?",
        "acceptedAnswer": {
          "@type": "Answer",
          "text": "Yes, but it requires preparation. Our analysis shows a ${result.verdict() == firstrentverdict.model.verdict.Verdict.APPROVED ? "high" : "moderate to low"} likelihood of approval based on your financials, provided you have a safety buffer."
        }
      }]
    }
    </script>

    <!-- Breadcrumbs -->
    <nav style="max-width: 900px; margin: 0 auto; padding: var(--space-4) var(--space-6) 0; font-size: 0.8125rem; color: var(--text-tertiary);">
        <a href="/RentVerdict/" style="color: inherit; text-decoration: none;">Home</a> 
        <span style="margin: 0 4px;">/</span> 
        <a href="/RentVerdict/verdict/${input.city().toLowerCase().replace(" ", "-")}-${input.state().toLowerCase()}" style="color: inherit; text-decoration: none;">${input.city()}</a>
        <span style="margin: 0 4px;">/</span>
        <span style="color: var(--text-secondary); font-weight: 500;">
            Analysis
        </span>
    </nav>

    <!-- Main Result Container -->
    <div style="max-width: 900px; margin: 0 auto; padding: var(--space-8) var(--space-6);">
        
        <!-- 1. Navigation -->
        <div style="margin-bottom: var(--space-8);">
            <a href="/RentVerdict/" class="back-link">
                &larr; Start Over
            </a>
        </div>
        
        <!-- pSEO Engagement Hook (Behavioral Nudge) -->
        @if(!"GENERAL".equals(pageType))
            <div style="background: #fff8e1; border: 1px solid #ffe082; color: #5d4037; padding: var(--space-4); border-radius: var(--radius-sm); margin-bottom: var(--space-6); text-align: center; font-size: 0.95rem; box-shadow: 0 2px 4px rgba(0,0,0,0.05);">
                <div style="font-weight: 600; margin-bottom: 4px;">⚠️ Estimate based on typical ${pageType.replace("CREDIT_", "").toLowerCase()} scenarios</div>
                <div>
                   Results vary by exact rent & cash. 
                   <a href="#simulation-lab" style="color: #e65100; font-weight: 700; text-decoration: underline; margin-left: 4px;">
                       👇 Customize your calculation below for accuracy.
                   </a>
                </div>
            </div>
        @endif



        <!-- 2. Hero Verdict Section -->
        <div class="verdict-hero fade-in-up">
            @if(!"GENERAL".equals(pageType))
                <h1 style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary); margin-bottom: var(--space-4); text-transform: uppercase; letter-spacing: 0.02em; line-height: 1.2;">
                    @if(pageType.startsWith("CREDIT_POOR"))
                        Renting in ${input.city()} with <span style="color: var(--danger);">Poor Credit</span>
                    @elseif(pageType.startsWith("CREDIT_FAIR"))
                        Renting in ${input.city()} with <span style="color: #f59e0b;">Fair Credit</span>
                    @elseif(pageType.equals("RELOCATION"))
                         Moving to ${input.city()} Cost Guide
                    @else
                         ${input.city()} Rental Verdict
                    @endif
                </h1>
            @else
                <div class="verdict-label">OFFICIAL VERDICT</div>
            @endif
            
            <!-- Verdict Badge -->
            <div style="margin-bottom: var(--space-4);" id="verdict-badge-container">
                @if(result.verdict() == Verdict.APPROVED)
                    <h1 class="verdict-text verdict-approved">APPROVED</h1>
                @elseif(result.verdict() == Verdict.BORDERLINE)
                    <h1 class="verdict-text verdict-borderline">BORDERLINE</h1>
                @else
                    <h1 class="verdict-text verdict-denied">DENIED</h1>
                @endif
            </div>

            <!-- AI Advisor Note (The "WHY") -->
            <div class="advisor-note">
                <div class="advisor-icon">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                    </svg>
                </div>
                <div>
                    <h3 style="font-size: 0.875rem; font-weight: 700; color: var(--text-primary); margin-bottom: 2px;">Analyst's Summary</h3>
                    <p id="main-advisor-text" style="font-size: 1rem; color: var(--text-secondary); line-height: 1.5;">
                        ${result.whyThisVerdict()}
                    </p>
                </div>
            </div>

            <!-- Key Contributing Factors (The "PROOF") -->
            <div id="factors-container" class="factors-grid">
                @for(String factor : result.contributingFactors())
                    <div class="factor-item">
                        <span style="color: var(--accent-primary); margin-right: var(--space-2);">✓</span>
                        ${factor}
                    </div>
                @endfor
                <!-- Regional Context Injection -->
                @if(result.regionalContext() != null && result.regionalContext().contextFactors() != null)
                   @for(String regionFactor : result.regionalContext().contextFactors())
                        <div class="factor-item" style="background: rgba(var(--accent-primary-rgb), 0.05);">
                            <span style="color: var(--text-tertiary); margin-right: var(--space-2);">📍</span>
                            ${regionFactor}
                        </div>
                   @endfor
                @endif
            </div>
        </div>


        <!-- 3. Key Financial Cards (Dashboard Grid) -->
        <div class="financial-grid fade-in-up" style="animation-delay: 0.1s; margin-bottom: var(--space-12);">
            <!-- Total Upfront Cost -->
            <div class="card">
                <div class="financial-label">Est. Move-In Invoice</div>
                <div class="financial-value" id="disp-total-cost">$${String.format("%,d", result.financials().totalUpfrontCost())}</div>
                <div class="financial-value-sm">1st Month + Deposit + Fees</div>
            </div>

            <!-- Available Cash -->
            <div class="card">
                <div class="financial-label">Your Liquidity</div>
                <div class="financial-value" id="disp-cash">$${String.format("%,d", result.financials().availableCash())}</div>
                <div class="financial-value-sm">Adjustable in Lab</div>
            </div>

            <!-- Remaining Buffer / Shortfall -->
            <div class="card" id="card-buffer" data-initial-status="${result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "danger" : "success"}">
                <div class="financial-label" id="lbl-buffer">
                    ${result.financials().remainingBuffer() < 0 ? "Deficit (Shortfall)" : "Safety Buffer"}
                </div>
                <div class="financial-value" id="disp-buffer" style="color: ${result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--danger)" : "var(--success)"};">
                    ${result.financials().remainingBuffer() < 0 ? "-" : ""}$${String.format("%,d", Math.abs(result.financials().remainingBuffer()))}
                </div>
                <div class="financial-value-sm" id="lbl-buffer-status">
                    ${result.financials().remainingBuffer() < result.financials().recommendedBuffer() 
                        ? "High Risk: Below Safety Threshold" 
                        : "Healthy Emergency Reserve"}
                </div>
            </div>
        </div>

        <!-- 4. Split Section: Breakdown & Analysis -->
        <div class="split-section-grid fade-in-up" style="animation-delay: 0.2s;">
            
            <!-- Left: Smart Receipt (Correctly using costBreakdown) -->
            <div>
                <h3 class="section-title">Verified Cost Breakdown</h3>
                <div class="receipt">
                    <div class="receipt-header">
                        <h4>Itemized Invoice</h4>
                        <span class="badge badge-outline">2026 Data</span>
                    </div>
                    <div class="receipt-body">
                        <!-- Loop through ACTUAL backend items instead of hardcoding -->
                        @for(firstrentverdict.model.verdict.FinancialLineItem item : result.financials().costBreakdown())
                            <div class="receipt-item receipt-dynamic-item" data-label="${item.label()}">
                                <div>
                                    <div class="receipt-item-label">${item.label()}</div>
                                    <div class="receipt-item-note" id="note-${item.label().replaceAll("[^a-zA-Z0-9]", "")}">${item.annotation()}</div>
                                </div>
                                <div class="receipt-item-value" id="val-${item.label().replaceAll("[^a-zA-Z0-9]", "")}">$${String.format("%,d", item.amount())}</div>
                            </div>
                        @endfor
                    </div>
                    <div class="receipt-total">
                        <span class="receipt-total-label">EST. TOTAL</span>
                        <span class="receipt-total-value" id="receipt-total">$${String.format("%,d", result.financials().totalUpfrontCost())}</span>
                    </div>
                </div>
                
                <p style="margin-top: var(--space-4); font-size: 0.8125rem; color: var(--text-tertiary); line-height: 1.5;">
                    * <strong>Data Source:</strong> Estimates based on <span style="text-decoration: underline;">2026 Regional Cost Index</span>. 
                    Security deposits are calculated using state-specific caps where applicable. Values are for planning purposes only.
                </p>
            </div>

            <!-- Right: Interactive Simulation -->
            <div>
                <h3 class="section-title">Scenario Lab</h3>
                
                <!-- Market Radar -->
                <div class="market-radar">
                    <div class="radar-legend" style="margin-bottom: var(--space-2);">
                        <span>Market Assessment</span>
                        <span style="font-weight: 600; color: var(--text-primary);" id="radar-label">${result.marketPosition().marketZone()}</span>
                    </div>
                    <div class="radar-track">
                        <div class="radar-range" title="Typical Market Range"></div>
                        <div class="radar-marker" id="radar-marker" 
                             style="left: ${Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 2.0) * 100.0)) + "%"};">
                        </div>
                    </div>
                    <div class="radar-legend">
                        <span>Better Value</span>
                        <span>Premium</span>
                    </div>
                </div>

                <!-- Interactive Sim Card -->
                <div id="simulation-lab" class="card sim-card" style="margin-bottom: var(--space-8); border: 1px solid var(--border-default); box-shadow: 0 4px 12px rgba(0,0,0,0.05);">
                     <div style="margin-bottom: var(--space-6);">
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                            <h4 style="font-size: 1rem; font-weight: 700; color: var(--accent-primary);">Live Simulator</h4>
                            <span class="badge badge-accent">Interactive</span>
                        </div>
                        <p style="font-size: 0.8125rem; color: var(--text-secondary); margin-top: var(--space-2);">
                            Adjust values to test 'What-If' scenarios. Deposit rates update automatically based on risk.
                        </p>
                     </div>
                     
                     <!-- Rent Slider -->
                     <div class="form-group mb-4">
                        <div style="display: flex; justify-content: space-between; margin-bottom: var(--space-2); font-weight: 600;">
                            <span style="font-size: 0.875rem; color: var(--text-secondary);">Target Rent</span>
                            <span id="sim-rent-display" style="color: var(--accent-primary); font-size: 1.125rem;">$${String.format("%,d", result.financials().monthlyRent())}</span>
                        </div>
                        <!-- Range Limit: Min = 25% percentile or $500 / Max = 75% percentile * 1.5 or Current*1.5 -->
                        <input type="range" id="rent-slider" 
                               style="width: 100%; cursor: grab;"
                               min="${Math.max(500, (int)(result.marketPosition().p25Rent() * 0.5))}" 
                               max="${(int)Math.max(result.marketPosition().p75Rent() * 1.5, result.financials().monthlyRent() * 1.5)}" 
                               value="${result.financials().monthlyRent()}" 
                               step="50">
                        <div style="display: flex; justify-content: space-between; font-size: 0.75rem; color: var(--text-tertiary); margin-top: 4px;">
                            <span>$${Math.max(500, (int)(result.marketPosition().p25Rent() * 0.5))}</span>
                            <span>$${(int)Math.max(result.marketPosition().p75Rent() * 1.5, result.financials().monthlyRent() * 1.5)}</span>
                        </div>
                     </div>

                     <!-- Cash Slider -->
                     <div class="form-group mb-4">
                        <div style="display: flex; justify-content: space-between; margin-bottom: var(--space-2); font-weight: 600;">
                            <span style="font-size: 0.875rem; color: var(--text-secondary);">Your Cash</span>
                            <span id="sim-cash-display" style="color: var(--text-primary); font-size: 1.125rem;">$${String.format("%,d", result.financials().availableCash())}</span>
                        </div>
                        <!-- Range Limit: Min = 0 / Max = Current + 10k or Current * 1.5 (More realistic) -->
                        <input type="range" id="cash-slider" 
                               style="width: 100%; cursor: grab;"
                               min="0" 
                               max="${(int)Math.max(result.financials().availableCash() + 5000, result.financials().availableCash() * 1.5)}" 
                               value="${result.financials().availableCash()}" 
                               step="100">
                     </div>

                     <!-- 3. Advanced Simulation Controls -->
                     <div style="background: var(--bg-tertiary); padding: var(--space-4); border-radius: var(--radius-sm); border: 1px solid var(--border-default); margin-top: var(--space-4);">
                        
                        <!-- Move Type -->
                        <div class="form-group mb-4">
                            <label class="form-label" style="font-size: 0.75rem; font-weight: 700; text-transform: uppercase; color: var(--text-secondary); margin-bottom: var(--space-2);">Move Type</label>
                            <div style="display: flex; gap: var(--space-4);">
                                <label style="display: flex; align-items: center; gap: 6px; font-size: 0.875rem; cursor: pointer;">
                                    @if(input.isLocalMove())
                                        <input type="radio" name="moveType" value="local" checked> Local
                                    @else
                                        <input type="radio" name="moveType" value="local"> Local
                                    @endif
                                </label>
                                <label style="display: flex; align-items: center; gap: 6px; font-size: 0.875rem; cursor: pointer;">
                                    @if(!input.isLocalMove())
                                        <input type="radio" name="moveType" value="long" checked> Long Distance
                                    @else
                                        <input type="radio" name="moveType" value="long"> Long Distance
                                    @endif
                                </label>
                            </div>

                        </div>

                        <!-- Credit Score -->
                        <div class="form-group mb-4">
                             <label class="form-label" style="font-size: 0.75rem; font-weight: 700; text-transform: uppercase; color: var(--text-secondary); margin-bottom: var(--space-2);">Credit Profile</label>
                             <select id="sim-credit" class="form-select" style="font-size: 0.875rem;">
                                @if(input.creditTier() == firstrentverdict.model.verdict.CreditTier.EXCELLENT)
                                    <option value="EXCELLENT" selected>Excellent (750+)</option>
                                @else
                                    <option value="EXCELLENT">Excellent (750+)</option>
                                @endif

                                @if(input.creditTier() == firstrentverdict.model.verdict.CreditTier.GOOD)
                                    <option value="GOOD" selected>Good (670-749)</option>
                                @else
                                    <option value="GOOD">Good (670-749)</option>
                                @endif

                                @if(input.creditTier() == firstrentverdict.model.verdict.CreditTier.FAIR)
                                    <option value="FAIR" selected>Fair (580-669)</option>
                                @else
                                    <option value="FAIR">Fair (580-669)</option>
                                @endif

                                @if(input.creditTier() == firstrentverdict.model.verdict.CreditTier.POOR)
                                    <option value="POOR" selected>Poor (&lt; 580)</option>
                                @else
                                    <option value="POOR">Poor (&lt; 580)</option>
                                @endif
                             </select>
                         </div>
                         
                         <!-- Pet Toggle -->
                         <div class="form-group mb-0">
                            <label class="checkbox-label" style="font-size: 0.875rem; font-weight: 500;">
                                                                @if(input.hasPet())
                                    <input type="checkbox" id="sim-pet" checked>
                                @else
                                    <input type="checkbox" id="sim-pet">
                                @endif
                                <span>Moving with a pet</span>
                            </label>
                         </div>
                     </div>
                     
                     <div id="sim-feedback" style="margin-top: var(--space-4); padding: var(--space-3); background: var(--bg-primary); border-radius: var(--radius-sm); font-size: 0.875rem; display: none; border-left: 3px solid var(--text-secondary);">
                        <div style="display: flex; justify-content: space-between;">
                             <span>New Buffer:</span>
                             <strong id="sim-new-buffer">$0</strong>
                        </div>
                     </div>
                </div>

                <!-- Strategic Action Steps -->
                <div id="action-steps-container" class="step-list">
                    @if(result.verdict() != Verdict.APPROVED)
                        <div class="step-item">
                            <div class="step-number">1</div>
                            <div class="step-content">
                                <h4>Bridge the Gap</h4>
                                <p id="action-step-desc">You are approximately <strong>$${String.format("%,d", Math.abs(result.financials().remainingBuffer()))}</strong> short of a comfortable move. Consider a co-signer.</p>
                            </div>
                        </div>
                    @else
                        <div class="step-item">
                            <div class="step-number" style="background: var(--success);">✓</div>
                            <div class="step-content">
                                <h4>Application Ready</h4>
                                <p id="action-step-desc">This rent is within your <strong>safe financial zone</strong>. Proceed with documentation.</p>
                            </div>
                        </div>
                    @endif
                </div>

                <!-- Share This Insight (Viral Loop) -->
                <div style="margin-top: var(--space-8); padding-top: var(--space-6); border-top: 1px solid var(--border-default);">
                    <h4 style="font-size: 0.875rem; font-weight: 600; color: var(--text-secondary); margin-bottom: var(--space-3); text-transform: uppercase; letter-spacing: 0.05em;">Share This Tool</h4>
                    <div style="display: flex; gap: var(--space-3);">
                        <button onclick="shareTwitter()" class="btn btn-secondary btn-sm" style="flex: 1;">
                            Twitter
                        </button>
                        <button onclick="copyLink()" class="btn btn-secondary btn-sm" style="flex: 1;">
                            Copy Link
                        </button>
                    </div>
                    <script>
                        function getShareUrl() {
                            const baseUrl = "https://movecostinfo.com/RentVerdict/verdict";
                            const params = new URLSearchParams();
                            params.append("cityState", "${input.city()}|${input.state()}"); 
                            // Note: We need the exact 'City|State' format. 
                            // Assuming result.regionalContext() has these or extracting from result logic.
                            // However, let's use the explicit Values from the result object for accuracy.
                            // result object doesn't openly expose 'input' directly in the generic view unless we cast it or use financials.
                            // But we have result.financials().monthlyRent() etc.
                            // Constructing params:
                            params.append("monthlyRent", "${result.financials().monthlyRent()}");
                            params.append("availableCash", "${result.financials().availableCash()}");
                            // Defaulting booleans to false/true based on logic is hard without the input object.
                            // But wait, the controller saves input to session.
                            // Let's just use what we have. If pet/move info is minor, we can omit or default.
                            // Better: Inject the input parameters as hidden data attributes or JS consts.
                            return baseUrl + "?" + params.toString();
                        }

                        function shareTwitter() {
                            const text = "Checking my move-in costs for ${result.regionalContext().cityName()} with First Rent Verdict. The hidden costs are real! 🏠💸";
                            const url = getShareUrl();
                            window.open("https://twitter.com/intent/tweet?text=" + encodeURIComponent(text) + "&url=" + encodeURIComponent(url), "_blank");
                        }
                        function copyLink() {
                            navigator.clipboard.writeText(getShareUrl());
                            alert("Link copied to clipboard! Anyone with this link can see these results.");
                        }
                    </script>
                </div>

            </div>
        </div>
        
        <!-- Mobile Sticky Bar for Scenarios -->
        <div id="sticky-sim-bar" class="sim-sticky-bar">
            <div>
                <div class="sticky-label">Remaining Buffer</div>
                <div id="sticky-buffer-val" class="sticky-value">$0</div>
            </div>
            <div id="sticky-status-badge" class="sticky-status" style="background: var(--bg-tertiary);">
                Checking...
            </div>
        </div>

        <!-- Data Injection with Dual Deposit Logic -->
        <div id="sim-data" 
             data-cash="${result.financials().availableCash()}"
             data-static-costs="${result.financials().staticCosts()}"
             data-dep-typ="${result.financials().typicalDepositMult()}"
             data-dep-high="${result.financials().highRiskDepositMult()}"
             data-rec-buffer="${result.financials().recommendedBuffer()}"
             data-median-rent="${result.marketPosition().medianRent()}"
             style="display:none;"></div>

    </div>
    
    <style>
        .split-section-grid {
            display: grid; 
            grid-template-columns: 1fr 1fr; 
            gap: var(--space-8); 
            align-items: start;
        }
        @media (max-width: 900px) {
            .split-section-grid {
                grid-template-columns: 1fr;
            }
        }
        
        /* Advisor Note Styling */
        .advisor-note {
            background: #f8faff;
            border-left: 4px solid var(--accent-primary);
            padding: var(--space-4);
            border-radius: var(--radius-sm);
            margin-bottom: var(--space-6);
            display: flex;
            gap: var(--space-4);
            align-items: flex-start;
        }
        .advisor-icon {
            color: var(--accent-primary);
            flex-shrink: 0;
            margin-top: 2px;
        }
        
        /* Factors Grid */
        .factors-grid {
            display: flex;
            gap: var(--space-3);
            flex-wrap: wrap;
            margin-bottom: var(--space-6);
        }
        .factor-item {
            background: var(--bg-secondary);
            border: 1px solid var(--border-default);
            padding: var(--space-2) var(--space-3);
            border-radius: var(--radius-full);
            font-size: 0.875rem;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
        }
        
        .fade-in-up {
            animation: fadeInUp 0.5s ease-out forwards;
            opacity: 0;
            transform: translateY(10px);
        }
        @keyframes fadeInUp {
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
    
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            // -- Elements --
            const rentSlider = document.getElementById('rent-slider');
            const cashSlider = document.getElementById('cash-slider');
            const rentDisplay = document.getElementById('sim-rent-display');
            const cashDisplay = document.getElementById('sim-cash-display');
            
            // New Inputs
            const moveTypeRadios = document.getElementsByName('moveType');
            const simCredit = document.getElementById('sim-credit');
            const simPet = document.getElementById('sim-pet');

            // Display Elements
            const dispTotalCost = document.getElementById('disp-total-cost');
            const dispCash = document.getElementById('disp-cash'); // Financial Card
            const dispBuffer = document.getElementById('disp-buffer');
            const lblBuffer = document.getElementById('lbl-buffer');
            const lblBufferStatus = document.getElementById('lbl-buffer-status');
            const cardBuffer = document.getElementById('card-buffer');
            
            const receiptBody = document.querySelector('.receipt-body');
            const receiptTotal = document.getElementById('receipt-total');
            
            const badgeContainer = document.getElementById('verdict-badge-container');
            const advisorText = document.getElementById('main-advisor-text');
            const factorsContainer = document.getElementById('factors-container');
            
            const radarMarker = document.getElementById('radar-marker');
            const radarLabel = document.getElementById('radar-label');

            if (!rentSlider || !cashSlider) return;
            
            const fmt = (n) => '$' + Math.floor(n).toLocaleString();
            let debounceTimer;

            // -- Toggle Long Distance Inputs --
            Array.from(moveTypeRadios).forEach(r => {
                r.addEventListener('change', triggerSimulation);
            });

            // -- Event Listeners --
            [rentSlider, cashSlider, simCredit, simPet].forEach(el => {
                if(el) el.addEventListener('input', triggerSimulation);
            });
            
            // Initial state check
            if(document.querySelector('input[name="moveType"]:checked').value === 'long') {
                longDistInputs.style.display = 'block';
            }

            function triggerSimulation() {
                // Immediate UI feedback for sliders
                rentDisplay.innerText = fmt(rentSlider.value);
                cashDisplay.innerText = fmt(cashSlider.value);

                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(fetchSimulation, 500);
            }

            async function fetchSimulation() {
                // 1. Gather Data
                const cityState = "${input.city()}|${input.state()}"; // Original target city
                const parts = cityState.split('|');
                const isLongDist = document.querySelector('input[name="moveType"]:checked').value === 'long';
                
                const payload = {
                    city: parts[0],
                    state: parts[1],
                    monthlyRent: parseInt(rentSlider.value),
                    availableCash: parseInt(cashSlider.value),
                    hasPet: simPet.checked,
                    isLocalMove: !isLongDist,
                    creditTier: simCredit.value,
                    fromCity: isLongDist ? simFromCity.value : null,
                    fromState: isLongDist ? simFromState.value : null
                };

                // Visual Loading State
                if(advisorText) advisorText.style.opacity = '0.5';

                try {
                    const res = await fetch('/RentVerdict/api/simulate', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify(payload)
                    });
                    
                    if (!res.ok) throw new Error("Sim failed");
                    const result = await res.json();
                    
                    updateUI(result);
                    
                } catch (e) {
                    console.error(e);
                } finally {
                    if(advisorText) advisorText.style.opacity = '1';
                }
            }

            function updateUI(result) {
                const fin = result.financials;
                
                // Financial Cards
                if(dispTotalCost) dispTotalCost.innerText = fmt(fin.totalUpfrontCost);
                if(dispCash) dispCash.innerText = fmt(fin.availableCash);
                
                const buff = fin.remainingBuffer;
                if(dispBuffer) {
                    dispBuffer.innerText = (buff < 0 ? "-" : "") + fmt(Math.abs(buff));
                    dispBuffer.style.color = buff < fin.recommendedBuffer ? "var(--danger)" : "var(--success)";
                }
                
                if(cardBuffer) {
                    const isRisk = buff < fin.recommendedBuffer;
                    cardBuffer.style.border = isRisk ? "1px solid var(--danger)" : "1px solid var(--success)";
                    cardBuffer.style.background = isRisk ? "#fff0f0" : "#f0fff4";
                    
                    if(lblBuffer) lblBuffer.innerText = buff < 0 ? "Deficit" : "Safety Buffer";
                    if(lblBufferStatus) lblBufferStatus.innerText = isRisk ? (buff < 0 ? "Insolvency Risk" : "Below Safety Level") : "Healthy Reserve";
                }

                // Receipt Breakdown (Dynamic Rebuild) - NO BACKTICKS
                if(receiptBody) {
                    let html = '';
                    fin.costBreakdown.forEach(item => {
                        html += '<div class="receipt-item receipt-dynamic-item">' +
                            '<div>' +
                                '<div class="receipt-item-label">' + item.label + '</div>' +
                                '<div class="receipt-item-note">' + item.annotation + '</div>' +
                            '</div>' +
                            '<div class="receipt-item-value">$' + item.amount.toLocaleString() + '</div>' +
                        '</div>';
                    });
                    receiptBody.innerHTML = html;
                }
                if(receiptTotal) receiptTotal.innerText = fmt(fin.totalUpfrontCost);

                // Verdict Badge
                if(badgeContainer) {
                     badgeContainer.innerHTML = '<h1 class="verdict-text verdict-' + result.verdict.toLowerCase() + '">' + result.verdict + '</h1>';
                }
                
                // Advisor Text
                if(advisorText) advisorText.innerText = result.whyThisVerdict;
                
                // Factors - NO BACKTICKS
                if(factorsContainer) {
                    let html = '';
                    result.contributingFactors.forEach(f => {
                         html += '<div class="factor-item"><span style="color: var(--accent-primary); margin-right: var(--space-2);">✓</span>' + f + '</div>';
                    });
                    if (result.regionalContext && result.regionalContext.contextFactors) {
                        result.regionalContext.contextFactors.forEach(rf => {
                            html += '<div class="factor-item" style="background: rgba(var(--accent-primary-rgb), 0.05);"><span style="color: var(--text-tertiary); margin-right: var(--space-2);">📍</span>' + rf + '</div>';
                        });
                    }
                    factorsContainer.innerHTML = html;
                }

                // Action Steps
                const actionContainer = document.getElementById('action-steps-container');
                if (actionContainer) {
                    const isApproved = result.verdict === 'APPROVED';
                    const gap = Math.abs(fin.remainingBuffer);
                    const fmtGap = '$' + gap.toLocaleString();
                    
                    if (!isApproved) {
                        actionContainer.innerHTML = '<div class="step-item"><div class="step-number">1</div><div class="step-content"><h4>Bridge the Gap</h4><p id="action-step-desc">You are approximately <strong>' + fmtGap + '</strong> short of a comfortable move. Consider a co-signer.</p></div></div>';
                    } else {
                        actionContainer.innerHTML = '<div class="step-item"><div class="step-number" style="background: var(--success);">✓</div><div class="step-content"><h4>Application Ready</h4><p id="action-step-desc">This rent is within your <strong>safe financial zone</strong>. Proceed with documentation.</p></div></div>';
                    }
                }
                
                // Market Radar
                if(result.marketPosition) {
                    const mp = result.marketPosition;
                    if(radarLabel) radarLabel.innerText = mp.marketZone;
                    if(radarMarker) {
                         const maxRadar = mp.medianRent * 2.0;
                         let pct = (fin.monthlyRent / maxRadar) * 100;
                         if(pct > 100) pct = 100; if(pct < 0) pct = 0;
                         radarMarker.style.left = pct + "%";
                    }
                }
            }
        });
    </script>

    </div>
`)
