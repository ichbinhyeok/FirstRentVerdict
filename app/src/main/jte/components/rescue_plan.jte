@import firstrentverdict.model.verdict.VerdictResult
@import firstrentverdict.model.verdict.Verdict
@import firstrentverdict.model.verdict.VerdictInput
@param VerdictResult result
@param VerdictInput input

<div id="rescue-plan-card" style="display: ${result.verdict() == Verdict.DENIED || result.verdict() == Verdict.BORDERLINE ? "block" : "none"}; background: var(--bg-surface); border: 2px solid var(--danger); border-radius: var(--radius-lg); padding: 2rem; margin-bottom: 3rem; text-align: left;">
        <h3 style="color: var(--danger); font-size: 1.5rem; font-weight: 800; margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>[!]</span> Approval Rescue Plan
        </h3>
        <p style="color: var(--text-secondary); font-size: 1.1rem; line-height: 1.6; margin-bottom: 1.5rem;">
            Your current profile is getting blocked by screening pressure, move-in cash pressure, or both. Use these routes before paying more application fees in ${input.city()}.
        </p>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1rem;">
            <div style="background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1.5rem;">
                <div style="color: var(--accent-primary); font-weight: 700; margin-bottom: 0.5rem;">STRATEGY 1</div>
                <h4 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Pre-Clear an Approval Backstop</h4>
                <p style="font-size: 0.9rem; color: var(--text-tertiary); margin-bottom: 1rem;">Ask whether the property accepts a guarantor, larger deposit, or prepaid rent before you submit another application.</p>
                <div style="display: flex; flex-direction: column; gap: 0.4rem;">
                    <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" style="font-size: 0.85rem; font-weight: 700; color: var(--accent-primary); text-decoration: none;">Compare Backstops &rarr;</a>
                    <a href="https://www.theguarantors.com/" target="_blank" rel="noopener nofollow"
                       data-track-event="lead_submit"
                       data-track-category="monetization"
                       data-track-label="rescue_partner_theguarantors_click"
                       style="font-size: 0.8rem; font-weight: 600; color: var(--text-secondary); text-decoration: underline;">
                        Open Demo Provider
                    </a>
                </div>
            </div>
            
            <div style="background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1.5rem;">
                <div style="color: #f59e0b; font-weight: 700; margin-bottom: 0.5rem;">STRATEGY 2</div>
                <h4 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Switch to Flexible Screening Inventory</h4>
                <p style="font-size: 0.9rem; color: var(--text-tertiary); margin-bottom: 1rem;">Corporate screening stacks rejections. Independent owners and flexible operators in ${input.state()} are more likely to review context and documents.</p>
                <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" style="font-size: 0.85rem; font-weight: 700; color: #d97706; text-decoration: none;">Open Approval Playbook &rarr;</a>
            </div>
            
            <div style="background: var(--bg-panel); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1.5rem;">
                <div style="color: #10b981; font-weight: 700; margin-bottom: 0.5rem;">STRATEGY 3</div>
                <h4 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Raise Cash, Then Re-Apply</h4>
                <p style="font-size: 0.9rem; color: var(--text-tertiary); margin-bottom: 1rem;">If move-in wipes out your buffer, fix liquidity first. A smaller target rent beats repeated denials and fee leakage.</p>
                <a href="/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment" style="font-size: 0.85rem; font-weight: 700; color: #10b981; text-decoration: none;">Estimate the Real Cash Threshold &rarr;</a>
            </div>
        </div>
    </div>
