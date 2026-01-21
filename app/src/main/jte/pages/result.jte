@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict

@param VerdictResult result

@template.layout.main(content = @`
    <div class="glass-card">
        
        <!-- Animated Verdict Badge -->
        @if(result.verdict() == Verdict.APPROVED)
            <div class="verdict-badge verdict-approved">APPROVED</div>
        @elseif(result.verdict() == Verdict.BORDERLINE)
            <div class="verdict-badge verdict-borderline">BORDERLINE</div>
        @else
            <div class="verdict-badge verdict-denied">DENIED</div>
        @endif

        <!-- Impact Summary -->
        <div style="text-align: center; margin-bottom: 2rem;">
            <p style="font-size: 1.1rem; line-height: 1.6;">${result.summary()}</p>
        </div>

        <!-- Primary Distress Factor (If Any) -->
        @if(result.primaryDistressFactor() != null)
            <div style="background: rgba(239, 68, 68, 0.1); border: 1px solid var(--signal-danger); padding: 1rem; border-radius: 8px; margin-bottom: 2rem; color: #ffcccc;">
                <strong>🚨 Critical Risk Factor:</strong> ${result.primaryDistressFactor()}
            </div>
        @endif

        <!-- Legal Note (If Any) -->
        @if(result.legalProtectionNote() != null)
            <div style="background: rgba(16, 185, 129, 0.1); border: 1px solid var(--signal-safe); padding: 0.8rem; border-radius: 8px; margin-bottom: 2rem; font-size: 0.9rem;">
                <strong>⚖️ Legal Protection:</strong> ${result.legalProtectionNote()}
            </div>
        @endif

        <!-- Financial Breakdown -->
        <h3 style="border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 0.5rem; margin-bottom: 1rem;">The Cold Hard Numbers</h3>
        
        <div class="financial-grid">
            <div class="stat-card">
                <div class="stat-label">Total Upfront Cost</div>
                <div class="stat-value" style="color: var(--signal-danger);">$${String.format("%,d", result.financials().totalUpfrontCost())}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Remaining Safe Buffer</div>
                <div class="stat-value" style="color: ${result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--signal-warning)" : "var(--signal-safe)"};">
                    $${String.format("%,d", result.financials().remainingBuffer())}
                </div>
            </div>
        </div>

        <ul class="breakdown-list">
            @for(String line : result.breakdown())
                <li>${line}</li>
            @endfor
            <li><strong>Recommended Minimum Buffer:</strong> $${String.format("%,d", result.financials().recommendedBuffer())}</li>
        </ul>

        <!-- Action Items (Conversion Slots) -->
        <div style="margin-top: 2rem; display: flex; gap: 1rem; flex-wrap: wrap;">
            <a href="/RentVerdict/" class="btn-primary" style="text-align: center; text-decoration: none; background: var(--bg-surface); border: 1px solid var(--text-secondary); color: var(--text-primary);">
                Check Another City
            </a>
            <!-- Placeholder for Affiliate Links -->
        </div>

    </div>
`)
