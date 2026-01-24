@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict

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
                </div>
                <div>
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

        let debounceTimer;

        function updateUI(data) {
            const result = data.result;
            
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

            // 2. Safety Gap
            const sgContainer = document.getElementById('safety-gap-container');
            if (result.safetyGap) {
                const isPositive = result.safetyGap.isApproved;
                const color = isPositive ? "var(--signal-success)" : "var(--signal-error)";
                const bgColor = isPositive ? "var(--bg-body)" : "rgba(205, 44, 37, 0.03)";
                
                sgContainer.innerHTML = 
                    '<div style="position: sticky; top: 60px; background: var(--bg-body); border: 2px solid ' + color + '; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: ' + bgColor + ';">' +
                        '<div id="safety-gap-amount" style="font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;">' + result.safetyGap.gapAmount + '</div>' +
                        '<div id="safety-gap-action" style="font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">' + result.safetyGap.actionPrompt + '</div>' +
                    '</div>';

            }

            // 3. Why & Bottleneck
            document.getElementById('why-text').innerText = result.whyThisVerdict;
            document.getElementById('bottleneck-text').innerText = result.primaryBottleneck;

            // 4. Financials
            document.getElementById('total-upfront-cost').innerText = '$' + result.financials.totalUpfrontCost.toLocaleString();
            const remBuffer = document.getElementById('remaining-buffer');
            remBuffer.innerText = '$' + result.financials.remainingBuffer.toLocaleString();
            remBuffer.style.color = result.financials.remainingBuffer < result.financials.recommendedBuffer ? 'var(--signal-error)' : 'var(--text-primary)';
            
            simStatus.style.display = 'none';
            document.querySelectorAll('.simulation-lab, #verdict-badge, #safety-gap-container, #why-text, #bottleneck-text').forEach(el => el.classList.remove('updating'));
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
