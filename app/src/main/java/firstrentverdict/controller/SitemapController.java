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

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        List<CitiesData.CityEntry> cities = repository.getAllCities();
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // SEO Strategy: Use 1st day of current month to signal stable, monthly updates
        // instead of daily changes which can be flagged as artificial.
        String monthlyMod = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);

        // 1. Static Pages
        addUrl(xml, "https://movecostinfo.com/", "1.0", monthlyMod); // Hub Root
        addUrl(xml, "https://movecostinfo.com/RentVerdict/", "1.0", monthlyMod); // Engine Home
        addUrl(xml, "https://movecostinfo.com/RentVerdict/cities", "0.9", monthlyMod); // All Cities List
        addUrl(xml, "https://movecostinfo.com/RentVerdict/about", "0.6", monthlyMod);
        addUrl(xml, "https://movecostinfo.com/RentVerdict/methodology", "0.6", monthlyMod);
        addUrl(xml, "https://movecostinfo.com/RentVerdict/guide/rent-affordability-rule", "0.9", monthlyMod); // Anchor
                                                                                                              // Article
        addUrl(xml, "https://movecostinfo.com/RentVerdict/privacy", "0.4", monthlyMod);
        addUrl(xml, "https://movecostinfo.com/RentVerdict/terms", "0.5", monthlyMod);

        // 2. City Pages (pSEO)
        for (CitiesData.CityEntry city : cities) {
            String slug = city.city().toLowerCase().replace(" ", "-") + "-" + city.state().toLowerCase();
            String location = "https://movecostinfo.com/RentVerdict/verdict/" + slug;
            addUrl(xml, location, "0.8", monthlyMod);
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
