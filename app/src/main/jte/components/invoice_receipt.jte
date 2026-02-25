@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@param CityPageContent pageData

<div style="background: var(--bg-secondary); border-radius: var(--radius-lg); border: 1px solid var(--border-default); overflow: hidden;">
    <div style="padding: 1.5rem; border-bottom: 1px solid var(--border-subtle); display: flex; justify-content: space-between; align-items: center; background: var(--bg-tertiary);">
        <span style="font-weight: 700; font-size: 0.9rem; color: var(--text-secondary);">ESTIMATED BREAKDOWN</span>
        <span class="badge badge-subtle">2026 COST INDEX</span>
    </div>
    
    <div style="padding: 0 1.5rem;">
        <div style="display: flex; justify-content: space-between; padding: 1.5rem 0; border-bottom: 1px solid var(--border-subtle);">
            <div>
                <div style="font-weight: 700;">First Month Rent</div>
                <div style="font-size: 0.85rem; color: var(--text-tertiary);">Standard target for ${pageData.city()}</div>
            </div>
            <div style="font-size: 1.25rem; font-weight: 700;">$${String.format("%,d", pageData.medianRent())}</div>
        </div>
        
        <div style="display: flex; justify-content: space-between; padding: 1.5rem 0; border-bottom: 1px solid var(--border-subtle);">
            <div>
                <div style="font-weight: 700;">Security Deposit</div>
                <div style="font-size: 0.85rem; color: var(--text-tertiary);">${pageData.depositNotes()}</div>
            </div>
            <div style="font-size: 1.25rem; font-weight: 700;">$${String.format("%,d", pageData.typicalDeposit())}</div>
        </div>
        
        @if(pageData.isPetIntent())
        <div style="display: flex; justify-content: space-between; padding: 1.5rem 0; border-bottom: 1px solid var(--border-subtle);">
            <div>
                <div style="font-weight: 700;">Pet-Related (Est.)</div>
                <div style="font-size: 0.85rem; color: var(--text-tertiary);">${pageData.petNotes()}</div>
            </div>
            <div style="font-size: 1.25rem; font-weight: 700;">$${String.format("%,d", pageData.petDeposit())}</div>
        </div>
        @endif

        <div style="display: flex; justify-content: space-between; padding: 1.5rem 0;">
            <div>
                <div style="font-weight: 700;">Moving & Logistics</div>
                <div style="font-size: 0.85rem; color: var(--text-tertiary);">${pageData.movingAssumptions()}</div>
            </div>
            <div style="font-size: 1.25rem; font-weight: 700;">$${String.format("%,d", pageData.typicalMoving())}</div>
        </div>
    </div>

    <div style="padding: 2rem 1.5rem; background: var(--accent-gradient-subtle); border-top: 1px solid var(--accent-primary); display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">TOTAL UPFRONT</span>
        <span style="font-size: 2rem; font-weight: 800; color: var(--accent-primary);">$${String.format("%,d", pageData.isPetIntent() ? pageData.totalUpfrontEstimate() + pageData.petDeposit() : pageData.totalUpfrontEstimate())}</span>
    </div>
    
    @if(!pageData.isPetIntent())
    <div style="padding: 1rem 1.5rem; text-align: center; border-top: 1px solid var(--border-subtle);">
        <a href="/RentVerdict/verdict/with-pet/${pageData.city().toLowerCase().replace(" ", "-")}-${pageData.state().toLowerCase()}" style="color: var(--accent-primary); text-decoration: none; font-weight: 600; font-size: 0.9rem;">
            🐾 Have a pet? See pet cost breakdown &rarr;
        </a>
    </div>
    @endif
</div>
