@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict
@import firstrentverdict.model.verdict.MarketPosition

@param VerdictResult result
@param boolean noindex = true

@template.layout.main(
    title = "Your Rent Verdict Result - Confidential",
    noindex = true,
    content = @`
    <div style="max-width: 720px; margin: 4rem auto; padding: 0 2rem;">
        
        <div class="back-link-container">
            <a href="/RentVerdict/" class="back-link">
                &larr; Return to Engine
            </a>
        </div>

        <div class="verdict-section">
            <h3 class="verdict-label">Final Decision</h3>
            <div id="verdict-badge">
                @if(result.verdict() == Verdict.APPROVED)
                    <div class="verdict-text verdict-approved">APPROVED</div>
                @elseif(result.verdict() == Verdict.BORDERLINE)
                    <div class="verdict-text verdict-borderline">BORDERLINE</div>
                @else
                    <div class="verdict-text verdict-denied">DENIED</div>
                @endif
            </div>

            <!-- MATH PROOF: Now Integrated into Safety Gap Card -->
            @if(result.verdict() != Verdict.APPROVED)
               <!-- Proof moved to Safety Gap -->
            @endif
        </div>

        <div id="safety-gap-container">
            @if(result.safetyGap() != null)
                @if(result.safetyGap().isApproved())
                    <div class="safety-gap-card safety-gap-approved">
                        <div id="safety-gap-amount" class="safety-gap-amount">${result.safetyGap().displayText()}</div>
                        <div id="safety-gap-action" class="safety-gap-action">${result.safetyGap().actionPrompt()}</div>
                    </div>
                @else
                    <div class="safety-gap-card safety-gap-denied">
                        <div id="safety-gap-amount" class="safety-gap-amount">${result.safetyGap().displayText()}</div>
                        <!-- REPLACED Action Prompt with MATH PROOF -->
                         <div class="math-proof" style="background: transparent; padding: 0; color: var(--signal-error); font-weight: 500; font-size: 0.85rem;">
                            Total Upfront $${String.format("%,d", result.financials().totalUpfrontCost())} — Available Cash $${String.format("%,d", result.financials().availableCash())}
                        </div>
                    </div>
                @endif
            @endif
        </div>

        <div class="why-verdict-section" style="display: ${result.whyThisVerdict() != null ? "block" : "none"};">
            <h3 class="section-title" style="font-weight: 700; letter-spacing: 0.02em;">WHY THIS VERDICT</h3>
            <p id="why-text" class="why-text" style="font-weight: 400; color: var(--text-primary);">
                ${result.whyThisVerdict() != null ? result.whyThisVerdict() : ""}
            </p>
            <div id="bottleneck-container" class="bottleneck-container" style="display: ${result.primaryBottleneck() != null ? "block" : "none"};">
                <span class="bottleneck-label">Primary Bottleneck:</span>
                <span id="bottleneck-text" class="bottleneck-value">${result.primaryBottleneck() != null ? result.primaryBottleneck() : ""}</span>
            </div>
        </div>

        @if(result.contributingFactors() != null && !result.contributingFactors().isEmpty())
            <div class="contributing-section">
                <h4 class="contributing-title" style="font-weight: 700; letter-spacing: 0.02em;">CONTRIBUTING FACTORS</h4>
                <ul class="contributing-list">
                    @for(String factor : result.contributingFactors())
                        <li class="contributing-item">
                            <span class="contributing-bullet">→</span>
                            ${factor}
                        </li>
                    @endfor
                </ul>
            </div>
        @endif

        @if(result.regionalContext() != null)
            <details class="regional-details">
                <summary class="regional-summary">REGION: ${result.regionalContext().cityName().toUpperCase()}</summary>
                <div class="market-warning" style="display: ${result.verdict() == Verdict.DENIED && result.marketPosition().marketZone().equals("Below Market") ? "block" : "none"}; margin-top: 0.5rem; font-size: 0.9rem; color: var(--text-secondary);">
                    <strong>Market Position: Below Market.</strong> However, affordability fails due to upfront cash constraints.
                </div>
                <ul class="contributing-list" style="margin-top: 1rem;">
                    @for(String context : result.regionalContext().contextFactors())
                        <li class="contributing-item" style="font-size: 0.95rem;">
                            <span class="contributing-bullet">•</span>
                            ${context}
                        </li>
                    @endfor
                </ul>
            </details>
        @endif


        <!-- Recovery Hint Box (Deterministic) -->
        <div id="recovery-hint" class="recovery-hint-box" style="display: ${result.verdict() != Verdict.APPROVED ? "block" : "none"};">
            <h4 class="verdict-label" style="margin-bottom: 1rem;">To Reach Approved Status:</h4>
            <div style="font-size: 1rem; color: var(--text-primary); line-height: 1.5;">
                You must meet <strong>ONE</strong> of the following conditions:
                <ul style="margin: 0.5rem 0 0 0; padding-left: 1.5rem;">
                    <li style="margin-bottom: 0.5rem;">
                        @if(((result.financials().availableCash() - result.financials().staticCosts() - result.financials().recommendedBuffer()) / result.financials().upfrontBaseMultiplier()) < 0)
                             Reduce rent to <strong>Option Unavailable</strong>
                             <div style="color: var(--signal-error); font-size: 0.85rem; margin-top: 0.5rem;">
                                Current cash level cannot support any rental scenario.
                            </div>
                        @else
                             Reduce rent to <strong>$<span id="target-rent">
                             ${String.format("%,d", (int) Math.floor((result.financials().availableCash() - result.financials().staticCosts() - result.financials().recommendedBuffer()) / result.financials().upfrontBaseMultiplier()))}
                             </span></strong> or lower
                        @endif
                    </li>
                    <li>
                        Add <strong>$<span id="target-cash">
                        ${String.format("%,d", Math.max(0, result.financials().recommendedBuffer() - result.financials().remainingBuffer()))}
                        </span></strong> in available cash
                        <div style="font-size: 0.8rem; color: var(--text-tertiary); margin-top: 0.2rem;">
                            (Includes upfront shortfall + required post-move safety buffer)
                        </div>
                    </li>
                </ul>
            </div>
             <div id="target-rent-error" style="display:none; color: var(--signal-error); font-size: 0.85rem; margin-top: 0.5rem;">
                Current cash level cannot support any rental scenario.
            </div>
        </div>

        <div class="simulation-lab">
            <h4>Simulation Lab: Change the Variables</h4>
            <div class="sim-controls">
                <div class="control-group">
                    <label>Monthly Rent: $<span id="rent-val">${result.financials().monthlyRent()}</span></label>
                    <input type="range" id="rent-slider" 
                           min="500" 
                           max="${(int)Math.max(3000, Math.max(result.financials().monthlyRent() * 1.5, result.marketPosition().p75Rent() * 1.5))}" 
                           value="${result.financials().monthlyRent()}" 
                           step="50">
                    <div class="control-info">Slide to see impact of different rent levels</div>
                </div>
                <div class="control-group">
                    <label>Additional Cash: $<span id="cash-val">0</span></label>
                    <input type="range" id="cash-slider" 
                           min="0" max="20000" value="0" step="500">
                    <div class="control-info">Simulate extra savings or external support</div>
                </div>
            </div>
            <div id="sim-status" style="display:none; text-align: center; margin-top: 1rem; font-size: 0.8rem; color: var(--text-tertiary);">
                Recalculating verdict...
            </div>
        </div>

        <div style="margin-bottom: 4rem;">
            <h3 class="contributing-title" style="margin-bottom: 1.5rem; font-weight: 700; letter-spacing: 0.02em;">FINANCIAL DATA POINTS</h3>
            
            <div class="financial-grid">
                <div>
                    <div class="financial-label-sm">Total Upfront Cost</div>
                    <div id="total-upfront-cost" class="financial-value-lg">$${String.format("%,d", result.financials().totalUpfrontCost())}</div>
                    
                    <!-- Legend for Annotations -->
                    <div style="font-size: 0.7rem; color: var(--text-tertiary); margin-top: 1rem; font-style: italic;">
                        * "Applied Baseline/Standard": Used when specific rules are absent.
                    </div>

                    <!-- Smart Receipt: Detailed Breakdown -->
                    <div style="margin-top: 0.5rem; border-top: 1px dashed var(--border-subtle); padding-top: 1rem;">
                        <ul style="list-style: none; padding: 0; font-size: 0.9rem;">
                             @for(firstrentverdict.model.verdict.FinancialLineItem item : result.financials().costBreakdown())
                                <li style="display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 0.75rem;">
                                    <div>
                                        <div style="font-weight: 500; color: var(--text-secondary);">${item.label()}</div>
                                        <div class="receipt-annotation-locked" style="font-size: 0.75rem; color: var(--text-tertiary); margin-top: 0.1rem;">${item.annotation()}</div>
                                    </div>
                                    <div style="font-weight: 600; color: var(--text-primary);">$${String.format("%,d", item.amount())}</div>
                                </li>
                             @endfor
                        </ul>
                        <div class="receipt-guardrail">
                            Locked items represent requirements under the selected scenario.
                        </div>
                    </div>
                </div>
                <div>
                     <!-- Market Radar (New Section before Buffer) -->
                     <div class="market-radar-container">
                        <div class="radar-title">Market Radar: <span id="radar-title-text">${result.marketPosition().marketZone()}</span></div>
                        <div class="radar-track">
                             <div class="radar-range"></div>
                             <div id="radar-marker" class="radar-marker" style="left: ${Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 2.0) * 100.0)) + "%"};"></div>
                             <div id="radar-label" class="radar-label" style="left: ${Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 2.0) * 100.0)) + "%"}; white-space: nowrap;">
                                YOU ($${String.format("%,d", result.financials().monthlyRent())})
                             </div>
                        </div>
                        <div class="radar-legend" style="display: grid; grid-template-columns: 1fr 1fr 1fr; position: relative;">
                            <span style="text-align: left;">Lower ($${result.marketPosition().p25Rent()})</span>
                            <span style="text-align: center;">Median ($${result.marketPosition().medianRent()})</span>
                            <span style="text-align: right;">Higher ($${result.marketPosition().p75Rent()})</span>
                        </div>
                     </div>
                     
                    <!-- Market Correction Simulation Action -->
                     <div id="market-correction-container" style="margin-bottom: 2rem; text-align: center;">
                          <button onclick="simulateMarketCorrection(${result.marketPosition().p25Rent()})" class="market-correction-btn">
                              Simulate Market Correction (Rent $${result.marketPosition().p25Rent()})
                          </button>
                     </div>

                    <div class="financial-label-sm">Remaining Buffer</div>
                    <div id="remaining-buffer" class="financial-value-lg" style="color: ${result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--signal-error)" : "var(--text-primary)"};">
                        $${String.format("%,d", result.financials().remainingBuffer())}
                    </div>
                </div>
            </div>
        </div>

        <!-- Next Steps / Action Plan (Decision Support Enhancement) -->
        <div style="margin-bottom: 3rem; padding: 2rem; background: linear-gradient(135deg, rgba(59, 130, 246, 0.03) 0%, rgba(16, 185, 129, 0.03) 100%); border-radius: 12px; border: 1px solid var(--border-subtle);">
            <h3 style="font-size: 1.1rem; font-weight: 700; margin-bottom: 1.5rem; color: var(--text-primary);">
                📋 Your Next Steps
            </h3>
            
            @if(result.verdict() == Verdict.APPROVED)
                <div style="display: grid; gap: 1rem;">
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(22, 163, 74, 0.1); color: #16a34a; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">1</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Prepare Documentation</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">Gather pay stubs (2-3 months), bank statements, and photo ID before applying.</div>
                        </div>
                    </div>
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(22, 163, 74, 0.1); color: #16a34a; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">2</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Check Credit Report</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">Review your credit score and dispute any errors before landlords pull it.</div>
                        </div>
                    </div>
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(22, 163, 74, 0.1); color: #16a34a; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">3</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Keep Buffer Intact</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">Your $${String.format("%,d", result.safetyGap().gapAmount())} buffer is your safety net—don't spend it on move-in extras.</div>
                        </div>
                    </div>
                </div>
            @elseif(result.verdict() == Verdict.BORDERLINE)
                <div style="display: grid; gap: 1rem;">
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(202, 138, 4, 0.1); color: #ca8a04; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">1</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Negotiate Lower Rent</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">Ask for a reduced rate, especially if you can show income stability or sign a longer lease.</div>
                        </div>
                    </div>
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(202, 138, 4, 0.1); color: #ca8a04; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">2</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Delay Move by 30-60 Days</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">An extra month of savings could move you from BORDERLINE to APPROVED.</div>
                        </div>
                    </div>
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(202, 138, 4, 0.1); color: #ca8a04; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">3</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Consider Roommates</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">Splitting rent can free up cash for a safer financial buffer.</div>
                        </div>
                    </div>
                </div>
            @else
                <div style="display: grid; gap: 1rem;">
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(220, 38, 38, 0.1); color: #dc2626; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">1</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Build Your Cash Reserve</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">You need $${String.format("%,d", Math.abs(result.safetyGap().gapAmount()))} more before this move is financially safe.</div>
                        </div>
                    </div>
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(220, 38, 38, 0.1); color: #dc2626; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">2</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Look for Lower-Cost Areas</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">Use the slider above to see what rent level would make this move safe for you.</div>
                        </div>
                    </div>
                    <div class="next-step-item" style="display: flex; gap: 0.75rem; align-items: flex-start;">
                        <span style="background: rgba(220, 38, 38, 0.1); color: #dc2626; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">3</span>
                        <div>
                            <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 0.25rem;">Cut Moving Costs</div>
                            <div style="font-size: 0.9rem; color: var(--text-secondary);">DIY moving, asking friends for help, or selling furniture can save $300-800+.</div>
                        </div>
                    </div>
                </div>
            @endif
        </div>

        <!-- Data Footnote (Stage 3: Data Authority) -->
        <details class="methodology-footnote">
             <summary>Methodology & Data Sources</summary>
             <ul class="methodology-list">
                 <li><strong>Data Source:</strong> 2026 Q1 Regional Market Index (${result.regionalContext().cityName()})</li>
                 <li><strong>Compliance:</strong> State Security Deposit Regulations (Local Compliance)</li>
                 <li><strong>Benchmarks:</strong> Standardized Moving Cost Baselines</li>
             </ul>
        </details>

        <div class="new-assessment-section">
           <p style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 2rem;">
               This scenario fails due to cash constraints, not rent level.
           </p>
           <a href="/RentVerdict/" class="btn-primary" style="display: inline-block; text-decoration: none; width: auto; min-width: 200px;">Run a New Scenario</a>
        </div>

    </div>

    <script>
        const rentSlider = document.getElementById('rent-slider');
        const cashSlider = document.getElementById('cash-slider');
        const rentVal = document.getElementById('rent-val');
        const cashVal = document.getElementById('cash-val');
        const simStatus = document.getElementById('sim-status');
        const recoveryHint = document.getElementById('recovery-hint');
        const targetRentSpan = document.getElementById('target-rent');
        const targetCashSpan = document.getElementById('target-cash');
        const targetRentError = document.getElementById('target-rent-error');

        let debounceTimer;

        function formatCurrency(amount) {
            return amount.toLocaleString();
        }

        function updateUI(data) {
            const result = data.result;
            const financials = result.financials;
            
            // 1. Verdict Badge
            const badge = document.getElementById('verdict-badge');
            let badgeHtml = '';
            // FALLBACK if verdict is somehow missing
            const verdict = result.verdict || 'DENIED'; 
            
            if (verdict === 'APPROVED') {
                badgeHtml = '<div style="font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;">APPROVED</div>';
            } else if (verdict === 'BORDERLINE') {
                badgeHtml = '<div style="font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;">BORDERLINE</div>';
            } else {
                badgeHtml = '<div style="font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;">DENIED</div>';
            }
            badge.innerHTML = badgeHtml;

            // 2. Safety Gap (Uses displayText field)
            const sgContainer = document.getElementById('safety-gap-container');
            if (result.safetyGap) {
                const isPositive = result.safetyGap.isApproved;
                const color = isPositive ? "var(--signal-success)" : "var(--signal-error)";
                const bgColor = isPositive ? "var(--bg-body)" : "rgba(205, 44, 37, 0.03)";
                
                // Fallback for missing displayText
                const displayTxt = result.safetyGap.displayText || "Risk Detected";
                const actionPrmpt = result.safetyGap.actionPrompt || "Action Required";

                sgContainer.innerHTML = 
                    '<div style="position: sticky; top: 60px; background: var(--bg-body); border: 2px solid ' + color + '; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: ' + bgColor + ';">' +
                        '<div id="safety-gap-amount" style="font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;">' + displayTxt + '</div>' + 
                        '<div id="safety-gap-action" style="font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">' + actionPrmpt + '</div>' +
                    '</div>';

            }

            // 3. Why & Bottleneck
            // 3. Why & Bottleneck
            // Fallback for missing Why text
            const whyText = result.whyThisVerdict || "Financial assessment indicates unacceptable risk level for this move.";
            const whyTextEl = document.getElementById('why-text');
            if (whyTextEl) whyTextEl.innerText = whyText;
            
            const bottleneckContainer = document.getElementById('bottleneck-container');
            const bottleneckTextEl = document.getElementById('bottleneck-text');

            if (result.primaryBottleneck) {
                 if (bottleneckTextEl) bottleneckTextEl.innerText = result.primaryBottleneck;
                 if (bottleneckContainer) bottleneckContainer.style.display = 'block';
            } else {
                 if (bottleneckContainer) bottleneckContainer.style.display = 'none';
            }

            // 4. Financials
            const totalUpfrontEl = document.getElementById('total-upfront-cost');
            if (totalUpfrontEl) totalUpfrontEl.innerText = '$' + financials.totalUpfrontCost.toLocaleString();

            const remBuffer = document.getElementById('remaining-buffer');
            if (remBuffer) {
                remBuffer.innerText = '$' + financials.remainingBuffer.toLocaleString();
                remBuffer.style.color = financials.remainingBuffer < financials.recommendedBuffer ? 'var(--signal-error)' : 'var(--text-primary)';
            }
            
            // 5. Recovery Hint Logic
            if (verdict !== 'APPROVED') {
                recoveryHint.style.display = 'block';
                
                // Needed Cash = max(0, recommendedBuffer - remainingBuffer)
                const neededCash = Math.max(0, financials.recommendedBuffer - financials.remainingBuffer);
                targetCashSpan.innerText = formatCurrency(neededCash);

                // Target Rent calculation
                const currentAvailable = financials.remainingBuffer + financials.totalUpfrontCost;
                const maxRent = Math.floor((currentAvailable - financials.staticCosts - financials.recommendedBuffer) / financials.upfrontBaseMultiplier);
                
                if (maxRent < 0) {
                     if (targetRentSpan) {
                         targetRentSpan.innerText = "Option Unavailable";
                         targetRentSpan.style.fontSize = "0.9em"; 
                         targetRentSpan.style.color = "var(--text-tertiary)";
                     }
                     if (targetRentError) {
                        targetRentError.innerText = "Current cash level cannot support any rental scenario.";
                        targetRentError.style.display = 'block';
                     }
                } else {
                     if (targetRentSpan) {
                         targetRentSpan.innerText = formatCurrency(maxRent);
                         targetRentSpan.style.fontSize = "inherit";
                         targetRentSpan.style.color = "inherit";
                     }
                     if (targetRentError) targetRentError.style.display = 'none';
                }

            } else {
                recoveryHint.style.display = 'none';
            }

            // 6. Market Radar Dynamic Update
            const marketPos = result.marketPosition;
            if (marketPos && financials) {
                const titleEl = document.getElementById('radar-title-text');
                if (titleEl) titleEl.innerText = marketPos.marketZone;
                
                const userRent = financials.monthlyRent;
                const median = marketPos.medianRent;
                
                // Define scale: 0 to 2x Median
                const maxScale = median * 2.0;
                const percentage = Math.min(100.0, Math.max(0.0, (userRent / maxScale) * 100.0));
                
                const marker = document.getElementById('radar-marker');
                const label = document.getElementById('radar-label');
                
                if (marker) marker.style.left = percentage + '%';
                if (label) {
                    label.style.left = percentage + '%';
                    label.innerHTML = 'YOU ($' + userRent.toLocaleString() + ')';
                }
            }

            simStatus.style.display = 'none';
            document.querySelectorAll('.simulation-lab, .verdict-text, .safety-gap-card, .why-text, .bottleneck-value').forEach(el => el.classList.remove('updating'));
        }

        function simulateMarketCorrection(targetRent) {
            simStatus.style.display = 'block';
            document.querySelectorAll('.verdict-text, .safety-gap-card, .why-text, .bottleneck-value').forEach(el => el.classList.add('updating'));
            
            // Sync slider UI so they stay consistent
            rentSlider.value = targetRent;
            rentVal.innerText = targetRent;

            const payload = {
                adjustedRent: targetRent,
                cashInjection: 0
            };

            fetch('/RentVerdict/what-if', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(response => response.json())
            .then(data => updateUI(data))
            .catch(err => {
                console.error('Market Correction failed:', err);
                simStatus.innerText = 'Error applying market correction.';
            });
        }

        let abortController;

        function runSimulation() {
            if (abortController) {
                abortController.abort();
            }
            abortController = new AbortController();

            simStatus.style.display = 'block';
            document.querySelectorAll('.verdict-text, .safety-gap-card, .why-text, .bottleneck-value').forEach(el => el.classList.add('updating'));
            
            const payload = {
                adjustedRent: parseInt(rentSlider.value),
                cashInjection: parseInt(cashSlider.value)
            };

            fetch('/RentVerdict/what-if', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
                signal: abortController.signal
            })
            .then(async response => {
                if (!response.ok) {
                    const errorData = await response.text();
                    throw new Error('Server responded with ' + response.status + ': ' + errorData);
                }
                return response.json();
            })
            .then(data => updateUI(data))
            .catch(err => {
                if (err.name === 'AbortError') return;
                console.error('Simulation failed:', err);
                simStatus.innerText = 'Error updating simulation: ' + err.message;
            });
        }

        rentSlider.addEventListener('input', (e) => {
            rentVal.innerText = e.target.value;
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(runSimulation, 200);
        });

        cashSlider.addEventListener('input', (e) => {
            cashVal.innerText = e.target.value;
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(runSimulation, 200);
        });
    </script>
`)
