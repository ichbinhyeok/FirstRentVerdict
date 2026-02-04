package firstrentverdict.controller;

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

    public SitemapController(VerdictDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/RentVerdict/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        List<CitiesData.CityEntry> cities = repository.getAllCities();
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        String monthlyMod = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);

        // 1. Static Pages
        addUrl(xml, "https://movecostinfo.com/RentVerdict/", "1.0", monthlyMod);
        addUrl(xml, "https://movecostinfo.com/RentVerdict/cities", "0.9", monthlyMod);

        // 2. High-Intent pSEO Scenarios (~2,600 target)
        for (CitiesData.CityEntry city : cities) {
            String slug = city.city().toLowerCase().replace(" ", "-") + "-" + city.state().toLowerCase();
            String root = "https://movecostinfo.com/RentVerdict/verdict/";

            // Tier 1: Core Alignment (Target: ~2,600)
            // - City Landing & Relocation (200)
            addUrl(xml, root + slug, "0.8", monthlyMod);
            addUrl(xml, root + "moving-to/" + slug, "0.9", monthlyMod);

            // - Credit (3 tiers x 100 cities = 300)
            addUrl(xml, root + "credit/poor/" + slug, "0.8", monthlyMod);
            addUrl(xml, root + "credit/fair/" + slug, "0.7", monthlyMod);
            addUrl(xml, root + "credit/good/" + slug, "0.6", monthlyMod);

            // - Pet (2 intents x 100 cities = 200)
            addUrl(xml, root + "with-pet/" + slug, "0.8", monthlyMod);
            addUrl(xml, root + "pet-friendly-apartments/" + slug, "0.8", monthlyMod);

            // - Savings Based (4 amounts x 100 cities = 400)
            for (int savings : new int[] { 2000, 3500, 5000, 10000 }) {
                addUrl(xml, root + "can-i-move-with/" + savings + "/to/" + slug, "0.9", monthlyMod);
            }

            // - Moving Pairs (870 target)
            cities.stream()
                    .filter(c -> !c.city().equalsIgnoreCase(city.city()))
                    .limit(9)
                    .forEach(from -> {
                        String fromSlug = from.city().toLowerCase().replace(" ", "-");
                        addUrl(xml,
                                "https://movecostinfo.com/RentVerdict/verdict/moving-from/" + fromSlug + "/to/" + slug,
                                "0.8", monthlyMod);
                    });
        }

        // 3. Rent x State Matrix (13 rent points x 51 states = 663 target)
        String[] states = cities.stream().map(c -> c.state()).distinct().toArray(String[]::new);
        int[] rentPoints = { 800, 1000, 1200, 1400, 1500, 1700, 1800, 2000, 2200, 2500, 3000, 3500, 4000 };
        for (String state : states) {
            for (int rent : rentPoints) {
                addUrl(xml, "https://movecostinfo.com/RentVerdict/first-month-cost/" + rent + "/" + state.toLowerCase(),
                        "0.7", monthlyMod);
            }
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
