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
            @if(result.verdict() == Verdict.APPROVED)
                <div style="font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;">APPROVED</div>
            @elseif(result.verdict() == Verdict.BORDERLINE)
                <div style="font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;">BORDERLINE</div>
            @else
                <div style="font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;">DENIED</div>
            @endif
        </div>

        @if(result.safetyGap() != null)
            @if(result.safetyGap().isApproved())
                <div style="position: sticky; top: 60px; background: var(--bg-body); border: 2px solid #2d5016; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100;">
                    <div style="font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;">${result.safetyGap().toDisplayText()}</div>
                    <div style="font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">${result.safetyGap().actionPrompt()}</div>
                </div>
            @else
                <div style="position: sticky; top: 60px; background: var(--bg-body); border: 2px solid var(--signal-error); padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: rgba(205, 44, 37, 0.03);">
                    <div style="font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;">${result.safetyGap().toDisplayText()}</div>
                    <div style="font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;">${result.safetyGap().actionPrompt()}</div>
                </div>
            @endif
        @endif

        @if(result.whyThisVerdict() != null)
            <div style="margin-bottom: 4rem; padding: 2rem; background: var(--bg-panel); border-left: 4px solid var(--text-primary);">
                <h3 style="font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1rem;">Why This Verdict</h3>
                <p style="font-size: 1.25rem; line-height: 1.6; max-width: 65ch; color: var(--text-primary);">
                    ${result.whyThisVerdict()}
                </p>
                @if(result.primaryBottleneck() != null)
                    <div style="margin-top: 1rem; padding-top: 1rem; border-top: 1px solid var(--border-subtle);">
                        <span style="font-size: 0.85rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em;">Primary Bottleneck:</span>
                        <span style="font-weight: 600; margin-left: 0.5rem;">${result.primaryBottleneck()}</span>
                    </div>
                @endif
            </div>
        @endif

        <div style="margin-bottom: 3rem;">
            <p style="font-size: 1.25rem; line-height: 1.5; color: var(--text-primary);">
                ${result.summary()}
            </p>
        </div>

        @if(result.primaryDistressFactor() != null)
            <div style="border: 1px solid var(--signal-error); padding: 2rem; margin-bottom: 4rem; background-color: rgba(205, 44, 37, 0.02);">
                <h4 style="color: var(--signal-error); text-transform: uppercase; font-size: 0.75rem; letter-spacing: 0.05em; margin-bottom: 0.5rem;">Critical Risk Factor</h4>
                <p style="font-size: 1.25rem; font-weight: 600; color: var(--signal-error); margin: 0;">${result.primaryDistressFactor()}</p>
            </div>
        @endif

        <div style="margin-bottom: 4rem;">
            <h3 style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;">Financial Data Points</h3>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 4rem;">
                <div>
                    <div style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;">Total Upfront Cost</div>
                    <div style="font-size: 2rem; font-weight: 600; letter-spacing: -0.02em;">$${String.format("%,d", result.financials().totalUpfrontCost())}</div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;">Remaining Buffer</div>
                    @if(result.financials().remainingBuffer() < result.financials().recommendedBuffer())
                        <div style="font-size: 2rem; font-weight: 600; letter-spacing: -0.02em; color: var(--signal-error);">
                            $${String.format("%,d", result.financials().remainingBuffer())}
                        </div>
                    @else
                        <div style="font-size: 2rem; font-weight: 600; letter-spacing: -0.02em; color: var(--text-primary);">
                            $${String.format("%,d", result.financials().remainingBuffer())}
                        </div>
                    @endif
                </div>
            </div>
        </div>

        <div style="text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 3rem;">
           <p style="font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 2rem;">
               This verdict is final based on the provided parameters.<br>
               To appeal or adjust parameters, start a new simulation.
           </p>
           <a href="/RentVerdict/" class="btn-primary" style="display: inline-block; text-decoration: none; width: auto; min-width: 200px;">Initialize New Assessment</a>
        </div>

    </div>
`)
