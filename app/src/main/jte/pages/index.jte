@import java.util.List
@import firstrentverdict.model.dtos.CitiesData.CityEntry

@param List<CityEntry> cities
@param String baseUrl = ""

@template.layout.main(
    title = "Bad Credit Apartment Approval Calculator (2026) | First Rent Verdict",
    description = "Check whether you can get approved with bad credit and no cosigner. Estimate move-in cash, deposits, and the safest approval route before you apply.",
    canonical = baseUrl + "/RentVerdict/",
    content = @`
    <!-- WebApplication Schema -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "WebApplication",
      "name": "First Rent Verdict",
      "url": "${baseUrl}/RentVerdict/",
      "description": "Free apartment approval rescue tool for renters with bad credit, no cosigner, and move-in cash pressure.",
      "applicationCategory": "FinanceApplication",
      "operatingSystem": "Any",
      "offers": {
        "@type": "Offer",
        "price": "0",
        "priceCurrency": "USD"
      }
    }
    </script>

    <!-- Organization Schema -->
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "Organization",
      "name": "First Rent Verdict",
      "url": "${baseUrl}/RentVerdict/",
      "logo": "${baseUrl}/images/favicon.svg"
    }
    </script>

    <div class="split-layout">
        <div class="split-left">
            <div style="max-width: 480px; position: relative; z-index: 1;" class="animate-in">
                <div class="badge badge-accent" style="margin-bottom: var(--space-6);">
                    2026 Approval Rescue Tool
                </div>
                <h1 class="hero-title">
                    Can You Get Approved<br>With Bad Credit<br>and No Cosigner?
                </h1>
                <p class="hero-subtitle">
                    Run the approval check first. Then use the right playbook for weak credit, no cosigner, and move-in cash pressure before you waste another application fee.
                </p>

                <div style="margin-top: var(--space-8); padding: var(--space-5); border: 1px solid rgba(245, 158, 11, 0.35); border-radius: var(--radius-md); background: rgba(245, 158, 11, 0.06);">
                    <div style="font-size: 0.75rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 0.5rem;">
                        Start Here
                    </div>
                    <div style="display: flex; flex-wrap: wrap; gap: 0.75rem;">
                        <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-secondary btn-sm" style="border-color: #f59e0b; color: #d97706;">
                            Bad Credit + No Cosigner
                        </a>
                        <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-secondary btn-sm" style="border-color: #f59e0b; color: #d97706;">
                            Guarantor vs Deposit
                        </a>
                        <a href="/RentVerdict/contact" class="btn btn-secondary btn-sm" style="border-color: #f59e0b; color: #d97706;">
                            Request Manual Route
                        </a>
                    </div>
                </div>

                <div style="margin-top: var(--space-12); padding-top: var(--space-6); border-top: 1px solid var(--border-default);">
                    <div style="display: flex; gap: var(--space-8); color: var(--text-tertiary); font-size: 0.875rem;">
                        <div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary);">1</div>
                            <div>Main Wedge</div>
                        </div>
                        <div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary);">3</div>
                            <div>Approval Playbooks</div>
                        </div>
                        <div>
                            <div style="font-size: 1.5rem; font-weight: 700; color: var(--text-primary);">Free</div>
                            <div>No Sign-up</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="split-right">
            <div class="card animate-in animate-delay-1" style="width: 100%; max-width: 420px; padding: var(--space-8);">
                <form action="/RentVerdict/verdict" method="post" data-track-event="cta_click" data-track-category="engagement" data-track-label="verdict_form_submit">
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
                        Run Approval Check
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

                            document.querySelector('form').addEventListener('submit', () => {
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

    <div style="background: var(--bg-secondary); border-top: 1px solid var(--border-default); padding: var(--space-20) var(--space-6);">
        <div style="max-width: 1000px; margin: 0 auto;">
            <div style="text-align: center; margin-bottom: var(--space-12);">
                <h2 class="section-title">Start With the Approval Rescue Route</h2>
                <p style="color: var(--text-tertiary); margin-top: var(--space-4);">The site is now organized around one question: can you still get approved if your profile is weaker than the listing wants?</p>
            </div>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: var(--space-8);">
                <div class="card" style="padding: var(--space-8); border-top: 4px solid var(--accent-primary);">
                    <h3 style="font-size: 1.25rem; margin-bottom: var(--space-6); color: var(--text-primary); display: flex; align-items: center; gap: 0.75rem;">
                        <span>Cash</span> Move-In Cash
                    </h3>
                    <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                        <a href="/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">How Much Cash Do I Need?</a>
                        <a href="/RentVerdict/research/move-in-cost-index" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">Monthly Move-In Cost Index</a>
                        <a href="/RentVerdict/verdict/can-i-move-with/5000/to/miami-fl" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">Can I Move With $5,000?</a>
                    </div>
                </div>

                <div class="card" style="padding: var(--space-8); border-top: 4px solid #f59e0b;">
                    <h3 style="font-size: 1.25rem; margin-bottom: var(--space-6); color: var(--text-primary); display: flex; align-items: center; gap: 0.75rem;">
                        <span>Approval</span> Approval Barriers
                    </h3>
                    <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                        <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #f59e0b; color: #d97706;">Bad Credit + No Cosigner</a>
                        <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #f59e0b; color: #d97706;">Guarantor vs Deposit</a>
                        <a href="/RentVerdict/guides/second-chance-apartments-qualification" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #f59e0b; color: #d97706;">Second-Chance Qualification</a>
                    </div>
                </div>

                <div class="card" style="padding: var(--space-8); border-top: 4px solid #ef4444;">
                    <h3 style="font-size: 1.25rem; margin-bottom: var(--space-6); color: var(--text-primary); display: flex; align-items: center; gap: 0.75rem;">
                        <span>Friction</span> Fee and Pet Friction
                    </h3>
                    <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                        <a href="/RentVerdict/guides/pet-deposit-and-pet-rent-negotiation-guide" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #ef4444; color: #dc2626;">Pet Fee Negotiation</a>
                        <a href="/RentVerdict/verdict/with-pet/new-york-ny" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #ef4444; color: #dc2626;">Pet-Friendly NY Audit</a>
                        <a href="/RentVerdict/verdict/credit/poor/atlanta-ga" class="btn btn-intent" style="justify-content: flex-start; text-align: left; border-color: #ef4444; color: #dc2626;">Poor Credit Atlanta</a>
                    </div>
                </div>
            </div>

            <div style="margin-top: var(--space-10);">
                <h3 style="font-size: 1rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: var(--space-4);">
                    Approval Playbooks
                </h3>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: var(--space-4);">
                    <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">
                        Bad Credit + No Cosigner Plan
                    </a>
                    <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">
                        Guarantor vs Deposit
                    </a>
                    <a href="/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">
                        Move-In Cash Guide
                    </a>
                    <a href="/RentVerdict/contact" class="btn btn-intent" style="justify-content: flex-start; text-align: left;">
                        Request Manual Approval Route
                    </a>
                </div>
            </div>

            <div style="margin-top: var(--space-10);">
                <h3 style="font-size: 1rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: var(--space-4);">
                    Supporting Evidence
                </h3>
                <a href="/RentVerdict/research/move-in-cost-index" class="btn btn-intent" style="display: inline-flex; align-items: center; gap: 0.5rem;">
                    Monthly Move-In Cost Index (Top Markets)
                </a>
            </div>

            <div style="margin-top: var(--space-12); text-align: center;">
                <a href="/RentVerdict/cities" class="btn btn-secondary">
                    Browse Supporting Cities
                </a>
            </div>
        </div>
    </div>
`)
