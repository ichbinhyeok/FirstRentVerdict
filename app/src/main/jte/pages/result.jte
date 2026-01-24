@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict
@import firstrentverdict.model.verdict.MarketPosition

@param VerdictResult result

@template.layout.main(content = @`
    <div style="max-width: 720px; margin: 4rem auto; padding: 0 2rem;">
        
        <div style="margin-bottom: 4rem;">
            <a href="/RentVerdict/" style="text-decoration: none; color: var(--text-tertiary); font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em;">
                &larr; Return to Engine
            </a>
        </div>

        <div style="text-align: center; margin-bottom: 2rem;">
            <h3 style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 0.5rem;">Final Decision</h3>
            <div id="verdict-badge">
                @if(result.verdict() == Verdict.APPROVED)
                    <div style="font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;">APPROVED</div>
                @elseif(result.verdict() == Verdict.BORDERLINE)
                    <div style="font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;">BORDERLINE</div>
                @else
                    <div style="font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;">DENIED</div>
                @endif
            </div>
        </div>

        <div id="safety-gap-container">
            @if(result.safetyGap() != null)
                @if(result.safetyGap().isApproved())
                    <div style="position: sticky; top: 60px; background: var(--bg-body); border: 2px solid #2d5016; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100;">
                        <div id="safety-gap-amount" style="font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;">${result.safetyGap().toDisplayText()}</div>
                        <div id="safety-gap-action" style="font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">${result.safetyGap().actionPrompt()}</div>
                    </div>
                @else
                    <div style="position: sticky; top: 60px; background: var(--bg-body); border: 2px solid var(--signal-error); padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: rgba(205, 44, 37, 0.03);">
                        <div id="safety-gap-amount" style="font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;">${result.safetyGap().toDisplayText()}</div>
                        <div id="safety-gap-action" style="font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">${result.safetyGap().actionPrompt()}</div>
                    </div>
                @endif
            @endif
        </div>

        @if(result.whyThisVerdict() != null)
            <div style="margin-bottom: 4rem; padding: 2rem; background: var(--bg-panel); border-left: 4px solid var(--text-primary);">
                <h3 style="font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1rem;">Why This Verdict</h3>
                <p id="why-text" style="font-size: 1.25rem; line-height: 1.6; max-width: 65ch; color: var(--text-primary);">
                    ${result.whyThisVerdict()}
                </p>
                @if(result.primaryBottleneck() != null)
                    <div style="margin-top: 1rem; padding-top: 1rem; border-top: 1px solid var(--border-subtle);">
                        <span style="font-size: 0.85rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em;">Primary Bottleneck:</span>
                        <span id="bottleneck-text" style="font-weight: 600; margin-left: 0.5rem;">${result.primaryBottleneck()}</span>
                    </div>
                @endif
            </div>
        @endif

        @if(result.contributingFactors() != null && !result.contributingFactors().isEmpty())
            <div class="verdict-contributing" style="margin-bottom: 3rem;">
                <h4 style="font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;">Contributing Factors</h4>
                <ul style="list-style: none; padding: 0;">
                    @for(String factor : result.contributingFactors())
                        <li style="margin-bottom: 1rem; padding-left: 1.5rem; position: relative; color: var(--text-secondary);">
                            <span style="position: absolute; left: 0; color: var(--text-tertiary);">→</span>
                            ${factor}
                        </li>
                    @endfor
                </ul>
            </div>
        @endif

        @if(result.regionalContext() != null)
            <details class="verdict-regional" style="margin-top: 3rem; margin-bottom: 3rem; padding: 1.5rem; background: var(--bg-body); border: 1px solid var(--border-subtle); border-radius: 4px;">
                <summary style="cursor: pointer; font-weight: 600; font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-tertiary); user-select: none;">Regional Context: ${result.regionalContext().cityName()}</summary>
                <ul style="list-style: none; padding: 0; margin-top: 1rem;">
                    @for(String context : result.regionalContext().contextFactors())
                        <li style="margin-bottom: 0.75rem; padding-left: 1.5rem; position: relative; font-size: 0.95rem; color: var(--text-secondary);">
                            <span style="position: absolute; left: 0; color: var(--text-tertiary);">•</span>
                            ${context}
                        </li>
                    @endfor
                </ul>
            </details>
        @endif


        <!-- Recovery Hint Box (Deterministic) -->
        <div id="recovery-hint" style="margin-bottom: 2rem; padding: 1.5rem; background: var(--bg-body); border: 2px solid var(--text-tertiary); border-radius: 4px; display: ${result.verdict() != Verdict.APPROVED ? "block" : "none"}; text-align: left;">
            <h4 style="margin: 0 0 1rem 0; font-size: 0.9rem; text-transform: uppercase; color: var(--text-secondary); letter-spacing: 0.05em;">To Reach Approved Status:</h4>
            <div style="font-size: 1rem; color: var(--text-primary); line-height: 1.5;">
                You must meet <strong>ONE</strong> of the following conditions:
                <ul style="margin: 0.5rem 0 0 0; padding-left: 1.5rem;">
                    <li style="margin-bottom: 0.5rem;">
                         Reduce rent to <strong>$<span id="target-rent">
                         ${String.format("%,d", (int) Math.floor((result.financials().remainingBuffer() + result.financials().totalUpfrontCost() - result.financials().staticCosts() - result.financials().recommendedBuffer()) / result.financials().upfrontBaseMultiplier()))}
                         </span></strong> or lower
                    </li>
                    <li>
                        Add <strong>$<span id="target-cash">
                        ${String.format("%,d", Math.max(0, result.financials().recommendedBuffer() - result.financials().remainingBuffer()))}
                        </span></strong> in available cash
                    </li>
                </ul>
            </div>
             <div id="target-rent-error" style="display:none; color: var(--signal-error); font-size: 0.85rem; margin-top: 0.5rem;">
                No viable rent at current cash level.
            </div>
        </div>

        <div class="simulation-lab">
            <h4>Simulation Lab: Change the Variables</h4>
            <div class="sim-controls">
                <div class="control-group">
                    <label>Monthly Rent: $<span id="rent-val">${result.financials().monthlyRent()}</span></label>
                    <input type="range" id="rent-slider" 
                           min="${Math.max(500, result.financials().monthlyRent() - 1000)}" 
                           max="${result.financials().monthlyRent() + 1000}" 
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
            <h3 style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;">Financial Data Points</h3>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 4rem;">
                <div>
                    <div style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;">Total Upfront Cost</div>
                    <div id="total-upfront-cost" style="font-size: 2rem; font-weight: 600; letter-spacing: -0.02em;">$${String.format("%,d", result.financials().totalUpfrontCost())}</div>
                    
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
                                        <div style="font-size: 0.75rem; color: var(--text-tertiary); margin-top: 0.1rem;">${item.annotation()}</div>
                                    </div>
                                    <div style="font-weight: 600; color: var(--text-primary);">$${String.format("%,d", item.amount())}</div>
                                </li>
                             @endfor
                        </ul>
                    </div>
                </div>
                <div>
                     <!-- Market Radar (New Section before Buffer) -->
                     <div style="margin-bottom: 2rem; padding: 1rem; background: var(--bg-body); border: 1px solid var(--border-subtle); border-radius: 4px;">
                        <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-secondary); margin-bottom: 0.5rem; letter-spacing: 0.05em;">Market Radar: ${result.marketPosition().marketZone()}</div>
                        <!-- Simple Bar Chart -->
                        <div style="position: relative; height: 8px; background: #e0e0e0; border-radius: 4px; margin-top: 1.5rem; margin-bottom: 1.5rem;">
                            <!-- Range P25 to P75 -->
                             <div style="position: absolute; left: 0%; width: 100%; height: 100%; background: linear-gradient(90deg, #e0e0e0 0%, #d1e7dd 25%, #d1e7dd 75%, #e0e0e0 100%); border-radius: 4px;"></div>
                             
                             <!-- User Dot -->
                             <%-- Calculate left position relative to median * 1.5 --%>
                             <%-- P25 is approx 25%, P75 is 75% on a relative scale --%>
                             <div style="position: absolute; left: ${Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 1.5) * 100.0)) + "%"}; top: -6px; width: 4px; height: 20px; background: var(--text-primary); border: 1px solid #fff;"></div>
                             <div style="position: absolute; left: ${Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 1.5) * 100.0)) + "%"}; top: -20px; transform: translateX(-50%); font-size: 0.7rem; font-weight: 700;">YOU</div>
                        </div>
                        <div style="display: flex; justify-content: space-between; font-size: 0.7rem; color: var(--text-tertiary);">
                            <span>Lower ($${result.marketPosition().p25Rent()})</span>
                            <span>Median</span>
                            <span>Higher</span>
                        </div>
                     </div>
                     
                    <!-- Market Correction Simulation Action -->
                     @if(result.marketPosition().userRent() > result.marketPosition().p25Rent())
                        <div style="margin-bottom: 2rem; text-align: center;">
                             <button onclick="simulateMarketCorrection(${result.marketPosition().p25Rent()})" style="background-color: transparent; border: 1px solid var(--text-primary); border-radius: 4px; padding: 0.5rem 1rem; font-size: 0.8rem; cursor: pointer; color: var(--text-primary);">
                                 Simulate Market Correction (Rent $${result.marketPosition().p25Rent()})
                             </button>
                        </div>
                     @endif

                    <div style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;">Remaining Buffer</div>
                    <div id="remaining-buffer" style="font-size: 2rem; font-weight: 600; letter-spacing: -0.02em; color: ${result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--signal-error)" : "var(--text-primary)"};">
                        $${String.format("%,d", result.financials().remainingBuffer())}
                    </div>
                </div>
            </div>
        </div>

        <div style="text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 3rem;">
           <p style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 2rem;">
               This verdict is an automated assessment based on provided parameters.<br>
               Adjust variables in the Simulation Lab to see alternative outcomes.
           </p>
           <a href="/RentVerdict/" class="btn-primary" style="display: inline-block; text-decoration: none; width: auto; min-width: 200px;">Initialize New Assessment</a>
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
            if (result.verdict === 'APPROVED') {
                badgeHtml = '<div style="font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;">APPROVED</div>';
            } else if (result.verdict === 'BORDERLINE') {
                badgeHtml = '<div style="font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;">BORDERLINE</div>';
            } else {
                badgeHtml = '<div style="font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;">DENIED</div>';
            }
            badge.innerHTML = badgeHtml;

            // 2. Safety Gap (Canonical Logic in Backend, UI just renders)
            const sgContainer = document.getElementById('safety-gap-container');
            if (result.safetyGap) {
                const isPositive = result.safetyGap.isApproved;
                const color = isPositive ? "var(--signal-success)" : "var(--signal-error)";
                const bgColor = isPositive ? "var(--bg-body)" : "rgba(205, 44, 37, 0.03)";
                
                sgContainer.innerHTML = 
                    '<div style="position: sticky; top: 60px; background: var(--bg-body); border: 2px solid ' + color + '; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: ' + bgColor + ';">' +
                        '<div id="safety-gap-amount" style="font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;">' + result.safetyGap.toDisplayText + '</div>' + 
                        '<div id="safety-gap-action" style="font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">' + result.safetyGap.actionPrompt + '</div>' +
                    '</div>';

            }

            // 3. Why & Bottleneck
            document.getElementById('why-text').innerText = result.whyThisVerdict;
            document.getElementById('bottleneck-text').innerText = result.primaryBottleneck;

            // 4. Financials
            document.getElementById('total-upfront-cost').innerText = '$' + financials.totalUpfrontCost.toLocaleString();
            const remBuffer = document.getElementById('remaining-buffer');
            remBuffer.innerText = '$' + financials.remainingBuffer.toLocaleString();
            remBuffer.style.color = financials.remainingBuffer < financials.recommendedBuffer ? 'var(--signal-error)' : 'var(--text-primary)';
            
            // 5. Recovery Hint Logic
            if (result.verdict !== 'APPROVED') {
                recoveryHint.style.display = 'block';
                
                // Needed Cash = max(0, recommendedBuffer - remainingBuffer)
                const neededCash = Math.max(0, financials.recommendedBuffer - financials.remainingBuffer);
                targetCashSpan.innerText = formatCurrency(neededCash);

                // Target Rent calculation
                const currentAvailable = financials.remainingBuffer + financials.totalUpfrontCost;
                const maxRent = Math.floor((currentAvailable - financials.staticCosts - financials.recommendedBuffer) / financials.upfrontBaseMultiplier);
                
                if (maxRent < 0) {
                     targetRentSpan.innerText = "N/A";
                     targetRentError.style.display = 'block';
                } else {
                     targetRentSpan.innerText = formatCurrency(maxRent);
                     targetRentError.style.display = 'none';
                }

            } else {
                recoveryHint.style.display = 'none';
            }

            simStatus.style.display = 'none';
            document.querySelectorAll('.simulation-lab, #verdict-badge, #safety-gap-container, #why-text, #bottleneck-text').forEach(el => el.classList.remove('updating'));
        }

        function simulateMarketCorrection(targetRent) {
            simStatus.style.display = 'block';
            document.querySelectorAll('#verdict-badge, #safety-gap-container, #why-text, #bottleneck-text').forEach(el => el.classList.add('updating'));
            
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

        function runSimulation() {
            simStatus.style.display = 'block';
            document.querySelectorAll('#verdict-badge, #safety-gap-container, #why-text, #bottleneck-text').forEach(el => el.classList.add('updating'));
            
            const payload = {
                adjustedRent: parseInt(rentSlider.value),
                cashInjection: parseInt(cashSlider.value)
            };

            fetch('/RentVerdict/what-if', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(response => response.json())
            .then(data => updateUI(data))
            .catch(err => {
                console.error('Simulation failed:', err);
                simStatus.innerText = 'Error updating simulation.';
            });
        }

        rentSlider.addEventListener('input', () => {
            rentVal.innerText = rentSlider.value;
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(runSimulation, 300);
        });

        cashSlider.addEventListener('input', () => {
            cashVal.innerText = cashSlider.value;
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(runSimulation, 300);
        });
    </script>
`)
