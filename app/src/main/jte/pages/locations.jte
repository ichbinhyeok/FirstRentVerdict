@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import java.util.List
@import java.util.Map

@param Map<String, List<CityEntry>> citiesByState

@template.layout.main(
    title = "Available Jurisdictions - First Rent Verdict",
    description = "Complete index of supported cities for rent affordability analysis.",
    canonical = "https://movecostinfo.com/RentVerdict/cities",
    content = @`
    <div style="max-width: 1000px; margin: 4rem auto; padding: 0 2rem;">
        <div style="text-align: center; margin-bottom: 4rem;">
            <h1 class="hero-title" style="font-size: 2.5rem;">Supported Jurisdictions</h1>
            <p class="hero-subtitle" style="margin: 0 auto;">Select your city to begin the affordability audit.</p>
        </div>

        <!-- Specialized Intent Hub -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1.5rem; margin-bottom: 4rem;">
            <a href="/RentVerdict/verdict/with-pet/new-york-ny" style="text-decoration: none; color: inherit; transition: all 0.2s ease;" class="intent-card">
                <div style="padding: 2rem 1.5rem; background: var(--bg-surface); border: 2px solid var(--border-subtle); border-radius: 16px; text-align: center;">
                    <div style="font-size: 2.25rem; margin-bottom: 0.75rem;">🐾</div>
                    <div style="font-weight: 800; font-size: 1.1rem; color: var(--text-primary);">Pet Audit Guide</div>
                    <div style="font-size: 0.85rem; color: var(--text-tertiary); margin-top: 0.5rem;">New York Pet Rules</div>
                </div>
            </a>
            <a href="/RentVerdict/verdict/credit/poor/atlanta-ga" style="text-decoration: none; color: inherit; transition: all 0.2s ease;" class="intent-card">
                <div style="padding: 2rem 1.5rem; background: var(--bg-surface); border: 2px solid var(--border-subtle); border-radius: 16px; text-align: center;">
                    <div style="font-size: 2.25rem; margin-bottom: 0.75rem;">📉</div>
                    <div style="font-weight: 800; font-size: 1.1rem; color: var(--text-primary);">Poor Credit Guide</div>
                    <div style="font-size: 0.85rem; color: var(--text-tertiary); margin-top: 0.5rem;">Atlanta Approval Strategy</div>
                </div>
            </a>
            <a href="/RentVerdict/verdict/moving-to/austin-tx" style="text-decoration: none; color: inherit; transition: all 0.2s ease;" class="intent-card">
                <div style="padding: 2rem 1.5rem; background: var(--bg-surface); border: 2px solid var(--border-subtle); border-radius: 16px; text-align: center;">
                    <div style="font-size: 2.25rem; margin-bottom: 0.75rem;">📦</div>
                    <div style="font-weight: 800; font-size: 1.1rem; color: var(--text-primary);">Relocation Audit</div>
                    <div style="font-size: 0.85rem; color: var(--text-tertiary); margin-top: 0.5rem;">Austin Cost Breakdown</div>
                </div>
            </a>
            <a href="/RentVerdict/verdict/can-i-move-with/5000/to/miami-fl" style="text-decoration: none; color: inherit; transition: all 0.2s ease;" class="intent-card">
                <div style="padding: 2rem 1.5rem; background: var(--bg-surface); border: 2px solid var(--border-subtle); border-radius: 16px; text-align: center;">
                    <div style="font-size: 2.25rem; margin-bottom: 0.75rem;">💰</div>
                    <div style="font-weight: 800; font-size: 1.1rem; color: var(--text-primary);">Savings Analysis</div>
                    <div style="font-size: 0.85rem; color: var(--text-tertiary); margin-top: 0.5rem;">Miami with $5,000</div>
                </div>
            </a>
        </div>

        <!-- Popular Markets (Direct Access) -->
        <div style="margin-bottom: 5rem;">
            <h2 style="font-size: 1.1rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.1em; color: var(--text-tertiary); margin-bottom: 1.5rem; text-align: center;">Top Market Audits</h2>
            <div style="display: flex; flex-wrap: wrap; gap: 1rem; justify-content: center;">
                <a href="/RentVerdict/verdict/new-york-ny" class="btn btn-intent" style="padding: 1rem 2rem; border-radius: 50px; font-weight: 700;">NEW YORK, NY</a>
                <a href="/RentVerdict/verdict/los-angeles-ca" class="btn btn-intent" style="padding: 1rem 2rem; border-radius: 50px; font-weight: 700;">LOS ANGELES, CA</a>
                <a href="/RentVerdict/verdict/chicago-il" class="btn btn-intent" style="padding: 1rem 2rem; border-radius: 50px; font-weight: 700;">CHICAGO, IL</a>
                <a href="/RentVerdict/verdict/atlanta-ga" class="btn btn-intent" style="padding: 1rem 2rem; border-radius: 50px; font-weight: 700;">ATLANTA, GA</a>
                <a href="/RentVerdict/verdict/houston-tx" class="btn btn-intent" style="padding: 1rem 2rem; border-radius: 50px; font-weight: 700;">HOUSTON, TX</a>
            </div>
        </div>

        <style>
            .intent-card:hover { transform: translateY(-4px); }
            .intent-card > div:hover { border-color: var(--accent-primary) !important; box-shadow: 0 8px 30px rgba(99, 102, 241, 0.15); }
        </style>

        <div class="split-layout" style="display: block; border: none; min-height: auto;">
             <div style="column-count: 1; column-gap: 2rem;">
                @for(String state : citiesByState.keySet())
                    <div style="break-inside: avoid; margin-bottom: 2rem; background: var(--bg-panel); padding: 1.5rem; border-radius: 8px;">
                        <h2 style="font-size: 1.2rem; margin-bottom: 1rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;">${state}</h2>
                        <ul style="list-style: none; padding: 0;">
                            @for(CityEntry city : citiesByState.get(state))
                                <li style="margin-bottom: 0.5rem;">
                                    <a href="/RentVerdict/verdict/${city.city().toLowerCase().replace(" ", "-")}-${city.state().toLowerCase()}" 
                                       style="text-decoration: none; color: var(--text-primary); font-size: 0.95rem; display: block; padding: 0.25rem 0;">
                                        ${city.city()}
                                    </a>
                                </li>
                            @endfor
                        </ul>
                    </div>
                @endfor
             </div>
        </div>
        
        <style>
             @media (min-width: 768px) {
                 div[style*="column-count: 1"] {
                     column-count: 3 !important;
                 }
             }
             @media (min-width: 1024px) {
                 div[style*="column-count: 1"] {
                     column-count: 4 !important;
                 }
             }
        </style>

        <div class="back-link-container" style="text-align: center; margin-top: 4rem;">
            <a href="/RentVerdict/" class="back-link">&larr; Return to Engine</a>
        </div>
    </div>
`)
