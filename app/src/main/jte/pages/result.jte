@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict
@import firstrentverdict.model.verdict.MarketPosition
@import firstrentverdict.model.verdict.RegionalContext
@import firstrentverdict.model.verdict.SafetyGap
@import firstrentverdict.model.verdict.FinancialLineItem
@import java.util.List
@import java.lang.Math

@param VerdictResult result
@param boolean noindex = true

@template.layout.main(
    title = "Verdict Report - First Rent Verdict",
    noindex = noindex,
    content = @`
    <!-- Main Result Container -->
    <div style="max-width: 900px; margin: 0 auto; padding: var(--space-8) var(--space-6);">
        
        <!-- 1. Navigation -->
        <div style="margin-bottom: var(--space-8);">
            <a href="/RentVerdict/" class="back-link">
                &larr; Start Over
            </a>
        </div>

        <!-- 2. Hero Verdict Section -->
        <div class="verdict-hero fade-in-up">
            <div class="verdict-label">OFFICIAL VERDICT</div>
            
            <!-- Verdict Badge -->
            <div style="margin-bottom: var(--space-4);">
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
                    <p style="font-size: 1rem; color: var(--text-secondary); line-height: 1.5;">
                        ${result.whyThisVerdict()}
                    </p>
                </div>
            </div>

            <!-- Key Contributing Factors (The "PROOF") -->
            <div class="factors-grid">
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
                <div class="card sim-card" style="margin-bottom: var(--space-8); border: 1px solid var(--border-default); box-shadow: 0 4px 12px rgba(0,0,0,0.05);">
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

                     <!-- Credit Score Toggle (NEW!) -->
                     <div class="form-group mb-0" style="padding: var(--space-3); background: var(--bg-secondary); border-radius: var(--radius-sm); border: 1px solid var(--border-default);">
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                            <div>
                                <span style="font-size: 0.875rem; font-weight: 600; color: var(--text-primary);">Credit Score</span>
                                <p style="font-size: 0.75rem; color: var(--text-tertiary); margin: 2px 0 0 0;">Affects deposit requirements</p>
                            </div>
                            <div style="display: flex; gap: var(--space-2);">
                                <button type="button" id="credit-good" class="credit-btn credit-active" 
                                        style="padding: 6px 12px; border-radius: 16px; font-size: 0.75rem; font-weight: 600; cursor: pointer; transition: all 0.2s; border: 1px solid var(--success); background: rgba(22, 163, 74, 0.1); color: var(--success);">
                                    Good+
                                </button>
                                <button type="button" id="credit-poor" class="credit-btn"
                                        style="padding: 6px 12px; border-radius: 16px; font-size: 0.75rem; font-weight: 600; cursor: pointer; transition: all 0.2s; border: 1px solid var(--border-default); background: transparent; color: var(--text-secondary);">
                                    Fair/Poor
                                </button>
                            </div>
                        </div>
                        <div id="credit-warning" style="display: none; margin-top: var(--space-2); padding: var(--space-2); background: rgba(220, 38, 38, 0.05); border-radius: var(--radius-sm); font-size: 0.75rem; color: var(--danger);">
                            ⚠️ Lower credit scores often require higher security deposits
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
                <div class="step-list">
                    @if(result.verdict() != Verdict.APPROVED)
                        <div class="step-item">
                            <div class="step-number">1</div>
                            <div class="step-content">
                                <h4>Bridge the Gap</h4>
                                <p>You are approximately <strong>$${String.format("%,d", Math.abs(result.financials().remainingBuffer()))}</strong> short of a comfortable move. Consider a co-signer.</p>
                            </div>
                        </div>
                    @else
                        <div class="step-item">
                            <div class="step-number" style="background: var(--success);">✓</div>
                            <div class="step-content">
                                <h4>Application Ready</h4>
                                <p>This rent is within your <strong>safe financial zone</strong>. Proceed with documentation.</p>
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
                        function shareTwitter() {
                            const text = "Checking my move-in costs for ${result.regionalContext().cityName()} with First Rent Verdict. The hidden costs are real! 🏠💸";
                            const url = "https://movecostinfo.com/RentVerdict";
                            window.open("https://twitter.com/intent/tweet?text=" + encodeURIComponent(text) + "&url=" + encodeURIComponent(url), "_blank");
                        }
                        function copyLink() {
                            navigator.clipboard.writeText("https://movecostinfo.com/RentVerdict");
                            alert("Link copied to clipboard!");
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
            const rentSlider = document.getElementById('rent-slider');
            const cashSlider = document.getElementById('cash-slider');
            const rentDisplay = document.getElementById('sim-rent-display');
            const cashDisplay = document.getElementById('sim-cash-display');
            const dataEl = document.getElementById('sim-data');
            
            const dispRecTotal = document.getElementById('receipt-total');
            const dispTotalCost = document.getElementById('disp-total-cost');
            const dispCash = document.getElementById('disp-cash');
            const dispBuffer = document.getElementById('disp-buffer');
            const lblBuffer = document.getElementById('lbl-buffer');
            const lblBufferStatus = document.getElementById('lbl-buffer-status');
            const cardBuffer = document.getElementById('card-buffer');
            const radarMarker = document.getElementById('radar-marker');
            const radarLabel = document.getElementById('radar-label');
            const simFeedback = document.getElementById('sim-feedback');
            const simNewBuffer = document.getElementById('sim-new-buffer');
            
            // Credit Score Toggle Elements
            const creditGoodBtn = document.getElementById('credit-good');
            const creditPoorBtn = document.getElementById('credit-poor');
            const creditWarning = document.getElementById('credit-warning');
            
            // Sticky Bar Elements
            const stickyBar = document.getElementById('sticky-sim-bar');
            const stickyVal = document.getElementById('sticky-buffer-val');
            const stickyStatus = document.getElementById('sticky-status-badge');

            if (!rentSlider || !cashSlider || !dataEl) return;

            const staticCosts = parseInt(dataEl.dataset.staticCosts);
            const depTyp = parseFloat(dataEl.dataset.depTyp); 
            const depHigh = parseFloat(dataEl.dataset.depHigh); 
            const recBuffer = parseInt(dataEl.dataset.recBuffer);
            const medianRent = parseInt(dataEl.dataset.medianRent);
            
            // Track credit score selection (default: good)
            let isCreditPoor = false;

            const fmt = (n) => '$' + Math.floor(n).toLocaleString();
            
            // Credit Score Toggle Handlers
            if (creditGoodBtn && creditPoorBtn) {
                creditGoodBtn.addEventListener('click', () => {
                    isCreditPoor = false;
                    creditGoodBtn.style.border = '1px solid var(--success)';
                    creditGoodBtn.style.background = 'rgba(22, 163, 74, 0.1)';
                    creditGoodBtn.style.color = 'var(--success)';
                    creditPoorBtn.style.border = '1px solid var(--border-default)';
                    creditPoorBtn.style.background = 'transparent';
                    creditPoorBtn.style.color = 'var(--text-secondary)';
                    if (creditWarning) creditWarning.style.display = 'none';
                    updateSimulation();
                });
                
                creditPoorBtn.addEventListener('click', () => {
                    isCreditPoor = true;
                    creditPoorBtn.style.border = '1px solid var(--danger)';
                    creditPoorBtn.style.background = 'rgba(220, 38, 38, 0.1)';
                    creditPoorBtn.style.color = 'var(--danger)';
                    creditGoodBtn.style.border = '1px solid var(--border-default)';
                    creditGoodBtn.style.background = 'transparent';
                    creditGoodBtn.style.color = 'var(--text-secondary)';
                    if (creditWarning) creditWarning.style.display = 'block';
                    updateSimulation();
                });
            }
            
            // Helper to update everything
            function updateSimulation() {
                const newRent = parseInt(rentSlider.value);
                const newCash = parseInt(cashSlider.value);
                
                rentDisplay.innerText = fmt(newRent);
                cashDisplay.innerText = fmt(newCash);
                dispCash.innerText = fmt(newCash); 
                
                // Dynamic Risk Logic: 
                // 1. If Credit is Poor/Fair -> always High Risk
                // 2. Else if Cash < Rent * 3 -> High Risk (cash-based)
                const isCashHighRisk = newCash < (newRent * 3);
                const isHighRisk = isCreditPoor || isCashHighRisk;
                const currentDepMult = isHighRisk ? depHigh : depTyp;
                
                // Label reflects why
                let riskLabel = "Standard";
                if (isCreditPoor && depHigh > depTyp) {
                    riskLabel = "Credit-Adjusted";
                } else if (isCashHighRisk && depHigh > depTyp) {
                    riskLabel = "Risk-Adjusted";
                }
                const noteVal = riskLabel + " (" + currentDepMult.toFixed(1) + "x Rent)";

                // Update Rent Line
                const rentValEl = document.getElementById('val-FirstMonthRent');
                if(rentValEl) rentValEl.innerText = fmt(newRent);
                
                // Update Deposit Line
                const newDeposit = newRent * currentDepMult;
                const depositValEl = document.getElementById('val-SecurityDeposit');
                if(depositValEl) depositValEl.innerText = fmt(newDeposit);
                
                // Update Deposit Note in Receipt (Visual Feedback!)
                const depositNoteEl = document.getElementById('note-SecurityDeposit');
                if(depositNoteEl) {
                    depositNoteEl.innerText = noteVal;
                    if(isHighRisk) depositNoteEl.style.color = "var(--danger)";
                    else depositNoteEl.style.color = "var(--text-tertiary)";
                }

                // Total
                const newTotalUpfront = newRent + newDeposit + staticCosts;
                
                dispRecTotal.innerText = fmt(newTotalUpfront);
                dispTotalCost.innerText = fmt(newTotalUpfront);
                
                const newBuffer = newCash - newTotalUpfront;
                
                dispBuffer.innerText = (newBuffer < 0 ? "-" : "") + fmt(Math.abs(newBuffer));
                simNewBuffer.innerText = (newBuffer < 0 ? "-" : "") + fmt(Math.abs(newBuffer));
                simFeedback.style.display = 'block';
                
                if (newBuffer < recBuffer) {
                    dispBuffer.style.color = "var(--danger)";
                    cardBuffer.className = "card"; 
                    cardBuffer.style.border = "1px solid var(--danger)";
                    cardBuffer.style.background = "#fff0f0";
                    simFeedback.style.borderLeftColor = "var(--danger)";
                    
                    if (newBuffer < 0) {
                        lblBuffer.innerText = "Deficit";
                        lblBufferStatus.innerText = "Insolvency Risk";
                    } else {
                        lblBuffer.innerText = "Remaining Buffer";
                        lblBufferStatus.innerText = "Below Safety Level";
                    }
                } else {
                    dispBuffer.style.color = "var(--success)";
                    cardBuffer.className = "card";
                    cardBuffer.style.border = "1px solid var(--success)";
                    cardBuffer.style.background = "#f0fff4";
                    simFeedback.style.borderLeftColor = "var(--success)";
                    lblBuffer.innerText = "Safety Buffer";
                    lblBufferStatus.innerText = "Healthy Reserve";
                }

                // Update Sticky Bar (Mobile)
                if(stickyBar && stickyVal && stickyStatus) {
                    stickyVal.innerText = (newBuffer < 0 ? "-" : "") + fmt(Math.abs(newBuffer));
                    if(newBuffer < 0) {
                        stickyStatus.innerText = "Insolvency Risk";
                        stickyStatus.style.background = "var(--danger-bg)";
                        stickyStatus.style.color = "var(--danger)";
                    } else if(newBuffer < recBuffer) {
                        stickyStatus.innerText = "Low Buffer";
                        stickyStatus.style.background = "var(--warning-bg)";
                        stickyStatus.style.color = "#a16207";
                    } else {
                        stickyStatus.innerText = "Safe";
                        stickyStatus.style.background = "var(--success-bg)";
                        stickyStatus.style.color = "var(--success)";
                    }
                }
                
                const maxRadar = medianRent * 2.0;
                let pct = (newRent / maxRadar) * 100;
                if(pct > 100) pct = 100;
                if(pct < 0) pct = 0;
                radarMarker.style.left = pct + "%";
                
                // Update Radar Label
                if(newRent <= (medianRent * 0.8)) radarLabel.innerText = "Below Market";
                else if(newRent >= (medianRent * 1.2)) radarLabel.innerText = "Premium Range";
                else radarLabel.innerText = "Market Standard";
            }

            rentSlider.addEventListener('input', updateSimulation);
            cashSlider.addEventListener('input', updateSimulation);

            // Mobile Sticky Bar Observer
            // Show sticky bar only when Simulation Card is visible/active
            const simCard = document.querySelector('.sim-card');
            if(simCard && stickyBar && window.innerWidth <= 768) {
                const observer = new IntersectionObserver((entries) => {
                    entries.forEach(entry => {
                        // If sim card is intersecting (even slightly), show sticky bar
                        // Or maybe better: if card is in viewport.
                        // Let's optimize: Show when card is visibly focused.
                        if (entry.isIntersecting) {
                            stickyBar.style.display = 'flex';
                        } else {
                             stickyBar.style.display = 'none';
                        }
                    });
                }, { threshold: 0.1 });
                
                observer.observe(simCard);
            }

            // Initial calculation to set defaults
            updateSimulation();
        });
    </script>
`)
