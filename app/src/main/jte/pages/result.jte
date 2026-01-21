@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict

@param VerdictResult result

@template.layout.main(content = @`
    <div class="animate-entry" style="max-width: 720px; margin: 4rem auto; padding: 0 2rem;">
        
        <!-- Navbar / Back Link -->
        <div style="margin-bottom: 4rem;">
            <a href="/RentVerdict/" style="text-decoration: none; color: var(--text-tertiary); font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em;">
                &larr; Return to Engine
            </a>
        </div>

        <!-- Header / Case ID -->
        <div style="border-bottom: 2px solid var(--border-focus); padding-bottom: 2rem; margin-bottom: 3rem; display: flex; justify-content: space-between; align-items: flex-end;">
            <div>
                <h6 style="text-transform: uppercase; letter-spacing: 0.1em; font-size: 0.75rem; color: var(--text-tertiary); margin-bottom: 0.5rem;">Verdict Decree</h6>
                <h1 style="font-size: 2.5rem; margin: 0; letter-spacing: -0.02em;">Financial Viability Assessment</h1>
            </div>
            <div style="text-align: right;">
                <span style="font-family: monospace; color: var(--text-secondary); font-size: 0.9rem;">CASE ID: ${java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase()}</span>
            </div>
        </div>

        <!-- Final Decision (Typography Focus) -->
        <div style="margin-bottom: 4rem; display: flex; gap: 3rem; align-items: flex-start;">
            <div style="flex: 0 0 auto;">
                 <h3 style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 0.5rem;">Decision</h3>
                @if(result.verdict() == Verdict.APPROVED)
                    <div style="font-size: 4.5rem; font-weight: 900; color: var(--text-primary); letter-spacing: -0.05em; line-height: 0.9;">APPROVED</div>
                @elseif(result.verdict() == Verdict.BORDERLINE)
                    <div style="font-size: 4.5rem; font-weight: 900; color: var(--text-primary); letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;">BORDERLINE</div>
                @else
                    <div style="font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;">DENIED</div>
                @endif
            </div>
            
            <div style="flex: 1; padding-top: 1.5rem;">
                <p style="font-size: 1.25rem; line-height: 1.5; color: var(--text-primary);">
                    ${result.summary()}
                </p>
            </div>
        </div>

        <!-- Critical Risk Factor -->
        @if(result.primaryDistressFactor() != null)
            <div style="border: 1px solid var(--signal-error); padding: 2rem; margin-bottom: 4rem; background-color: rgba(205, 44, 37, 0.02);">
                <h4 style="color: var(--signal-error); text-transform: uppercase; font-size: 0.75rem; letter-spacing: 0.05em; margin-bottom: 0.5rem;">Critical Risk Factor</h4>
                <p style="font-size: 1.25rem; font-weight: 600; color: var(--signal-error); margin: 0;">${result.primaryDistressFactor()}</p>
            </div>
        @endif

        <!-- Financial Breakdown (2-Column Grid inside the Doc) -->
        <div style="margin-bottom: 4rem;">
            <h3 style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;">Financial Data Points</h3>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 4rem;">
                <div>
                    <div style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;">Total Upfront Cost</div>
                    <div style="font-size: 2rem; font-weight: 600; letter-spacing: -0.02em;">$${String.format("%,d", result.financials().totalUpfrontCost())}</div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;">Remaining Buffer</div>
                    <div style="font-size: 2rem; font-weight: 600; letter-spacing: -0.02em; color: ${result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--signal-error)" : "var(--text-primary)"};">
                        $${String.format("%,d", result.financials().remainingBuffer())}
                    </div>
                </div>
            </div>
        </div>

        <!-- Detailed Line Items -->
        <div style="margin-bottom: 5rem;">
            <ul style="list-style: none; padding: 0;">
                @for(String line : result.breakdown())
                    <li style="padding: 1rem 0; border-bottom: 1px solid var(--border-subtle); display: flex; justify-content: space-between; font-size: 1rem;">
                        <span style="color: var(--text-secondary);">${line.split(":")[0]}</span>
                        <span style="font-weight: 500;">${line.split(":")[1]}</span>
                    </li>
                @endfor
                <li style="padding: 1rem 0; border-bottom: 1px solid var(--border-subtle); display: flex; justify-content: space-between; font-size: 1rem;">
                   <span style="color: var(--text-secondary);">Recommended Buffer / Compliance</span>
                   <span style="font-weight: 600;">$${String.format("%,d", result.financials().recommendedBuffer())}</span>
                </li>
            </ul>
        </div>

        <!-- Footer / Action -->
        <div style="text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 3rem;">
           <p style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 2rem;">
               This verdict is final based on the provided parameters.<br>
               To appeal or adjust parameters, start a new simulation.
           </p>
           <a href="/RentVerdict/" class="btn-primary" style="display: inline-block; text-decoration: none; width: auto; min-width: 200px;">Initialize New Assessment</a>
        </div>

    </div>
`)
