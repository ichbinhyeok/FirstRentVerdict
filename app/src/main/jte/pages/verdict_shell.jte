@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@param CityPageContent pageData
@param String canonicalUrl = null
@param String baseUrl = ""

@template.layout.main(
    title = pageData.pageTitle(),
    description = pageData.metaDescription(),
    canonical = canonicalUrl,
    content = @`
    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 2rem 1.5rem;">
        <h1>${pageData.pageTitle()}</h1>
        <p>This is the generic verdict shell.</p>
    </div>
    `
)


