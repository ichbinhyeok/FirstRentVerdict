@import firstrentverdict.content.GuideCatalog.GuideEntry
@param GuideEntry guide
@param String baseUrl = ""

!{
    boolean isBadCreditGuide = "rent-with-bad-credit-no-cosigner".equals(guide.slug());
    boolean isCashGuide = "how-much-cash-do-i-need-to-move-into-an-apartment".equals(guide.slug());
    boolean isBackstopGuide = "apartment-guarantor-services-vs-larger-security-deposit".equals(guide.slug());
}

@template.layout.main(
    title = guide.title(),
    description = guide.excerpt(),
    canonical = baseUrl + "/RentVerdict/guides/" + guide.slug(),
    content = @`
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "FAQPage",
      "mainEntity": [
        @for(int i=0; i < guide.faqs().size(); i++)
        {
          "@type": "Question",
          "name": "$unsafe{guide.faqs().get(i).question().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "")}",
          "acceptedAnswer": {
            "@type": "Answer",
            "text": "$unsafe{guide.faqs().get(i).answer().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "")}"
          }
        }${i < guide.faqs().size() - 1 ? "," : ""}
        @endfor
      ]
    }
    </script>

    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 3rem 1.5rem;">
        <!-- Breadcrumbs -->
        <nav style="margin-bottom: 2.5rem; font-size: 0.825rem; color: var(--text-tertiary); display: flex; gap: 0.5rem; align-items: center;">
            <a href="/RentVerdict/" style="color: inherit;">Home</a>
            <span>/</span>
            <a href="/RentVerdict/guides" style="color: inherit;">Guides</a>
            <span>/</span>
            <span style="color: var(--text-secondary); font-weight: 500;">${guide.title()}</span>
        </nav>

        <header style="margin-bottom: 4rem;">
            <h1 style="font-size: clamp(2rem, 5vw, 3.25rem); font-weight: 800; line-height: 1.1; margin-bottom: 1.5rem; letter-spacing: -0.03em;">
                <span class="gradient-text">${guide.title()}</span>
            </h1>
            <p style="font-size: 1.25rem; color: var(--text-secondary); line-height: 1.6; max-width: 750px;">
                ${guide.excerpt()}
            </p>
            <div style="margin-top: 1.25rem; display: flex; gap: 0.75rem; flex-wrap: wrap;">
                <span class="badge badge-outline">Persona: ${guide.persona()}</span>
                <span class="badge badge-outline">Intent: ${guide.searchIntent()}</span>
                <span class="badge badge-outline">Keyword: ${guide.primaryKeyword()}</span>
            </div>
        </header>

        <section style="margin-bottom: 2rem; padding: 1.25rem 1.5rem; border-radius: 12px; border: 1px solid var(--border-subtle); background: var(--bg-surface);">
            <div style="font-size: 0.8rem; margin-bottom: 0.5rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-tertiary); font-weight: 700;">Approval-First Route</div>
            <div style="display: flex; flex-wrap: wrap; gap: 0.75rem;">
                @if(!isBadCreditGuide)
                    <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-intent">Bad Credit + No Cosigner</a>
                @endif
                @if(!isCashGuide)
                    <a href="/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment" class="btn btn-intent">Move-In Cash Guide</a>
                @endif
                @if(!isBackstopGuide)
                    <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-intent">Guarantor vs Deposit</a>
                @endif
                <a href="/RentVerdict/" class="btn btn-intent">Run Calculator</a>
            </div>
        </section>

        @if(isBadCreditGuide)
            <section style="margin-bottom: 2rem; padding: 1.25rem 1.5rem; border-radius: 12px; border: 1px solid rgba(245, 158, 11, 0.28); background: linear-gradient(135deg, rgba(245, 158, 11, 0.08) 0%, rgba(255, 255, 255, 0.98) 100%);">
                <div style="font-size: 0.8rem; margin-bottom: 0.5rem; text-transform: uppercase; letter-spacing: 0.05em; color: #b45309; font-weight: 700;">Manual Route</div>
                <h2 style="font-size: 1.2rem; color: var(--text-primary); margin-bottom: 0.5rem;">Need a realistic approval route, not generic advice?</h2>
                <p style="margin: 0 0 1rem; color: var(--text-secondary); line-height: 1.6;">
                    Send your city, rent, cash, credit tier, and cosigner status. The manual route is the fastest way to compare whether you should try a flexible landlord, a guarantor, or a different price point first.
                </p>
                <div style="display: flex; flex-wrap: wrap; gap: 0.75rem;">
                    <a href="/RentVerdict/contact" class="btn btn-primary">Request Manual Approval Route</a>
                    <a href="/RentVerdict/" class="btn btn-secondary">Run Approval Check</a>
                </div>
            </section>
        @endif

        <article style="line-height: 1.8; color: var(--text-secondary); margin-bottom: 6rem; font-size: 1.125rem;">
            <div style="background: var(--bg-surface); padding: 2rem; border-radius: 12px; margin-bottom: 2rem; border: 1px solid var(--border-subtle);">
                <h2 style="font-size: 1.25rem; color: var(--text-primary); margin-bottom: 0.75rem;">Who This Is For</h2>
                <p style="margin: 0;">
                    ${guide.persona()} searching for a practical execution path. This page is built for decision support and immediate action.
                </p>
            </div>

            <h2 style="font-size: 2rem; color: var(--text-primary); margin-bottom: 1.5rem; margin-top: 3rem;">Execution Checklist</h2>
            <ul style="list-style-type: disc; padding-left: 2rem; margin-bottom: 2rem;">
                @for(String step : guide.checklist())
                    <li>${step}</li>
                @endfor
            </ul>

            <h2 style="font-size: 2rem; color: var(--text-primary); margin-bottom: 1.5rem; margin-top: 3rem;">Common Mistakes</h2>
            <ul style="list-style-type: disc; padding-left: 2rem; margin-bottom: 2rem;">
                @for(String pitfall : guide.pitfalls())
                    <li>${pitfall}</li>
                @endfor
            </ul>

            <h2 style="font-size: 2rem; color: var(--text-primary); margin-bottom: 1.5rem; margin-top: 3rem;">FAQ</h2>
            <div style="display: grid; gap: 1rem; margin-bottom: 2rem;">
                @for(var faq : guide.faqs())
                    <div style="border: 1px solid var(--border-subtle); border-radius: 10px; padding: 1rem 1.25rem; background: var(--bg-surface);">
                        <h3 style="font-size: 1rem; color: var(--text-primary); margin-bottom: 0.5rem;">${faq.question()}</h3>
                        <p style="margin: 0; color: var(--text-secondary);">${faq.answer()}</p>
                    </div>
                @endfor
            </div>

            <h2 style="font-size: 2rem; color: var(--text-primary); margin-bottom: 1.5rem; margin-top: 3rem;">Next Action</h2>
            <p>Move from content to decision by running your numbers with live city-level cost data and approval logic.</p>
            <div style="display: flex; flex-wrap: wrap; gap: 0.75rem; margin-top: 2rem;">
                <a href="${guide.ctaPath()}" class="btn btn-primary">${guide.ctaLabel()}</a>
                @if(isBadCreditGuide)
                    <a href="/RentVerdict/" class="btn btn-secondary">Run My Approval Check</a>
                    <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" class="btn btn-secondary">Compare Guarantor vs Deposit</a>
                @else
                    <a href="/RentVerdict/cities" class="btn btn-outline-primary">Browse All City Audits</a>
                @endif
                @if(!isBadCreditGuide)
                    <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" class="btn btn-secondary">Open Bad Credit Playbook</a>
                @endif
            </div>
        </article>
    </div>
    `
)
