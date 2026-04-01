@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import firstrentverdict.content.GuideCatalog
@import firstrentverdict.content.GuideCatalog.GuideEntry
@import java.util.List
@param String city
@param String state
@param List<CityEntry> relatedCities
@param String intentCategory = "approval"
@param String baseUrl = ""

!{
    List<GuideEntry> guideRecommendations = GuideCatalog.recommendationsFor(intentCategory);
}

<!-- Internal Mesh (High-Intent Clustering) -->
<section style="margin-bottom: 4rem; padding: 2rem; background: var(--bg-surface); border-radius: var(--radius-lg); border: 1px solid var(--border-default);">
    <h3 style="font-size: 0.9rem; font-weight: 700; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 1.5rem;">Exploration Guide: ${city}</h3>
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
        <a href="/RentVerdict/verdict/with-pet/${city.toLowerCase().replace(" ", "-").replace(".", "")}-${state.toLowerCase()}" 
           style="padding: 1rem; background: var(--bg-secondary); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); text-decoration: none; color: var(--text-primary); transition: all 0.2s;">
            <div style="font-size: 1.25rem; margin-bottom: 0.5rem;">Pet</div>
            <div style="font-weight: 700; font-size: 0.9rem;">Pet Ownership</div>
            <div style="font-size: 0.75rem; color: var(--text-tertiary);">Deposits & Rules</div>
        </a>
        <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" 
           style="padding: 1rem; background: var(--bg-secondary); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); text-decoration: none; color: var(--text-primary); transition: all 0.2s;">
            <div style="font-size: 1.25rem; margin-bottom: 0.5rem;">Credit</div>
            <div style="font-weight: 700; font-size: 0.9rem;">Bad Credit + No Cosigner</div>
            <div style="font-size: 0.75rem; color: var(--text-tertiary);">Approval Playbook</div>
        </a>
        <a href="/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment" 
           style="padding: 1rem; background: var(--bg-secondary); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); text-decoration: none; color: var(--text-primary); transition: all 0.2s;">
            <div style="font-size: 1.25rem; margin-bottom: 0.5rem;">Cash</div>
            <div style="font-weight: 700; font-size: 0.9rem;">Move-In Cash Guide</div>
            <div style="font-size: 0.75rem; color: var(--text-tertiary);">Buffer Planning</div>
        </a>
        <a href="/RentVerdict/verdict/moving-to/${city.toLowerCase().replace(" ", "-").replace(".", "")}-${state.toLowerCase()}" 
           style="padding: 1rem; background: var(--bg-secondary); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); text-decoration: none; color: var(--text-primary); transition: all 0.2s;">
            <div style="font-size: 1.25rem; margin-bottom: 0.5rem;">Move</div>
            <div style="font-weight: 700; font-size: 0.9rem;">Relocation Cost</div>
            <div style="font-size: 0.75rem; color: var(--text-tertiary);">Long Distance</div>
        </a>
    </div>
</section>

<!-- Intent-Matched Guides -->
<section style="margin-bottom: 4rem; padding: 2rem; background: var(--bg-panel); border-radius: var(--radius-lg); border: 1px solid var(--border-default);">
    <h3 style="font-size: 0.9rem; font-weight: 700; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 1.5rem;">Recommended Playbooks</h3>
    <div style="display: grid; gap: 0.75rem;">
        @for(GuideEntry guide : guideRecommendations)
            <a href="/RentVerdict/guides/${guide.slug()}" style="padding: 1rem; border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); text-decoration: none; color: inherit; background: var(--bg-surface);">
                <div style="font-size: 0.875rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">${guide.title()}</div>
                <div style="font-size: 0.8125rem; color: var(--text-secondary);">${guide.searchIntent()}</div>
            </a>
        @endfor
    </div>
</section>

<section style="margin-bottom: 4rem; padding: 2rem; background: var(--bg-surface); border-radius: var(--radius-lg); border: 1px solid var(--border-default);">
    <h3 style="font-size: 0.9rem; font-weight: 700; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 1.5rem;">Solve the Bottleneck First</h3>
    <div style="display: grid; gap: 0.75rem;">
        <a href="/RentVerdict/guides/rent-with-bad-credit-no-cosigner" style="padding: 1rem; border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); text-decoration: none; color: inherit; background: var(--bg-secondary);">
            <div style="font-size: 0.875rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Low credit and no cosigner?</div>
            <div style="font-size: 0.8125rem; color: var(--text-secondary);">Start with the approval playbook before you spend another application fee.</div>
        </a>
        <a href="/RentVerdict/guides/how-much-cash-do-i-need-to-move-into-an-apartment" style="padding: 1rem; border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); text-decoration: none; color: inherit; background: var(--bg-secondary);">
            <div style="font-size: 0.875rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">How much cash do I need before I sign?</div>
            <div style="font-size: 0.8125rem; color: var(--text-secondary);">Use the move-in cash checklist before you burn an application fee.</div>
        </a>
        <a href="/RentVerdict/guides/apartment-guarantor-services-vs-larger-security-deposit" style="padding: 1rem; border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); text-decoration: none; color: inherit; background: var(--bg-secondary);">
            <div style="font-size: 0.875rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.25rem;">Should you pay for a guarantor or raise the deposit?</div>
            <div style="font-size: 0.8125rem; color: var(--text-secondary);">Compare approval paths before you spend on the wrong one.</div>
        </a>
    </div>
</section>

<!-- Related Cities -->
<footer style="padding-top: 3rem; border-top: 1px solid var(--border-default);">
    <div style="font-size: 0.85rem; font-weight: 700; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 1.5rem;">Compare with Nearby Markets in ${state}</div>
    <div style="display: flex; flex-wrap: wrap; gap: 0.75rem;">
        @for(CityEntry related : relatedCities)
            <a href="/RentVerdict/verdict/${related.city().toLowerCase().replace(" ", "-").replace(".", "")}-${related.state().toLowerCase()}" class="benefit-pill">
                ${related.city()}
            </a>
        @endfor
    </div>
</footer>


