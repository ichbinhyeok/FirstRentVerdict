@import java.util.List
@import firstrentverdict.model.dtos.CitiesData.CityEntry

@param List<CityEntry> cities

@template.layout.main(
    title = "First Rent Verdict: Can You Afford That Apartment? Free Calculator (2026)",
    description = "Free rent affordability calculator using 2026 market data. Calculate your move-in costs, security deposit, and cash buffer for 100+ US cities.",
    canonical = "https://movecostinfo.com/RentVerdict/",
    content = @`
    <!-- WebApplication Schema for Search Engines -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "WebApplication",
      "name": "First Rent Verdict",
      "url": "https://movecostinfo.com/RentVerdict/",
      "description": "Free rent affordability calculator using 2026 market data for 100+ US cities.",
      "applicationCategory": "FinanceApplication",
      "operatingSystem": "Any",
      "offers": {
        "@type": "Offer",
        "price": "0",
        "priceCurrency": "USD"
      }
    }
    </script>
    <div class="split-layout">
        <!-- LEFT: Identity & Authority -->
        <div class="split-left animate-entry">
            <div style="max-width: 440px;">
                <h1 class="hero-title">First Apartment<br>True Cost Verdict</h1>
                <p class="hero-subtitle">
                    This is not a marketing suggestion.<br>
                    It is a mathematical certainty based on 2026 economic data.
                </p>
                <div style="margin-top: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);">
                    <p style="font-size: 0.8rem; color: var(--text-tertiary);">
                        CONFIDENTIAL RISK ASSESSMENT<br>
                        DO NOT DISTRIBUTE OUTSIDE DECISION CIRCLE
                    </p>
                </div>
            </div>
        </div>

        <!-- RIGHT: Evaluation Form -->
        <div class="split-right">
            <div class="form-container animate-entry" style="animation-delay: 0.1s;">
                <form action="/RentVerdict/verdict" method="post">
                    
                    <div class="form-group">
                        <label for="cityState">Jurisdiction (Target City)</label>
                        <select id="cityState" name="cityState" required aria-required="true" aria-describedby="cityStateHint">
                            <option value="" disabled selected>Select Jurisdiction...</option>
                            @for(CityEntry city : cities)
                                <option value="${city.city()}|${city.state()}">${city.city()}, ${city.state()}</option>
                            @endfor
                        </select>
                        <span id="cityStateHint" class="visually-hidden">Select the city where you plan to rent</span>
                    </div>

                    <div class="form-group">
                        <label for="monthlyRent">Base Monthly Liability (Rent)</label>
                        <input type="text" id="monthlyRent" name="monthlyRent" placeholder="0" inputmode="numeric" required aria-required="true" aria-describedby="rentHint">
                        <span id="rentHint" class="visually-hidden">Enter your expected monthly rent amount in dollars</span>
                    </div>

                    <div class="form-group">
                        <label for="availableCash">Liquid Assets (Cash on Hand)</label>
                        <input type="text" id="availableCash" name="availableCash" placeholder="0" inputmode="numeric" required aria-required="true" aria-describedby="cashHint">
                        <span id="cashHint" class="visually-hidden">Enter total cash available for move-in costs</span>
                    </div>

                    <div class="checkbox-group">
                        <label class="checkbox-label">
                            <input type="checkbox" name="hasPet" value="true">
                            <span>Pet</span>
                        </label>
                        <label class="checkbox-label">
                            <input type="checkbox" name="isLocalMove" value="true" checked>
                            <span>Local Move</span>
                        </label>
                    </div>

                    <button type="submit" class="btn-primary">Execute Verdict Analysis</button>
                    
                    <script>
                        document.addEventListener('DOMContentLoaded', () => {
                            const currencyInputs = ['monthlyRent', 'availableCash'];
                            
                            currencyInputs.forEach(id => {
                                const input = document.getElementById(id);
                                if (!input) return;

                                // Format with commas when leaving the field
                                input.addEventListener('blur', (e) => {
                                    let value = e.target.value.replace(/,/g, '').replace(/[^0-9]/g, '');
                                    if (value) {
                                        e.target.value = parseInt(value).toLocaleString('en-US');
                                    }
                                });

                                // Remove commas when entering to edit
                                input.addEventListener('focus', (e) => {
                                    e.target.value = e.target.value.replace(/,/g, '');
                                });
                            });

                            document.querySelector('form').addEventListener('submit', (e) => {
                                currencyInputs.forEach(id => {
                                    const input = document.getElementById(id);
                                    if (input) {
                                        input.value = input.value.replace(/,/g, '');
                                    }
                                });
                            });
                        });
                    </script>
                    
                    <p style="text-align: center; margin-top: 2rem; font-size: 0.75rem; color: var(--text-tertiary);">
                        Simulation ID: ${java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase()}
                    </p>
                </form>
            </div>
        </div>
    </div>

    <!-- Jurisdictions List for pSEO Discovery -->
    <div style="background: var(--bg-panel); border-top: 1px solid var(--border-subtle); padding: 4rem 2rem;">
        <div style="max-width: 1000px; margin: 0 auto;">
            <h2 style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.1em; color: var(--text-tertiary); margin-bottom: 2rem;">
                Popular Jurisdictions
            </h2>
            <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 1rem;">
                @for(CityEntry city : cities.subList(0, Math.min(cities.size(), 12)))
                    <a href="/RentVerdict/verdict/${city.city().toLowerCase().replace(" ", "-")}-${city.state().toLowerCase()}" 
                       style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem;">
                        ${city.city()}, ${city.state()} &rarr;
                    </a>
                @endfor
            </div>
            <div style="margin-top: 2rem; text-align: center;">
                <a href="/RentVerdict/cities" class="btn-primary" style="display: inline-block; width: auto; padding: 0.8rem 2rem; font-size: 0.9rem; background: var(--bg-panel); color: var(--text-primary); border: 1px solid var(--border-subtle);">
                    View All 100+ Jurisdictions &rarr;
                </a>
            </div>
        </div>
    </div>
`)
