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

    <!-- Market Explorer Hub -->
    <div style="background: var(--bg-secondary); border-top: 1px solid var(--border-default); padding: var(--space-20) var(--space-6);">
        <div style="max-width: 1000px; margin: 0 auto;">
            <div style="text-align: center; margin-bottom: var(--space-12);">
                <h2 class="section-title">Analyze Local Rental Markets</h2>
                <p style="color: var(--text-tertiary); margin-top: var(--space-4);">Real-time liquidity audits for 100+ US cities based on 2026 data.</p>
            </div>
            
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: var(--space-8);">
                <!-- Cluster: Major Markets -->
                <div class="card" style="padding: var(--space-8); border-top: 4px solid var(--accent-primary);">
                    <h3 style="font-size: 1.25rem; margin-bottom: var(--space-6); color: var(--text-primary); display: flex; align-items: center; gap: 0.75rem;">
                        <span>🌆</span> Major Markets
                    </h3>
                    <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                        <a href="/RentVerdict/verdict/new-york-ny" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">Audit New York, NY</a>
                        <a href="/RentVerdict/verdict/los-angeles-ca" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">Audit Los Angeles, CA</a>
                        <a href="/RentVerdict/verdict/chicago-il" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">Audit Chicago, IL</a>
                    </div>
                </div>

                <!-- Cluster: Case Studies -->
                <div class="card" style="padding: var(--space-8); border-top: 4px solid #f59e0b;">
                    <h3 style="font-size: 1.25rem; margin-bottom: var(--space-6); color: var(--text-primary); display: flex; align-items: center; gap: 0.75rem;">
                        <span>🐾</span> Special Intent
                    </h3>
                    <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                        <a href="/RentVerdict/verdict/with-pet/new-york-ny" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #f59e0b; color: #d97706;">Pet-Friendly NY Audit</a>
                        <a href="/RentVerdict/verdict/moving-to/austin-tx" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #f59e0b; color: #d97706;">Moving to Austin Guide</a>
                        <a href="/RentVerdict/verdict/can-i-move-with/5000/to/miami-fl" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #f59e0b; color: #d97706;">$5,000 Miami Savings</a>
                    </div>
                </div>

                <!-- Cluster: Credit Approval -->
                <div class="card" style="padding: var(--space-8); border-top: 4px solid #ef4444;">
                    <h3 style="font-size: 1.25rem; margin-bottom: var(--space-6); color: var(--text-primary); display: flex; align-items: center; gap: 0.75rem;">
                        <span>📉</span> Credit Strategy
                    </h3>
                    <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                        <a href="/RentVerdict/verdict/credit/poor/atlanta-ga" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #ef4444; color: #dc2626;">Poor Credit Atlanta</a>
                        <a href="/RentVerdict/verdict/credit/fair/houston-tx" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #ef4444; color: #dc2626;">Fair Credit Houston</a>
                        <a href="/RentVerdict/verdict/credit/good/seattle-wa" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #ef4444; color: #dc2626;">Good Credit Seattle</a>
                    </div>
                </div>
            </div>

            <div style="margin-top: var(--space-12); text-align: center;">
                <a href="/RentVerdict/cities" class="btn btn-secondary">
                    Browse All Supporting Cities & States →
                </a>
            </div>
        </div>
    </div>
`)
