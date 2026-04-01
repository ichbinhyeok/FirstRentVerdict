@param String baseUrl = ""

@template.layout.main(
    title = "Manual Approval Support | First Rent Verdict",
    description = "Request a manual apartment approval route if you have bad credit, no cosigner, or move-in cash pressure.",
    canonical = baseUrl + "/RentVerdict/contact",
    content = @`
    <div style="max-width: 700px; margin: 4rem auto; padding: 0 1.5rem;">
        
        <!-- Header -->
        <div style="text-align: center; margin-bottom: 4rem;">
            <div style="font-size: 0.8rem; letter-spacing: 0.15em; text-transform: uppercase; color: var(--text-tertiary); margin-bottom: 0.5rem;">
                Approval Support
            </div>
            <h1 style="font-size: 2.5rem; font-weight: 800; margin-bottom: 1rem;">Request a Manual Approval Route</h1>
            <p style="font-size: 1.1rem; color: var(--text-secondary); max-width: 500px; margin: 0 auto; line-height: 1.6;">
                If the calculator says borderline or denied, send the scenario and get a manual next-step route for bad credit, no cosigner, or move-in cash pressure.
            </p>
        </div>

        <!-- Contact Methods -->
        <div style="display: grid; gap: 2rem; margin-bottom: 4rem;">
            
            <!-- Approval Support Card -->
            <div style="background: linear-gradient(135deg, rgba(245, 158, 11, 0.08) 0%, rgba(255, 255, 255, 0.98) 100%); padding: 2rem; border-radius: 12px; border: 1px solid rgba(245, 158, 11, 0.28);">
                <div style="display: flex; align-items: flex-start; gap: 1rem;">
                    <div style="font-size: 1.5rem;">Route</div>
                    <div>
                        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">Manual Approval Support</h3>
                        <p style="color: var(--text-secondary); margin-bottom: 1rem;">Best for renters who are blocked by weak credit, no cosigner, higher deposits, or a thin move-in buffer.</p>
                        <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                            Include your target city, monthly rent, cash on hand, credit tier, whether you have a cosigner, pet status, and move timeline.
                        </p>
                        <a href="mailto:spainhyeok@gmail.com?subject=Manual%20Approval%20Support%20Request&body=Target%20city%3A%0AMonthly%20rent%3A%0ACash%20on%20hand%3A%0ACredit%20tier%3A%0AHave%20a%20cosigner%3F%3A%0AHave%20a%20pet%3F%3A%0AMove%20timeline%3A%0ABiggest%20approval%20blocker%3A"
                           data-track-event="lead_submit"
                           data-track-category="monetization"
                           data-track-label="contact_manual_approval_support_mailto"
                           style="color: var(--text-primary); font-weight: 600; text-decoration: underline;">
                            Request My Manual Route ->
                        </a>
                        <div style="margin-top: 1rem; font-size: 0.9rem; color: var(--text-tertiary);">
                            Current format: email-based manual support. No sign-up flow yet.
                        </div>
                    </div>
                </div>
            </div>

            <!-- General Email Card -->
            <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; border: 1px solid var(--border-subtle);">
                <div style="display: flex; align-items: flex-start; gap: 1rem;">
                    <div style="font-size: 1.5rem;">Email</div>
                    <div>
                        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">General Contact</h3>
                        <p style="color: var(--text-secondary); margin-bottom: 1rem;">For general questions, feedback, or support that is not tied to an approval scenario.</p>
                        <a href="mailto:spainhyeok@gmail.com" style="color: var(--text-primary); font-weight: 600; text-decoration: underline;">
                            spainhyeok@gmail.com
                        </a>
                    </div>
                </div>
            </div>

            <!-- Data Corrections Card -->
            <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; border: 1px solid var(--border-subtle);">
                <div style="display: flex; align-items: flex-start; gap: 1rem;">
                    <div style="font-size: 1.5rem;">Data</div>
                    <div>
                        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">Data Corrections</h3>
                        <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                            Found an error in our rent data, deposit laws, or moving cost estimates? 
                            We take data accuracy seriously and appreciate corrections.
                        </p>
                        <a href="mailto:spainhyeok@gmail.com?subject=Data%20Correction%20Request" 
                           style="color: var(--text-primary); font-weight: 600; text-decoration: underline;">
                            Submit a Correction ->
                        </a>
                    </div>
                </div>
            </div>

            <!-- Partnership Card -->
            <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; border: 1px solid var(--border-subtle);">
                <div style="display: flex; align-items: flex-start; gap: 1rem;">
                    <div style="font-size: 1.5rem;">Partner</div>
                    <div>
                        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">Partnerships</h3>
                        <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                            Interested in integrating First Rent Verdict into your platform, 
                            or exploring data licensing opportunities?
                        </p>
                        <a href="mailto:spainhyeok@gmail.com?subject=Partnership%20Inquiry"
                           data-track-event="lead_submit"
                           data-track-category="monetization"
                           data-track-label="contact_partnership_mailto"
                           style="color: var(--text-primary); font-weight: 600; text-decoration: underline;">
                            Explore Partnership ->
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Response Time -->
        <div style="text-align: center; padding: 2rem; background: var(--bg-panel); border-radius: 12px; margin-bottom: 4rem;">
            <p style="font-size: 0.9rem; color: var(--text-secondary);">
                <strong>Average Response Time:</strong> We typically respond within 24-48 hours during business days. Manual support is still lightweight and best-effort.
            </p>
        </div>

        <!-- Location Info -->
        <div style="text-align: center; margin-bottom: 4rem;">
            <p style="font-size: 0.9rem; color: var(--text-tertiary);">
                Based in <strong>Seoul, Republic of Korea</strong> | Serving Users Worldwide
            </p>
        </div>

        <!-- Back to Calculator -->
        <div style="text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 2rem;">
            <div style="display: flex; flex-wrap: wrap; justify-content: center; gap: 0.75rem;">
                <a href="/RentVerdict/" class="btn-primary" style="display: inline-block; padding: 1rem 2rem;">
                    Back to Approval Check
                </a>
                <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-secondary" style="display: inline-block; padding: 1rem 2rem;">
                    Open Bad Credit Playbook
                </a>
            </div>
        </div>
    </div>
`)

