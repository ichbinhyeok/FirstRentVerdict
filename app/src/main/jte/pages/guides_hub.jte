@import java.util.List
@import java.util.Map
@param List<Map<String, String>> guides
@param String pageTitle
@param String metaDescription
@param String baseUrl = "https://movecostinfo.com"

@template.layout.main(
    title = pageTitle,
    description = metaDescription,
    canonical = baseUrl + "/RentVerdict/guides",
    content = @`
    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 3rem 1.5rem;">
        
        <!-- Breadcrumbs -->
        <nav style="margin-bottom: 2.5rem; font-size: 0.825rem; color: var(--text-tertiary); display: flex; gap: 0.5rem; align-items: center;">
            <a href="/RentVerdict/" style="color: inherit;">Home</a> 
            <span>/</span> 
            <span style="color: var(--text-secondary); font-weight: 500;">Guides</span>
        </nav>

        <header style="margin-bottom: 4rem;">
            <h1 style="font-size: clamp(2rem, 5vw, 3.25rem); font-weight: 800; line-height: 1.1; margin-bottom: 1.5rem; letter-spacing: -0.03em;">
                <span class="gradient-text">${pageTitle}</span>
            </h1>
            <p style="font-size: 1.25rem; color: var(--text-secondary); line-height: 1.6; max-width: 750px;">
                ${metaDescription}
            </p>
        </header>

        <div style="display: grid; gap: 2rem;">
            @for(Map<String, String> guide : guides)
                <div class="card glass-morphism" style="padding: 2rem; border-radius: var(--radius-lg); border-left: 5px solid var(--accent-primary);">
                    <h2 style="font-size: 1.5rem; margin-bottom: 1rem; font-weight: 700;">
                        <a href="/RentVerdict/guides/${guide.get("slug")}" style="color: var(--text-primary); text-decoration: none;">${guide.get("title")}</a>
                    </h2>
                    <p style="line-height: 1.6; color: var(--text-secondary); margin-bottom: 1.5rem;">${guide.get("excerpt")}</p>
                    <a href="/RentVerdict/guides/${guide.get("slug")}" style="font-weight: 700; color: var(--accent-primary); text-decoration: none;">Read Guide &rarr;</a>
                </div>
            @endfor
        </div>
    </div>
    `
)
