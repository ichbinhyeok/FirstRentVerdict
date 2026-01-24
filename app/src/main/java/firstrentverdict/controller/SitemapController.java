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

        // 1. Static Pages
        addUrl(xml, "https://movecostinfo.com/", "1.0"); // Hub Root
        addUrl(xml, "https://movecostinfo.com/RentVerdict/", "1.0"); // Engine Home
        addUrl(xml, "https://movecostinfo.com/RentVerdict/cities", "0.9"); // All Cities List
        addUrl(xml, "https://movecostinfo.com/RentVerdict/about", "0.6");
        addUrl(xml, "https://movecostinfo.com/RentVerdict/methodology", "0.6");
        addUrl(xml, "https://movecostinfo.com/RentVerdict/guide/rent-affordability-rule", "0.9"); // Anchor Article

        // 2. City Pages (pSEO)
        // Using current date as lastmod since data is "2026 updated"
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        for (CitiesData.CityEntry city : cities) {
            String slug = city.city().toLowerCase().replace(" ", "-") + "-" + city.state().toLowerCase();
            String location = "https://movecostinfo.com/verdict/" + slug;

            xml.append("  <url>\n");
            xml.append("    <loc>").append(location).append("</loc>\n");
            xml.append("    <lastmod>").append(today).append("</lastmod>\n");
            xml.append("    <changefreq>weekly</changefreq>\n");
            xml.append("    <priority>0.8</priority>\n");
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String loc, String priority) {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        xml.append("  <url>\n");
        xml.append("    <loc>").append(loc).append("</loc>\n");
        xml.append("    <lastmod>").append(today).append("</lastmod>\n");
        xml.append("    <changefreq>monthly</changefreq>\n");
        xml.append("    <priority>").append(priority).append("</priority>\n");
        xml.append("  </url>\n");
    }
}
