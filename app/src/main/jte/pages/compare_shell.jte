@import firstrentverdict.service.seo.CityContentGenerator.CityPageContent
@param CityPageContent pageData1
@param CityPageContent pageData2
@param String canonicalUrl = null
@param String baseUrl = ""

@template.layout.main(
    title = pageData1.city() + " vs " + pageData2.city(),
    description = "Compare cost of moving between " + pageData1.city() + " and " + pageData2.city(),
    canonical = canonicalUrl,
    content = @`
    <div class="container" style="max-width: 900px; margin: 0 auto; padding: 2rem 1.5rem;">
        <h1>${pageData1.city()} vs ${pageData2.city()}</h1>
        <p>This is the compare shell.</p>
    </div>
    `
)


