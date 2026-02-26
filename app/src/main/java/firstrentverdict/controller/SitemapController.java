package firstrentverdict.controller;

import firstrentverdict.content.GuideCatalog;
import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.repository.VerdictDataRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class SitemapController {

    private final VerdictDataRepository repository;
    private final String baseUrl;

    public SitemapController(VerdictDataRepository repository,
            @org.springframework.beans.factory.annotation.Value("${app.base-url}") String baseUrl) {
        this.repository = repository;
        this.baseUrl = baseUrl;
    }

    @GetMapping(value = { "/sitemap.xml", "/RentVerdict/sitemap.xml" }, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        List<CitiesData.CityEntry> cities = repository.getAllCities();
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        String monthlyMod = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);

        // 1. Static Pages
        addUrl(xml, baseUrl + "/RentVerdict/", "1.0", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/cities", "0.9", monthlyMod);

        // 1b. Guides
        addUrl(xml, baseUrl + "/RentVerdict/guides", "0.9", monthlyMod);
        for (GuideCatalog.GuideEntry guide : GuideCatalog.all()) {
            addUrl(xml, baseUrl + "/RentVerdict/guides/" + guide.slug(), "0.8", monthlyMod);
        }

        // 2. High-Intent pSEO Scenarios
        for (CitiesData.CityEntry city : cities) {
            String slug = city.city().toLowerCase().replace(" ", "-") + "-" + city.state().toLowerCase();
            String root = baseUrl + "/RentVerdict/verdict/";

            // Tier 1: Core Alignment (Target: ~2,600)
            // - City Landing & Relocation (200)
            addUrl(xml, root + slug, "0.8", monthlyMod);
            addUrl(xml, root + "moving-to/" + slug, "0.9", monthlyMod);

            // - Credit (3 tiers x 100 cities = 300)
            addUrl(xml, root + "credit/poor/" + slug, "0.8", monthlyMod);
            addUrl(xml, root + "credit/fair/" + slug, "0.7", monthlyMod);
            addUrl(xml, root + "credit/good/" + slug, "0.6", monthlyMod);

            // - Pet (1 intent x 100 cities = 100)
            addUrl(xml, root + "with-pet/" + slug, "0.8", monthlyMod);

            // - Savings Based
            for (int savings : new int[] { 3000, 5000, 10000 }) {
                addUrl(xml, root + "can-i-move-with/" + savings + "/to/" + slug, "0.9", monthlyMod);
            }

            // Placeholders removed from sitemap (Salary Needed, No Cosigner)

            // - Moving Pairs (870 target)
            cities.stream()
                    .filter(c -> !c.city().equalsIgnoreCase(city.city()))
                    .limit(9)
                    .forEach(from -> {
                        String fromSlug = from.city().toLowerCase().replace(" ", "-") + "-"
                                + from.state().toLowerCase();
                        addUrl(xml,
                                baseUrl + "/RentVerdict/verdict/moving-from/" + fromSlug + "/to/" + slug,
                                "0.8", monthlyMod);
                    });

            // Placeholders removed from sitemap (Compare Pairs)
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String loc, String priority, String lastmod) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(loc).append("</loc>\n");
        xml.append("    <lastmod>").append(lastmod).append("</lastmod>\n");
        xml.append("    <changefreq>monthly</changefreq>\n");
        xml.append("    <priority>").append(priority).append("</priority>\n");
        xml.append("  </url>\n");
    }
}
