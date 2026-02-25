@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict
@import firstrentverdict.model.verdict.VerdictInput
@param VerdictResult result
@param VerdictInput input

@if(result.verdict() == Verdict.DENIED)
    <div style="background: var(--bg-surface); border: 2px solid var(--danger); border-radius: var(--radius-lg); padding: 2rem; margin-bottom: 3rem; text-align: left;">
        <h3 style="color: var(--danger); font-size: 1.5rem; font-weight: 800; margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>🚨</span> Verdict Rescue Plan
        </h3>
        <p style="color: var(--text-secondary); font-size: 1.1rem; line-height: 1.6; margin-bottom: 1.5rem;">
            Your profile currently triggers an algorithmic denial in ${input.city()}. However, you can override this by implementing our vetted rescue strategies:
        </p>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1rem;">
            <div style="background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1.5rem;">
                <div style="color: var(--accent-primary); font-weight: 700; margin-bottom: 0.5rem;">STRATEGY 1</div>
                <h4 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Guarantor Services</h4>
                <p style="font-size: 0.9rem; color: var(--text-tertiary); margin-bottom: 1rem;">Companies like The Guarantors or Rhino can co-sign for a small percentage of rent.</p>
                <a href="/RentVerdict/guides/no-credit-check-apartments" style="font-size: 0.85rem; font-weight: 700; color: var(--accent-primary); text-decoration: none;">Learn How &rarr;</a>
            </div>
            
            <div style="background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1.5rem;">
                <div style="color: #f59e0b; font-weight: 700; margin-bottom: 0.5rem;">STRATEGY 2</div>
                <h4 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Target Private Landlords</h4>
                <p style="font-size: 0.9rem; color: var(--text-tertiary); margin-bottom: 1rem;">Corporate properties auto-reject. Independent owners in ${input.state()} often accept explanation letters.</p>
                <a href="/RentVerdict/guides/how-to-rent-with-eviction" style="font-size: 0.85rem; font-weight: 700; color: #d97706; text-decoration: none;">Read the Guide &rarr;</a>
            </div>
            
            <div style="background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1.5rem;">
                <div style="color: #10b981; font-weight: 700; margin-bottom: 0.5rem;">STRATEGY 3</div>
                <h4 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Liquidity Proof (Bank Statements)</h4>
                <p style="font-size: 0.9rem; color: var(--text-tertiary); margin-bottom: 1rem;">Increase your cash buffer to at least 4x monthly rent and show 6 months of steady balance.</p>
            </div>
        </div>
    </div>
@endif
