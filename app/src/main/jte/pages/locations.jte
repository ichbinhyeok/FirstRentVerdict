@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import java.util.List
@import java.util.Map

@param Map<String, List<CityEntry>> citiesByState

@template.layout.main(
    title = "Available Jurisdictions - First Rent Verdict",
    description = "Complete index of supported cities for rent affordability analysis.",
    content = @`
    <div style="max-width: 1000px; margin: 4rem auto; padding: 0 2rem;">
        <div style="text-align: center; margin-bottom: 4rem;">
            <h1 class="hero-title" style="font-size: 2.5rem;">Supported Jurisdictions</h1>
            <p class="hero-subtitle" style="margin: 0 auto;">Select your city to begin the affordability audit.</p>
        </div>

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
