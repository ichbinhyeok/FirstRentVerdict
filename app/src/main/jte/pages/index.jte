@import java.util.List
@import firstrentverdict.model.dtos.CitiesData.CityEntry

@param List<CityEntry> cities

@template.layout.main(
    title = "First Rent Verdict: Can You Afford That Apartment? Free Calculator (2026)",
    description = "Free rent affordability calculator using 2026 market data. Calculate your move-in costs, security deposit, and cash buffer for 100+ US cities.",
    canonical = "https://movecostinfo.com/RentVerdict/",
    content = @`
    <!-- WebApplication Schema -->
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
        <!-- LEFT: Hero -->
        <div class="split-left">
            <div style="max-width: 480px; position: relative; z-index: 1;" class="animate-in">
                <div class="badge badge-accent" style="margin-bottom: var(--space-6);">
                    2026 Market Data
                </div>
                <h1 class="hero-title">
                    Know Your<br>True Move-In Cost
                </h1>
                <p class="hero-subtitle">
                    Don't just check if you can pay the rent. Check if you can survive move-in day.
                </p>
                
                <div style="margin-top: var(--space-12); padding-top: var(--space-6); border-top: 1px solid var(--border-default);">
                    <div style="display: flex; gap: var(--space-8); color: var(--text-tertiary); font-size: 0.875rem;">
                        <div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary);">100+</div>
                            <div>US Cities</div>
                        </div>
                        <div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary);">Free</div>
                            <div>No Sign-up</div>
                        </div>
                        <div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary);">2026</div>
                            <div>Data</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- RIGHT: Calculator Form -->
        <div class="split-right">
            <div class="card animate-in animate-delay-1" style="width: 100%; max-width: 420px; padding: var(--space-8);">
                <form action="/RentVerdict/verdict" method="post">
                    
                    <div class="form-group">
                        <label for="cityState" class="form-label">Target City</label>
                        <select id="cityState" name="cityState" class="form-select" required aria-required="true">
                            <option value="" disabled selected>Select a city...</option>
                            @for(CityEntry city : cities)
                                <option value="${city.city()}|${city.state()}">${city.city()}, ${city.state()}</option>
                            @endfor
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="monthlyRent" class="form-label">Monthly Rent</label>
                        <input type="text" id="monthlyRent" name="monthlyRent" class="form-input" placeholder="$0" inputmode="numeric" required aria-required="true">
                    </div>

                    <div class="form-group">
                        <label for="availableCash" class="form-label">Cash on Hand</label>
                        <input type="text" id="availableCash" name="availableCash" class="form-input" placeholder="$0" inputmode="numeric" required aria-required="true">
                    </div>

                    <div class="checkbox-group">
                        <label class="checkbox-label">
                            <input type="checkbox" name="hasPet" value="true">
                            <span>I have a pet</span>
                        </label>

                    </div>

                    <button type="submit" class="btn btn-primary btn-lg btn-full">
                        Get My Verdict
                    </button>
                    
                    <script>
                        document.addEventListener('DOMContentLoaded', () => {
                            const currencyInputs = ['monthlyRent', 'availableCash'];
                            
                            currencyInputs.forEach(id => {
                                const input = document.getElementById(id);
                                if (!input) return;

                                input.addEventListener('blur', (e) => {
                                    let value = e.target.value.replace(/[$,]/g, '').replace(/[^0-9]/g, '');
                                    if (value) {
                                        e.target.value = '$' + parseInt(value).toLocaleString('en-US');
                                    }
                                });

                                input.addEventListener('focus', (e) => {
                                    e.target.value = e.target.value.replace(/[$,]/g, '');
                                });
                            });

                            document.querySelector('form').addEventListener('submit', (e) => {
                                currencyInputs.forEach(id => {
                                    const input = document.getElementById(id);
                                    if (input) {
                                        input.value = input.value.replace(/[$,]/g, '');
                                    }
                                });
                            });
                        });
                    </script>
                </form>
            </div>
        </div>
    </div>

    <!-- Popular Cities Section -->
    <div style="background: var(--bg-secondary); border-top: 1px solid var(--border-default); padding: var(--space-16) var(--space-6);">
        <div style="max-width: 1000px; margin: 0 auto;">
            <h2 class="section-title" style="margin-bottom: var(--space-6);">Popular Cities</h2>
            <div class="tag-group">
                @for(CityEntry city : cities.subList(0, Math.min(cities.size(), 12)))
                    <a href="/RentVerdict/verdict/${city.city().toLowerCase().replace(" ", "-")}-${city.state().toLowerCase()}" class="tag">
                        ${city.city()}, ${city.state()}
                    </a>
                @endfor
            </div>
            <div style="margin-top: var(--space-6);">
                <a href="/RentVerdict/cities" class="btn btn-secondary">
                    View All 100+ Cities →
                </a>
            </div>
        </div>
    </div>
`)
