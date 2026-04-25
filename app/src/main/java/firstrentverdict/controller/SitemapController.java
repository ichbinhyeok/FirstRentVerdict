package firstrentverdict.controller;

import firstrentverdict.content.GuideCatalog;
import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.repository.VerdictDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class SitemapController {

    private final VerdictDataRepository repository;
    private final String baseUrl;
    private final boolean includeCreditGood;
    private final boolean includeRelocationPairs;
    private final int movingPairOriginsPerCity;
    private final List<Integer> sitemapSavingsAmounts;
    private final int cityLimit;
    private final List<String> priorityCitySlugs;

    public SitemapController(VerdictDataRepository repository,
            @Value("${app.base-url}") String baseUrl,
            @Value("${seo.sitemap.city-limit:30}") int cityLimit,
            @Value("${seo.sitemap.priority-city-slugs:}") String priorityCitySlugsCsv,
            @Value("${seo.sitemap.include-credit-good:false}") boolean includeCreditGood,
            @Value("${seo.sitemap.include-relocation-pairs:false}") boolean includeRelocationPairs,
            @Value("${seo.sitemap.moving-pair-origins-per-city:3}") int movingPairOriginsPerCity,
            @Value("${seo.sitemap.savings-amounts:5000}") String sitemapSavingsAmountsCsv) {
        this.repository = repository;
        this.baseUrl = baseUrl;
        this.cityLimit = Math.max(1, cityLimit);
        this.priorityCitySlugs = parseStringCsv(priorityCitySlugsCsv);
        this.includeCreditGood = includeCreditGood;
        this.includeRelocationPairs = includeRelocationPairs;
        this.movingPairOriginsPerCity = Math.max(0, movingPairOriginsPerCity);
        this.sitemapSavingsAmounts = parseIntegerCsv(sitemapSavingsAmountsCsv);
    }

    @GetMapping(value = { "/sitemap.xml", "/RentVerdict/sitemap.xml" }, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        List<CitiesData.CityEntry> cities = selectSitemapCities(repository.getAllCities());
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        String monthlyMod = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);

        // 1. Static Pages
        addUrl(xml, baseUrl + "/RentVerdict/", "1.0", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/should-i-apply", "1.0", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/application-fee-risk-checker", "0.9", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/move-in-cash-gap-calculator", "0.9", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/security-deposit-calculator", "0.8", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/pet-fee-move-in-cost-calculator", "0.8", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/cities", "0.9", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/about", "0.6", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/methodology", "0.7", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/data-sources", "0.6", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/corrections", "0.5", monthlyMod);

        // 1b. Guides
        addUrl(xml, baseUrl + "/RentVerdict/guides", "0.9", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/guide/rent-affordability-rule", "0.7", monthlyMod);
        addUrl(xml, baseUrl + "/RentVerdict/research/move-in-cost-index", "0.9", monthlyMod);
        for (GuideCatalog.GuideEntry guide : GuideCatalog.all()) {
            addUrl(xml, baseUrl + "/RentVerdict/guides/" + guide.slug(), "0.8", monthlyMod);
        }

        // 2. High-Intent pSEO Scenarios
        for (CitiesData.CityEntry city : cities) {
            String slug = toCanonicalSlug(city);
            String root = baseUrl + "/RentVerdict/verdict/";

            // Tier 1: Core Alignment (Target: ~2,600)
            // - City Landing & Relocation (200)
            addUrl(xml, root + slug, "0.8", monthlyMod);
            addUrl(xml, root + "moving-to/" + slug, "0.9", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/should-i-apply-in/" + slug, "0.9", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/city/" + slug + "/move-in-cost-calculator", "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/can-i-apply-with/5000/for/" + medianRentSafe(city) + "/in/" + slug, "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/can-i-apply-with/4000/for/" + medianRentSafe(city) + "/in/" + slug, "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/can-i-apply-with/6000/for/" + medianRentSafe(city) + "/in/" + slug, "0.8", monthlyMod);

            // Tool-state pages: fee, income, and deposit-stack families.
            addUrl(xml, baseUrl + "/RentVerdict/application-fee/75/in/" + slug, "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/application-fee/150/in/" + slug, "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/admin-fee/300/in/" + slug, "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/holding-deposit/" + medianRentSafe(city) + "/in/" + slug, "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/can-i-apply-with/" + Math.max(1, medianRentSafe(city) * 3) + "/income-for/" + medianRentSafe(city) + "/rent-in/" + slug, "0.8", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/can-i-apply-with/" + Math.max(1, (int) Math.round(medianRentSafe(city) * 2.5)) + "/income-for/" + medianRentSafe(city) + "/rent-in/" + slug, "0.7", monthlyMod);
            addUrl(xml, baseUrl + "/RentVerdict/first-last-security-deposit-in/" + slug, "0.8", monthlyMod);

            // - Credit (3 tiers x 100 cities = 300)
            addUrl(xml, root + "credit/poor/" + slug, "0.8", monthlyMod);
            addUrl(xml, root + "credit/fair/" + slug, "0.7", monthlyMod);
            if (includeCreditGood) {
                addUrl(xml, root + "credit/good/" + slug, "0.6", monthlyMod);
            }

            // - Pet (1 intent x 100 cities = 100)
            addUrl(xml, root + "with-pet/" + slug, "0.8", monthlyMod);

            // - Savings Based
            for (int savings : sitemapSavingsAmounts) {
                addUrl(xml, root + "can-i-move-with/" + savings + "/to/" + slug, "0.9", monthlyMod);
                addUrl(xml, baseUrl + "/RentVerdict/can-i-move-with/" + savings + "/to/" + slug, "0.8", monthlyMod);
            }

            // Placeholders removed from sitemap (Salary Needed, No Cosigner)

            // - Moving Pairs
            if (includeRelocationPairs && movingPairOriginsPerCity > 0) {
                selectOriginCities(cities, city, movingPairOriginsPerCity).forEach(from -> {
                    String fromSlug = toCanonicalSlug(from);
                    addUrl(xml,
                            baseUrl + "/RentVerdict/verdict/moving-from/" + fromSlug + "/to/" + slug,
                            "0.8", monthlyMod);
                });
            }

            // Placeholders removed from sitemap (Compare Pairs)
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private List<CitiesData.CityEntry> selectSitemapCities(List<CitiesData.CityEntry> allCities) {
        if (allCities == null || allCities.isEmpty()) {
            return List.of();
        }

        List<CitiesData.CityEntry> ranked = allCities.stream()
                .sorted(Comparator
                        .comparingInt(this::medianRentSafe).reversed()
                        .thenComparing(CitiesData.CityEntry::state)
                        .thenComparing(CitiesData.CityEntry::city))
                .toList();

        java.util.LinkedHashMap<String, CitiesData.CityEntry> selected = new java.util.LinkedHashMap<>();
        java.util.Map<String, CitiesData.CityEntry> bySlug = allCities.stream()
                .collect(Collectors.toMap(this::toCanonicalSlug, c -> c, (a, b) -> a));

        for (String slug : priorityCitySlugs) {
            CitiesData.CityEntry city = bySlug.get(slug);
            if (city != null) {
                selected.put(key(city), city);
            }
            if (selected.size() >= cityLimit) {
                break;
            }
        }

        for (CitiesData.CityEntry city : ranked) {
            if (selected.size() >= cityLimit) {
                break;
            }
            selected.putIfAbsent(key(city), city);
        }

        return new ArrayList<>(selected.values());
    }

    private int medianRentSafe(CitiesData.CityEntry city) {
        return repository.getRent(city.city(), city.state())
                .map(r -> r.median())
                .orElse(0);
    }

    private List<Integer> parseIntegerCsv(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of(5000);
        }
        List<Integer> values = java.util.Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> {
                    try {
                        return Integer.valueOf(s);
                    } catch (NumberFormatException ignored) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return values.isEmpty() ? List.of(5000) : values;
    }

    private List<String> parseStringCsv(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return java.util.Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .distinct()
                .toList();
    }

    private String key(CitiesData.CityEntry city) {
        return city.city().toLowerCase(Locale.ROOT) + "|" + city.state().toLowerCase(Locale.ROOT);
    }

    private String toCanonicalSlug(CitiesData.CityEntry city) {
        return city.city().toLowerCase().replace(" ", "-").replace(".", "") + "-" + city.state().toLowerCase();
    }

    private boolean isSameCity(CitiesData.CityEntry a, CitiesData.CityEntry b) {
        return a.city().equalsIgnoreCase(b.city()) && a.state().equalsIgnoreCase(b.state());
    }

    private List<CitiesData.CityEntry> selectOriginCities(
            List<CitiesData.CityEntry> allCities,
            CitiesData.CityEntry destination,
            int limit) {

        List<CitiesData.CityEntry> candidates = allCities.stream()
                .filter(c -> !isSameCity(c, destination))
                .toList();

        if (candidates.isEmpty()) {
            return List.of();
        }

        int selectionSize = Math.min(limit, candidates.size());
        List<CitiesData.CityEntry> selected = new ArrayList<>(selectionSize);
        java.util.Set<String> seen = new java.util.LinkedHashSet<>();

        // Keep some same-state corridors, but reserve most slots for inbound IRS migration states.
        int sameStateCap = Math.min(3, selectionSize);
        addCandidatesForState(candidates, destination.state(), selected, seen, sameStateCap);

        List<String> preferredStates = new ArrayList<>();

        repository.getStateMigrationFlow(destination.state()).ifPresent(flow -> {
            if (flow.topOrigins() != null) {
                flow.topOrigins().stream()
                        .map(origin -> origin.fromState() != null ? origin.fromState().toUpperCase() : null)
                        .filter(code -> code != null && !code.equalsIgnoreCase(destination.state()))
                        .forEach(preferredStates::add);
            }
        });

        // Keep order stable while removing duplicates.
        List<String> orderedStates = new ArrayList<>(new java.util.LinkedHashSet<>(preferredStates));

        for (String stateCode : orderedStates) {
            if (selected.size() >= selectionSize) {
                break;
            }
            addCandidatesForState(candidates, stateCode, selected, seen, selectionSize);
        }

        if (selected.size() < selectionSize) {
            List<CitiesData.CityEntry> remaining = candidates.stream()
                    .filter(c -> seen.add(c.city().toLowerCase() + "|" + c.state().toLowerCase()))
                    .toList();
            if (!remaining.isEmpty()) {
                int start = Math.floorMod((destination.city() + "|" + destination.state()).hashCode(), remaining.size());
                for (int i = 0; i < remaining.size() && selected.size() < selectionSize; i++) {
                    selected.add(remaining.get((start + i) % remaining.size()));
                }
            }
        }

        return selected;
    }

    private void addCandidatesForState(
            List<CitiesData.CityEntry> candidates,
            String stateCode,
            List<CitiesData.CityEntry> selected,
            java.util.Set<String> seen,
            int targetSize) {
        candidates.stream()
                .filter(c -> c.state().equalsIgnoreCase(stateCode))
                .forEach(c -> {
                    if (selected.size() < targetSize) {
                        String key = c.city().toLowerCase() + "|" + c.state().toLowerCase();
                        if (seen.add(key)) {
                            selected.add(c);
                        }
                    }
                });
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
