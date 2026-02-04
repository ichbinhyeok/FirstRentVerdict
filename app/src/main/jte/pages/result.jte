@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict
@import firstrentverdict.model.verdict.VerdictInput
@import firstrentverdict.model.verdict.MarketPosition
@import firstrentverdict.model.verdict.RegionalContext
@import firstrentverdict.model.verdict.SafetyGap
@import firstrentverdict.model.verdict.FinancialLineItem
@import firstrentverdict.model.verdict.CreditTier
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

                     <!-- 3. Advanced Simulation Controls (Premium Design) -->
                     <div class="sim-controls-panel">
                        
                        <!-- Move Type - Pill Toggle -->
                        <div class="sim-control-group">
                            <label class="sim-label">
                                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
                                Move Type
                            </label>
                            <div class="toggle-pills">
                                <label class="toggle-pill">
                                    <input type="radio" name="moveType" value="local" checked="${input.isLocalMove()}">
                                    <span class="pill-content">
                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
                                        Local
                                    </span>
                                </label>
                                <label class="toggle-pill">
                                    <input type="radio" name="moveType" value="long" checked="${!input.isLocalMove()}">
                                    <span class="pill-content">
                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/></svg>
                                        Long Distance
                                    </span>
                                </label>
                            </div>
                        </div>

                        <!-- Long Distance Panel (Conditional) -->
                        <div id="long-dist-inputs" class="long-dist-panel" style="display: ${!input.isLocalMove() ? "block" : "none"};">
                            <div class="origin-select-wrapper">
                                <label class="origin-label">
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="10" r="3"/><path d="M12 21.7C17.3 17 20 13 20 10a8 8 0 1 0-16 0c0 3 2.7 7 8 11.7z"/></svg>
                                    Origin City
                                </label>
                                <select id="sim-from-location" class="origin-select">
                                    <option value="">Select where you're moving from</option>
                                </select>
                                <input type="hidden" id="sim-from-city" value="">
                                <input type="hidden" id="sim-from-state" value="">
                            </div>
                            <div id="distance-preview" class="distance-badge" style="display: none;">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 3l4 4-4 4"/><path d="M3 11h18"/><path d="M7 21l-4-4 4-4"/><path d="M21 13H3"/></svg>
                                <span id="distance-miles">0</span> miles
                            </div>
                        </div>

                        <!-- Credit Profile - Enhanced Dropdown -->
                        <div class="sim-control-group">
                            <label class="sim-label">
                                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
                                Credit Profile
                            </label>
                            <div class="credit-select-wrapper">
                                <select id="sim-credit" class="credit-select">
                                    <option value="EXCELLENT" selected="${input.creditTier() == CreditTier.EXCELLENT}">✨ Excellent (750+)</option>
                                    <option value="GOOD" selected="${input.creditTier() == CreditTier.GOOD}">👍 Good (670-749)</option>
                                    <option value="FAIR" selected="${input.creditTier() == CreditTier.FAIR}">📊 Fair (580-669)</option>
                                    <option value="POOR" selected="${input.creditTier() == CreditTier.POOR}">⚠️ Poor (&lt; 580)</option>
                                </select>
                            </div>
                        </div>
                         
                        <!-- Pet Toggle - Modern Switch -->
                        <div class="sim-control-group" style="padding-top: var(--space-2);">
                            <label class="modern-toggle">
                                <input type="checkbox" id="sim-pet" checked="${input.hasPet()}">
                                <span class="toggle-track">
                                    <span class="toggle-thumb"></span>
                                </span>
                                <span class="toggle-label">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 21a9 9 0 1 0 0-18 9 9 0 0 0 0 18z"/><path d="M8 14s1.5 2 4 2 4-2 4-2"/><line x1="9" y1="9" x2="9.01" y2="9"/><line x1="15" y1="9" x2="15.01" y2="9"/></svg>
                                    Moving with a pet
                                </span>
                            </label>
                        </div>
                     </div>
                     
                     <!-- Inline Styles for Premium Controls -->
                     <style>
                         .sim-controls-panel {
                             background: linear-gradient(135deg, rgba(250,250,250,0.9) 0%, rgba(244,244,245,0.95) 100%);
                             backdrop-filter: blur(8px);
                             padding: var(--space-5);
                             border-radius: var(--radius-md);
                             border: 1px solid var(--border-default);
                             margin-top: var(--space-4);
                             box-shadow: inset 0 1px 0 rgba(255,255,255,0.5);
                         }
                         
                         .sim-control-group {
                             margin-bottom: var(--space-5);
                         }
                         .sim-control-group:last-child {
                             margin-bottom: 0;
                         }
                         
                         .sim-label {
                             display: flex;
                             align-items: center;
                             gap: 6px;
                             font-size: 0.6875rem;
                             font-weight: 700;
                             text-transform: uppercase;
                             letter-spacing: 0.08em;
                             color: var(--text-tertiary);
                             margin-bottom: var(--space-2);
                         }
                         .sim-label svg {
                             opacity: 0.6;
                         }
                         
                         /* Pill Toggle */
                         .toggle-pills {
                             display: flex;
                             gap: 8px;
                         }
                         .toggle-pill {
                             flex: 1;
                             cursor: pointer;
                         }
                         .toggle-pill input {
                             display: none;
                         }
                         .pill-content {
                             display: flex;
                             align-items: center;
                             justify-content: center;
                             gap: 6px;
                             padding: 10px 12px;
                             background: var(--bg-secondary);
                             border: 1px solid var(--border-default);
                             border-radius: var(--radius-sm);
                             font-size: 0.8125rem;
                             font-weight: 500;
                             color: var(--text-secondary);
                             transition: all 0.2s ease;
                         }
                         .toggle-pill:hover .pill-content {
                             border-color: var(--accent-primary);
                             background: rgba(99, 102, 241, 0.04);
                         }
                         .toggle-pill input:checked + .pill-content {
                             background: var(--accent-gradient);
                             color: white;
                             border-color: transparent;
                             box-shadow: 0 2px 8px rgba(99, 102, 241, 0.3);
                         }
                         .toggle-pill input:checked + .pill-content svg {
                             stroke: white;
                         }
                         
                         /* Long Distance Panel */
                         .long-dist-panel {
                             background: linear-gradient(135deg, rgba(99,102,241,0.03) 0%, rgba(139,92,246,0.05) 100%);
                             border: 1px solid rgba(99, 102, 241, 0.15);
                             border-radius: var(--radius-sm);
                             padding: var(--space-4);
                             margin-bottom: var(--space-4);
                             animation: slideDown 0.25s ease;
                         }
                         @keyframes slideDown {
                             from { opacity: 0; transform: translateY(-8px); }
                             to { opacity: 1; transform: translateY(0); }
                         }
                         
                         .origin-label {
                             display: flex;
                             align-items: center;
                             gap: 6px;
                             font-size: 0.75rem;
                             font-weight: 600;
                             color: var(--accent-primary);
                             margin-bottom: 8px;
                         }
                         .origin-label svg {
                             stroke: var(--accent-primary);
                         }
                         
                         .origin-select {
                             width: 100%;
                             padding: 12px 14px;
                             font-size: 0.875rem;
                             font-weight: 500;
                             background: var(--bg-secondary);
                             border: 1px solid var(--border-default);
                             border-radius: var(--radius-sm);
                             cursor: pointer;
                             transition: all 0.15s ease;
                         }
                         .origin-select:hover {
                             border-color: var(--accent-primary);
                         }
                         .origin-select:focus {
                             outline: none;
                             border-color: var(--accent-primary);
                             box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.12);
                         }
                         
                         .distance-badge {
                             display: inline-flex;
                             align-items: center;
                             gap: 6px;
                             margin-top: 12px;
                             padding: 8px 14px;
                             background: linear-gradient(135deg, var(--accent-primary) 0%, var(--accent-secondary) 100%);
                             color: white;
                             font-size: 0.8125rem;
                             font-weight: 600;
                             border-radius: var(--radius-full);
                             animation: popIn 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
                         }
                         @keyframes popIn {
                             from { transform: scale(0.8); opacity: 0; }
                             to { transform: scale(1); opacity: 1; }
                         }
                         .distance-badge svg {
                             stroke: white;
                         }
                         
                         /* Credit Dropdown */
                         .credit-select-wrapper {
                             position: relative;
                         }
                         .credit-select {
                             width: 100%;
                             padding: 12px 14px;
                             font-size: 0.875rem;
                             font-weight: 500;
                             background: var(--bg-secondary);
                             border: 1px solid var(--border-default);
                             border-radius: var(--radius-sm);
                             cursor: pointer;
                             transition: all 0.15s ease;
                             appearance: none;
                             background-image: url("data:image/svg+xml,%3Csvg width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%2371717a' stroke-width='2' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M6 9l6 6 6-6'/%3E%3C/svg%3E");
                             background-repeat: no-repeat;
                             background-position: right 12px center;
                             padding-right: 36px;
                         }
                         .credit-select:hover {
                             border-color: var(--text-tertiary);
                         }
                         .credit-select:focus {
                             outline: none;
                             border-color: var(--accent-primary);
                             box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.12);
                         }
                         
                         /* Modern Toggle Switch */
                         .modern-toggle {
                             display: flex;
                             align-items: center;
                             gap: 12px;
                             cursor: pointer;
                             padding: 8px 0;
                         }
                         .modern-toggle input {
                             display: none;
                         }
                         .toggle-track {
                             position: relative;
                             width: 44px;
                             height: 24px;
                             background: var(--border-default);
                             border-radius: 12px;
                             transition: background 0.2s ease;
                             flex-shrink: 0;
                         }
                         .toggle-thumb {
                             position: absolute;
                             top: 2px;
                             left: 2px;
                             width: 20px;
                             height: 20px;
                             background: white;
                             border-radius: 50%;
                             box-shadow: 0 1px 3px rgba(0,0,0,0.15);
                             transition: transform 0.2s cubic-bezier(0.34, 1.56, 0.64, 1);
                         }
                         .modern-toggle input:checked + .toggle-track {
                             background: var(--accent-gradient);
                         }
                         .modern-toggle input:checked + .toggle-track .toggle-thumb {
                             transform: translateX(20px);
                         }
                         .toggle-label {
                             display: flex;
                             align-items: center;
                             gap: 6px;
                             font-size: 0.875rem;
                             font-weight: 500;
                             color: var(--text-secondary);
                         }
                         .toggle-label svg {
                             opacity: 0.7;
                         }
                         
                         /* Mobile Responsive */
                         @media (max-width: 480px) {
                             .sim-controls-panel {
                                 padding: var(--space-4);
                             }
                             .toggle-pills {
                                 flex-direction: column;
                                 gap: 6px;
                             }
                             .pill-content {
                                 padding: 12px;
                                 font-size: 0.875rem;
                             }
                             .sim-label {
                                 font-size: 0.625rem;
                             }
                             .origin-select,
                             .credit-select {
                                 padding: 10px 12px;
                                 font-size: 0.8125rem;
                             }
                             .modern-toggle {
                                 gap: 10px;
                             }
                             .toggle-label {
                                 font-size: 0.8125rem;
                             }
                             .toggle-track {
                                 width: 40px;
                                 height: 22px;
                             }
                             .toggle-thumb {
                                 width: 18px;
                                 height: 18px;
                             }
                             .modern-toggle input:checked + .toggle-track .toggle-thumb {
                                 transform: translateX(18px);
                             }
                         }
                     </style>
                     
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
            // -- Supported Cities with Coordinates (for distance calculation) --
            const SUPPORTED_CITIES = [
                {city:"New York",state:"NY",lat:40.7128,lng:-74.006},{city:"Los Angeles",state:"CA",lat:34.0522,lng:-118.2437},
                {city:"Chicago",state:"IL",lat:41.8781,lng:-87.6298},{city:"Houston",state:"TX",lat:29.7604,lng:-95.3698},
                {city:"Phoenix",state:"AZ",lat:33.4484,lng:-112.074},{city:"Philadelphia",state:"PA",lat:39.9526,lng:-75.1652},
                {city:"San Antonio",state:"TX",lat:29.4241,lng:-98.4936},{city:"San Diego",state:"CA",lat:32.7157,lng:-117.1611},
                {city:"Dallas",state:"TX",lat:32.7767,lng:-96.797},{city:"San Jose",state:"CA",lat:37.3382,lng:-121.8863},
                {city:"Austin",state:"TX",lat:30.2672,lng:-97.7431},{city:"Jacksonville",state:"FL",lat:30.3322,lng:-81.6557},
                {city:"Fort Worth",state:"TX",lat:32.7555,lng:-97.3308},{city:"Columbus",state:"OH",lat:39.9612,lng:-82.9988},
                {city:"Indianapolis",state:"IN",lat:39.7684,lng:-86.1581},{city:"Charlotte",state:"NC",lat:35.2271,lng:-80.8431},
                {city:"San Francisco",state:"CA",lat:37.7749,lng:-122.4194},{city:"Seattle",state:"WA",lat:47.6062,lng:-122.3321},
                {city:"Denver",state:"CO",lat:39.7392,lng:-104.9903},{city:"Oklahoma City",state:"OK",lat:35.4676,lng:-97.5164},
                {city:"Nashville",state:"TN",lat:36.1627,lng:-86.7816},{city:"El Paso",state:"TX",lat:31.7619,lng:-106.485},
                {city:"Washington",state:"DC",lat:38.9072,lng:-77.0369},{city:"Las Vegas",state:"NV",lat:36.1699,lng:-115.1398},
                {city:"Boston",state:"MA",lat:42.3601,lng:-71.0589},{city:"Portland",state:"OR",lat:45.5152,lng:-122.6784},
                {city:"Louisville",state:"KY",lat:38.2527,lng:-85.7585},{city:"Memphis",state:"TN",lat:35.1495,lng:-90.049},
                {city:"Detroit",state:"MI",lat:42.3314,lng:-83.0458},{city:"Baltimore",state:"MD",lat:39.2904,lng:-76.6122},
                {city:"Milwaukee",state:"WI",lat:43.0389,lng:-87.9065},{city:"Albuquerque",state:"NM",lat:35.0844,lng:-106.6504},
                {city:"Tucson",state:"AZ",lat:32.2226,lng:-110.9747},{city:"Fresno",state:"CA",lat:36.7378,lng:-119.7871},
                {city:"Sacramento",state:"CA",lat:38.5816,lng:-121.4944},{city:"Kansas City",state:"MO",lat:39.0997,lng:-94.5786},
                {city:"Mesa",state:"AZ",lat:33.4152,lng:-111.8315},{city:"Atlanta",state:"GA",lat:33.749,lng:-84.388},
                {city:"Omaha",state:"NE",lat:41.2565,lng:-95.9345},{city:"Colorado Springs",state:"CO",lat:38.8339,lng:-104.8214},
                {city:"Raleigh",state:"NC",lat:35.7796,lng:-78.6382},{city:"Long Beach",state:"CA",lat:33.7701,lng:-118.1937},
                {city:"Virginia Beach",state:"VA",lat:36.8529,lng:-75.978},{city:"Miami",state:"FL",lat:25.7617,lng:-80.1918},
                {city:"Oakland",state:"CA",lat:37.8044,lng:-122.2711},{city:"Minneapolis",state:"MN",lat:44.9778,lng:-93.265},
                {city:"Tulsa",state:"OK",lat:36.154,lng:-95.9928},{city:"Bakersfield",state:"CA",lat:35.3733,lng:-119.0187},
                {city:"Wichita",state:"KS",lat:37.6872,lng:-97.3301},{city:"Arlington",state:"TX",lat:32.7357,lng:-97.1081},
                {city:"Aurora",state:"CO",lat:39.7294,lng:-104.8319},{city:"Tampa",state:"FL",lat:27.9506,lng:-82.4572},
                {city:"New Orleans",state:"LA",lat:29.9511,lng:-90.0715},{city:"Cleveland",state:"OH",lat:41.4993,lng:-81.6944},
                {city:"Honolulu",state:"HI",lat:21.3069,lng:-157.8583},{city:"Anaheim",state:"CA",lat:33.8366,lng:-117.9143},
                {city:"Lexington",state:"KY",lat:38.0406,lng:-84.5037},{city:"Stockton",state:"CA",lat:37.9577,lng:-121.2908},
                {city:"Corpus Christi",state:"TX",lat:27.8006,lng:-97.3964},{city:"Henderson",state:"NV",lat:36.0395,lng:-114.9817},
                {city:"Riverside",state:"CA",lat:33.9806,lng:-117.3755},{city:"Saint Paul",state:"MN",lat:44.9537,lng:-93.09},
                {city:"Newark",state:"NJ",lat:40.7357,lng:-74.1724},{city:"Santa Ana",state:"CA",lat:33.7456,lng:-117.8677},
                {city:"Cincinnati",state:"OH",lat:39.1031,lng:-84.512},{city:"Irvine",state:"CA",lat:33.6846,lng:-117.8265},
                {city:"Orlando",state:"FL",lat:28.5383,lng:-81.3792},{city:"Pittsburgh",state:"PA",lat:40.4406,lng:-79.9959},
                {city:"St. Louis",state:"MO",lat:38.627,lng:-90.1994},{city:"Greensboro",state:"NC",lat:36.0726,lng:-79.792},
                {city:"Jersey City",state:"NJ",lat:40.7178,lng:-74.0431},{city:"Anchorage",state:"AK",lat:61.2181,lng:-149.9003},
                {city:"Lincoln",state:"NE",lat:40.8136,lng:-96.7026},{city:"Plano",state:"TX",lat:33.0198,lng:-96.6989},
                {city:"Durham",state:"NC",lat:35.994,lng:-78.8986},{city:"Buffalo",state:"NY",lat:42.8864,lng:-78.8784},
                {city:"Chandler",state:"AZ",lat:33.3062,lng:-111.8413},{city:"Chula Vista",state:"CA",lat:32.6401,lng:-117.0842},
                {city:"Toledo",state:"OH",lat:41.6528,lng:-83.5379},{city:"Madison",state:"WI",lat:43.0731,lng:-89.4012},
                {city:"Gilbert",state:"AZ",lat:33.3528,lng:-111.789},{city:"Reno",state:"NV",lat:39.5296,lng:-119.8138},
                {city:"Fort Wayne",state:"IN",lat:41.0793,lng:-85.1394},{city:"North Las Vegas",state:"NV",lat:36.1989,lng:-115.1175},
                {city:"St. Petersburg",state:"FL",lat:27.7676,lng:-82.6403},{city:"Lubbock",state:"TX",lat:33.5779,lng:-101.8552},
                {city:"Laredo",state:"TX",lat:27.5036,lng:-99.5076},{city:"Irving",state:"TX",lat:32.814,lng:-96.9489},
                {city:"Chesapeake",state:"VA",lat:36.7682,lng:-76.2875},{city:"Glendale",state:"AZ",lat:33.5387,lng:-112.186},
                {city:"Winston-Salem",state:"NC",lat:36.0999,lng:-80.2442},{city:"Scottsdale",state:"AZ",lat:33.4942,lng:-111.9261},
                {city:"Garland",state:"TX",lat:32.9126,lng:-96.6389},{city:"Boise",state:"ID",lat:43.615,lng:-116.2023},
                {city:"Norfolk",state:"VA",lat:36.8508,lng:-76.2859},{city:"Port St. Lucie",state:"FL",lat:27.273,lng:-80.3582},
                {city:"Spokane",state:"WA",lat:47.6588,lng:-117.426},{city:"Richmond",state:"VA",lat:37.5407,lng:-77.436},
                {city:"Fremont",state:"CA",lat:37.5485,lng:-121.9886},{city:"Huntsville",state:"AL",lat:34.7304,lng:-86.5861},
                {city:"Tacoma",state:"WA",lat:47.2529,lng:-122.4443},{city:"Baton Rouge",state:"LA",lat:30.4515,lng:-91.1871},
                {city:"Santa Clarita",state:"CA",lat:34.3917,lng:-118.5426},{city:"San Bernardino",state:"CA",lat:34.1083,lng:-117.2898},
                {city:"Hialeah",state:"FL",lat:25.8576,lng:-80.2781},{city:"Frisco",state:"TX",lat:33.1507,lng:-96.8236},
                {city:"Modesto",state:"CA",lat:37.6391,lng:-120.9969},{city:"Cape Coral",state:"FL",lat:26.5629,lng:-81.9495},
                {city:"Fontana",state:"CA",lat:34.0922,lng:-117.435}
            ];
            
            // Current destination (from page context)
            const DEST_CITY = "${input.city()}";
            const DEST_STATE = "${input.state()}";
            const destCoord = SUPPORTED_CITIES.find(c => c.city === DEST_CITY && c.state === DEST_STATE);
            
            // -- Populate Location Dropdown (Grouped by State) --
            const locationSelect = document.getElementById('sim-from-location');
            if (locationSelect) {
                // Group cities by state
                const byState = {};
                SUPPORTED_CITIES.forEach(c => {
                    if (!byState[c.state]) byState[c.state] = [];
                    byState[c.state].push(c);
                });
                
                // Sort states alphabetically
                const sortedStates = Object.keys(byState).sort();
                
                sortedStates.forEach(state => {
                    const optgroup = document.createElement('optgroup');
                    optgroup.label = state;
                    
                    // Sort cities within state
                    byState[state].sort((a, b) => a.city.localeCompare(b.city)).forEach(c => {
                        // Don't include current destination city
                        if (c.city === DEST_CITY && c.state === DEST_STATE) return;
                        
                        const opt = document.createElement('option');
                        opt.value = c.city + '|' + c.state;
                        opt.textContent = c.city;
                        optgroup.appendChild(opt);
                    });
                    
                    if (optgroup.children.length > 0) {
                        locationSelect.appendChild(optgroup);
                    }
                });
                
                // Handle selection change
                locationSelect.addEventListener('change', (e) => {
                    const val = e.target.value;
                    if (val) {
                        const [city, state] = val.split('|');
                        document.getElementById('sim-from-city').value = city;
                        document.getElementById('sim-from-state').value = state;
                        
                        // Calculate and show distance preview
                        const fromCoord = SUPPORTED_CITIES.find(c => c.city === city && c.state === state);
                        if (fromCoord && destCoord) {
                            const miles = calculateDistance(fromCoord.lat, fromCoord.lng, destCoord.lat, destCoord.lng);
                            document.getElementById('distance-miles').textContent = Math.round(miles).toLocaleString();
                            document.getElementById('distance-preview').style.display = 'block';
                        }
                    } else {
                        document.getElementById('sim-from-city').value = '';
                        document.getElementById('sim-from-state').value = '';
                        document.getElementById('distance-preview').style.display = 'none';
                    }
                    triggerSimulation();
                });
            }
            
            // Haversine distance calculation
            function calculateDistance(lat1, lng1, lat2, lng2) {
                const R = 3958.8; // Earth radius in miles
                const dLat = (lat2 - lat1) * Math.PI / 180;
                const dLng = (lng2 - lng1) * Math.PI / 180;
                const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                          Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                          Math.sin(dLng/2) * Math.sin(dLng/2);
                const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                return R * c;
            }
            
            // -- Elements --
            const rentSlider = document.getElementById('rent-slider');
            const cashSlider = document.getElementById('cash-slider');
            const rentDisplay = document.getElementById('sim-rent-display');
            const cashDisplay = document.getElementById('sim-cash-display');
            
            // New Inputs
            const moveTypeRadios = document.getElementsByName('moveType');
            const simCredit = document.getElementById('sim-credit');
            const simPet = document.getElementById('sim-pet');

            const longDistInputs = document.getElementById('long-dist-inputs');
            const simFromCity = document.getElementById('sim-from-city');
            const simFromState = document.getElementById('sim-from-state');

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
                r.addEventListener('change', (e) => {
                    if (longDistInputs) longDistInputs.style.display = e.target.value === 'long' ? 'block' : 'none';
                    triggerSimulation();
                });
            });

            // -- Event Listeners --
            [rentSlider, cashSlider, simCredit, simPet].forEach(el => {
                if(el) {
                    const eventType = (el.tagName === 'SELECT' || el.type === 'checkbox' || el.type === 'radio') ? 'change' : 'input';
                    el.addEventListener(eventType, triggerSimulation);
                    // Also listen for input on sliders for instant value updates
                    if (el.type === 'range') {
                        el.addEventListener('input', () => {
                            if (el.id === 'rent-slider') rentDisplay.innerText = fmt(el.value);
                            if (el.id === 'cash-slider') cashDisplay.innerText = fmt(el.value);
                        });
                    }
                }
            });

            function triggerSimulation() {
                // Immediate UI feedback for sliders
                if (rentDisplay) rentDisplay.innerText = fmt(rentSlider.value);
                if (cashDisplay) cashDisplay.innerText = fmt(cashSlider.value);

                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(fetchSimulation, 500);
            }

            async function fetchSimulation() {
                // 1. Gather Data
                const cityState = "${input.city()}|${input.state()}"; // Original target city
                const parts = cityState.split('|');
                const selectedMoveType = document.querySelector('input[name="moveType"]:checked');
                const isLongDist = selectedMoveType ? selectedMoveType.value === 'long' : false;
                
                const payload = {
                    city: parts[0],
                    state: parts[1],
                    monthlyRent: parseInt(rentSlider.value),
                    availableCash: parseInt(cashSlider.value),
                    hasPet: simPet ? simPet.checked : false,
                    isLocalMove: !isLongDist,
                    creditTier: simCredit ? simCredit.value : 'GOOD',
                    fromCity: (isLongDist && simFromCity) ? simFromCity.value : null,
                    fromState: (isLongDist && simFromState) ? simFromState.value : null,
                    moveInDate: null
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
                    console.error("Simulation error:", e);
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
                
                // Update Mobile Sticky Bar
                updateStickyBar(result.verdict, fin.remainingBuffer, fin.recommendedBuffer);
            }
            
            // -- Mobile Sticky Bar Logic --
            const stickyBar = document.getElementById('sticky-sim-bar');
            const stickyBufferVal = document.getElementById('sticky-buffer-val');
            const stickyStatusBadge = document.getElementById('sticky-status-badge');
            const heroSection = document.querySelector('.verdict-hero');
            
            function updateStickyBar(verdict, buffer, recBuffer) {
                if (!stickyBufferVal || !stickyStatusBadge) return;
                
                // Update buffer value
                const prefix = buffer < 0 ? '-' : '';
                stickyBufferVal.textContent = prefix + fmt(Math.abs(buffer));
                stickyBufferVal.style.color = buffer < recBuffer ? 'var(--danger)' : 'var(--success)';
                
                // Update verdict badge
                let badgeColor, badgeText;
                if (verdict === 'APPROVED') {
                    badgeColor = 'var(--success)';
                    badgeText = '✓ APPROVED';
                } else if (verdict === 'BORDERLINE') {
                    badgeColor = 'var(--warning)';
                    badgeText = '⚠ BORDERLINE';
                } else {
                    badgeColor = 'var(--danger)';
                    badgeText = '✗ DENIED';
                }
                stickyStatusBadge.style.background = badgeColor;
                stickyStatusBadge.style.color = '#fff';
                stickyStatusBadge.textContent = badgeText;
            }
            
            // Initialize sticky bar with current values
            updateStickyBar(
                '${result.verdict().name()}',
                ${result.financials().remainingBuffer()},
                ${result.financials().recommendedBuffer()}
            );
            
            // Show/hide sticky bar on scroll (mobile only)
            if (stickyBar && heroSection) {
                let stickyVisible = false;
                const isMobile = window.innerWidth <= 768;
                
                function checkStickyVisibility() {
                    if (!isMobile) {
                        stickyBar.style.display = 'none';
                        return;
                    }
                    
                    const heroBottom = heroSection.getBoundingClientRect().bottom;
                    const shouldShow = heroBottom < 0; // Hero is scrolled past viewport
                    
                    if (shouldShow && !stickyVisible) {
                        stickyBar.style.display = 'flex';
                        stickyVisible = true;
                    } else if (!shouldShow && stickyVisible) {
                        stickyBar.style.display = 'none';
                        stickyVisible = false;
                    }
                }
                
                window.addEventListener('scroll', checkStickyVisibility, { passive: true });
                window.addEventListener('resize', () => {
                    // Recalculate on resize
                    if (window.innerWidth > 768) {
                        stickyBar.style.display = 'none';
                        stickyVisible = false;
                    }
                });
                
                // Initial check
                checkStickyVisibility();
            }
        });
    </script>

    </div>
`)
