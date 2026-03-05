package firstrentverdict.controller;

import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.model.dtos.WhatIfRequest;
import firstrentverdict.model.dtos.WhatIfResponse;
import firstrentverdict.model.verdict.VerdictInput;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.model.verdict.SimulationInput;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.core.VerdictService;
import firstrentverdict.service.whatif.WhatIfService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/RentVerdict")
public class VerdictController {

    public static final String SESSION_KEY_ORIGINAL_INPUT = "originalVerdictInput";
    public static final String SESSION_KEY_ORIGINAL_RESULT = "originalVerdictResult";

    private final VerdictDataRepository repository;
    private final VerdictService verdictService;
    private final WhatIfService whatIfService;
    private final firstrentverdict.service.seo.CityContentGenerator cityContentGenerator;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${seo.indexing.noindex.relocation-pairs:true}")
    private boolean noindexRelocationPairs;

    @Value("${seo.indexing.noindex.savings-amounts:3000,10000}")
    private String noindexSavingsAmountsCsv;

    private List<String> topOriginStates(String destinationState, int limit) {
        return repository.getStateMigrationFlow(destinationState)
                .map(flow -> flow.topOrigins() == null ? List.<String>of()
                        : flow.topOrigins().stream()
                                .map(origin -> origin.fromState())
                                .filter(state -> state != null && !state.isBlank())
                                .filter(state -> !state.equalsIgnoreCase(destinationState))
                                .limit(limit)
                                .toList())
                .orElse(List.of());
    }

    private Set<Integer> parseIntegerCsvToSet(String csv) {
        if (csv == null || csv.isBlank()) {
            return Set.of();
        }
        return java.util.Arrays.stream(csv.split(","))
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
                .collect(Collectors.toSet());
    }

    public VerdictController(
            VerdictDataRepository repository,
            VerdictService verdictService,
            WhatIfService whatIfService,
            firstrentverdict.service.seo.CityContentGenerator cityContentGenerator) {
        this.repository = repository;
        this.verdictService = verdictService;
        this.whatIfService = whatIfService;
        this.cityContentGenerator = cityContentGenerator;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<CitiesData.CityEntry> sortedCities = repository.getAllCities();
        sortedCities.sort(Comparator.comparing(CitiesData.CityEntry::city));

        model.addAttribute("cities", sortedCities);
        return "pages/index";
    }

    @GetMapping("")
    public org.springframework.web.servlet.view.RedirectView canonicalRoot() {
        org.springframework.web.servlet.view.RedirectView rv = new org.springframework.web.servlet.view.RedirectView(
                "/RentVerdict/");
        rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        rv.setExposeModelAttributes(false);
        return rv;
    }

    @RequestMapping(value = "/verdict", method = { RequestMethod.GET, RequestMethod.POST })
    public String getVerdict(
            @RequestParam(value = "cityState", required = false) String cityState, // Format: City|State
            @RequestParam(value = "monthlyRent", required = false) Integer monthlyRent,
            @RequestParam(value = "availableCash", required = false) Integer availableCash,
            @RequestParam(value = "hasPet", defaultValue = "false") boolean hasPet,
            @RequestParam(value = "isLocalMove", defaultValue = "true") boolean isLocalMove,
            Model model,
            HttpSession session) {

        // Validating Inputs: If core data is missing, redirect to home (Handle empty
        // GET access)
        if (cityState == null || monthlyRent == null || availableCash == null) {
            return "redirect:/";
        }

        String[] parts = cityState.split("\\|");
        if (parts.length < 2) {
            return "redirect:/";
        }
        String city = parts[0];
        String state = parts[1];

        validateFormVerdictInput(city, state, monthlyRent, availableCash);

        VerdictInput input = new VerdictInput(
                city, state, monthlyRent, availableCash, hasPet, isLocalMove,
                firstrentverdict.model.verdict.CreditTier.GOOD, null);

        VerdictResult result = verdictService.assessVerdict(input);

        // SAVE TO SESSION for what-if simulations
        session.setAttribute(SESSION_KEY_ORIGINAL_INPUT, input);
        session.setAttribute(SESSION_KEY_ORIGINAL_RESULT, result);

        model.addAttribute("input", input);
        model.addAttribute("result", result);
        model.addAttribute("noindex", true);

        return "pages/result";
    }

    private void validateFormVerdictInput(String city, String state, int monthlyRent, int availableCash) {
        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported city: " + city + ", " + state);
        }
        if (monthlyRent < 1 || monthlyRent > 30000 || availableCash < 0 || availableCash > 100000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid form parameters");
        }
    }

    private String toCanonicalSlug(String city, String state) {
        return city.toLowerCase().replace(" ", "-").replace(".", "") + "-" + state.toLowerCase();
    }

    private org.springframework.web.servlet.ModelAndView permanentRedirect(String path) {
        org.springframework.web.servlet.view.RedirectView rv = new org.springframework.web.servlet.view.RedirectView(path);
        rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        rv.setExposeModelAttributes(false);
        return new org.springframework.web.servlet.ModelAndView(rv);
    }

    private record CityResolution(String city, String state, String canonicalSlug) {
    }

    private CityResolution resolveCityOr404(String slug) {
        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found in database");
        }

        return new CityResolution(city, state, toCanonicalSlug(city, state));
    }

    private String[] parseCitySlug(String slug) {
        if (slug == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid URL format");
        }

        var cityEntry = repository.getCityBySlug(slug).orElse(null);
        if (cityEntry != null) {
            return new String[] { cityEntry.city(), cityEntry.state() };
        }

        if (slug.lastIndexOf('-') == -1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid URL format");
        }
        String state = slug.substring(slug.lastIndexOf('-') + 1).toUpperCase();
        String cityPart = slug.substring(0, slug.lastIndexOf('-'));

        // Capitalize: new-york -> New York
        String city = java.util.Arrays.stream(cityPart.split("-"))
                .map(s -> s.length() > 0 ? s.substring(0, 1).toUpperCase() + s.substring(1) : "")
                .collect(java.util.stream.Collectors.joining(" "));

        return new String[] { city, state };
    }

    @GetMapping("/verdict/{slug}")
    public Object cityPage(@PathVariable("slug") String slug, Model model) {
        CityResolution cityResolution = resolveCityOr404(slug);
        String city = cityResolution.city();
        String state = cityResolution.state();

        if (!slug.equals(cityResolution.canonicalSlug())) {
            return permanentRedirect("/RentVerdict/verdict/" + cityResolution.canonicalSlug());
        }

        // Fetch Data for SEO Landing
        var rentData = repository.getRent(city, state).orElse(null);
        if (rentData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent data not found");
        }

        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);
        var economicFact = repository.getCityEconomicFact(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, economicFact, firstrentverdict.service.seo.CityContentGenerator.Intent.GENERAL, null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();

        model.addAttribute("pageData", pageContent);
        model.addAttribute("relatedCities", relatedCities);
        model.addAttribute("canonicalUrl", baseUrl + "/RentVerdict/verdict/" + cityResolution.canonicalSlug());

        return "pages/city_landing";
    }

    @GetMapping("/verdict/credit/{tier:poor|fair}/{slug}")
    public Object creditPage(
            @PathVariable("tier") String tier,
            @PathVariable("slug") String slug,
            Model model) {

        CityResolution cityResolution = resolveCityOr404(slug);
        String city = cityResolution.city();
        String state = cityResolution.state();

        if (!slug.equals(cityResolution.canonicalSlug())) {
            return permanentRedirect(
                    "/RentVerdict/verdict/credit/" + tier.toLowerCase() + "/" + cityResolution.canonicalSlug());
        }

        // Fetch Data for SEO Landing
        var rentData = repository.getRent(city, state).orElse(null);
        if (rentData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent data not found");
        }
        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);
        var economicFact = repository.getCityEconomicFact(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, economicFact,
                tier.equalsIgnoreCase("poor") ? firstrentverdict.service.seo.CityContentGenerator.Intent.CREDIT_POOR
                        : firstrentverdict.service.seo.CityContentGenerator.Intent.CREDIT_FAIR,
                null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();

        model.addAttribute("pageData", pageContent);
        model.addAttribute("relatedCities", relatedCities);
        model.addAttribute("tier", Character.toUpperCase(tier.charAt(0)) + tier.substring(1).toLowerCase());
        model.addAttribute("canonicalUrl",
                baseUrl + "/RentVerdict/verdict/credit/" + tier.toLowerCase() + "/" + cityResolution.canonicalSlug());

        return "pages/credit_landing";
    }

    @GetMapping("/verdict/credit/good/{slug}")
    public Object creditGoodPage(
            @PathVariable("slug") String slug,
            Model model) {

        CityResolution cityResolution = resolveCityOr404(slug);
        String city = cityResolution.city();
        String state = cityResolution.state();

        if (!slug.equals(cityResolution.canonicalSlug())) {
            return permanentRedirect("/RentVerdict/verdict/credit/good/" + cityResolution.canonicalSlug());
        }

        // Fetch Data for SEO Landing
        var rentData = repository.getRent(city, state).orElse(null);
        if (rentData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent data not found");
        }

        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);
        var economicFact = repository.getCityEconomicFact(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, economicFact, firstrentverdict.service.seo.CityContentGenerator.Intent.CREDIT_GOOD, null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();

        model.addAttribute("pageData", pageContent);
        model.addAttribute("relatedCities", relatedCities);
        model.addAttribute("tier", "Good");
        model.addAttribute("canonicalUrl", baseUrl + "/RentVerdict/verdict/credit/good/" + cityResolution.canonicalSlug());

        return "pages/credit_landing";
    }

    @GetMapping("/verdict/moving-to/{slug}")
    public Object relocationPage(@PathVariable("slug") String slug, Model model) {
        CityResolution cityResolution = resolveCityOr404(slug);
        String city = cityResolution.city();
        String state = cityResolution.state();

        if (!slug.equals(cityResolution.canonicalSlug())) {
            return permanentRedirect("/RentVerdict/verdict/moving-to/" + cityResolution.canonicalSlug());
        }

        // Fetch Data for SEO Landing
        var rentData = repository.getRent(city, state).orElse(null);
        if (rentData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent data not found");
        }

        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);
        var economicFact = repository.getCityEconomicFact(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, economicFact, firstrentverdict.service.seo.CityContentGenerator.Intent.RELOCATION, null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();

        model.addAttribute("pageData", pageContent);
        model.addAttribute("relatedCities", relatedCities);
        model.addAttribute("canonicalUrl", baseUrl + "/RentVerdict/verdict/moving-to/" + cityResolution.canonicalSlug());
        model.addAttribute("topOriginStates", topOriginStates(state, 6));

        return "pages/relocation_landing";
    }

    @GetMapping("/verdict/with-pet/{slug}")
    public Object petPage(@PathVariable("slug") String slug, Model model) {
        CityResolution cityResolution = resolveCityOr404(slug);
        String city = cityResolution.city();
        String state = cityResolution.state();

        if (!slug.equals(cityResolution.canonicalSlug())) {
            return permanentRedirect("/RentVerdict/verdict/with-pet/" + cityResolution.canonicalSlug());
        }

        var rentData = repository.getRent(city, state).orElse(null);
        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);
        var economicFact = repository.getCityEconomicFact(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, economicFact, firstrentverdict.service.seo.CityContentGenerator.Intent.PET_FRIENDLY, null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();
        model.addAttribute("relatedCities", relatedCities);

        model.addAttribute("pageData", pageContent);
        model.addAttribute("canonicalUrl", baseUrl + "/RentVerdict/verdict/with-pet/" + cityResolution.canonicalSlug());
        return "pages/landing_pet";
    }

    @GetMapping("/verdict/pet-friendly-apartments/{slug}")
    public org.springframework.web.servlet.view.RedirectView petRedirect(@PathVariable("slug") String slug) {
        CityResolution cityResolution = resolveCityOr404(slug);
        org.springframework.web.servlet.view.RedirectView rv = new org.springframework.web.servlet.view.RedirectView(
                "/RentVerdict/verdict/with-pet/" + cityResolution.canonicalSlug());
        rv.setStatusCode(org.springframework.http.HttpStatus.MOVED_PERMANENTLY);
        rv.setExposeModelAttributes(false);
        return rv;
    }

    @GetMapping("/verdict/can-i-move-with/{amount}/to/{slug}")
    public Object savingsPage(
            @PathVariable("amount") int amount,
            @PathVariable("slug") String slug,
            Model model) {

        List<Integer> ALLOWED_SAVINGS = List.of(3000, 5000, 10000);
        if (!ALLOWED_SAVINGS.contains(amount)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Amount must be 3000, 5000, or 10000");
        }

        CityResolution cityResolution = resolveCityOr404(slug);
        String city = cityResolution.city();
        String state = cityResolution.state();

        if (!slug.equals(cityResolution.canonicalSlug())) {
            return permanentRedirect(
                    "/RentVerdict/verdict/can-i-move-with/" + amount + "/to/" + cityResolution.canonicalSlug());
        }

        var rentData = repository.getRent(city, state).orElse(null);
        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);
        var economicFact = repository.getCityEconomicFact(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, economicFact, firstrentverdict.service.seo.CityContentGenerator.Intent.SAVINGS_BASED, amount);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();
        model.addAttribute("relatedCities", relatedCities);

        model.addAttribute("pageData", pageContent);
        model.addAttribute("savings", amount);
        model.addAttribute("noindex", parseIntegerCsvToSet(noindexSavingsAmountsCsv).contains(amount));
        model.addAttribute("canonicalUrl",
                baseUrl + "/RentVerdict/verdict/can-i-move-with/" + amount + "/to/" + cityResolution.canonicalSlug());
        return "pages/landing_savings";
    }

    @GetMapping("/verdict/moving-from/{from}/to/{to}")
    public Object relocationPairPage(
            @PathVariable("from") String from,
            @PathVariable("to") String to,
            Model model) {

        CityResolution fromResolution = resolveCityOr404(from);
        CityResolution toResolution = resolveCityOr404(to);

        if (!from.equals(fromResolution.canonicalSlug()) || !to.equals(toResolution.canonicalSlug())) {
            return permanentRedirect(
                    "/RentVerdict/verdict/moving-from/" + fromResolution.canonicalSlug() + "/to/"
                            + toResolution.canonicalSlug());
        }

        String city = toResolution.city();
        String state = toResolution.state();

        var rentData = repository.getRent(city, state).orElse(null);
        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);
        var economicFact = repository.getCityEconomicFact(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, economicFact, firstrentverdict.service.seo.CityContentGenerator.Intent.RELOCATION_PAIR,
                fromResolution.canonicalSlug());

        model.addAttribute("pageData", pageContent);
        model.addAttribute("canonicalUrl",
                baseUrl + "/RentVerdict/verdict/moving-from/" + fromResolution.canonicalSlug() + "/to/"
                        + toResolution.canonicalSlug());
        model.addAttribute("fromState", fromResolution.state());
        model.addAttribute("topOriginStates", topOriginStates(state, 6));
        model.addAttribute("noindex", noindexRelocationPairs);
        return "pages/city_landing";
    }

    @GetMapping("/first-month-cost/{rent}/{state}")
    public String firstMonthCostPage(
            @PathVariable("rent") int rent,
            @PathVariable("state") String state,
            Model model) {

        throw new ResponseStatusException(HttpStatus.GONE, "This content has been permanently removed.");
    }

    @GetMapping("/verdict/salary-needed/{slug}")
    public String salaryNeededPage(@PathVariable("slug") String slug) {
        throw new ResponseStatusException(HttpStatus.GONE, "This placeholder content has been permanently removed.");
    }

    @GetMapping("/verdict/no-cosigner/{slug}")
    public String noCosignerPage(@PathVariable("slug") String slug) {
        throw new ResponseStatusException(HttpStatus.GONE, "This placeholder content has been permanently removed.");
    }

    @GetMapping("/verdict/compare/{compareSlug}")
    public String comparePage(@PathVariable("compareSlug") String compareSlug) {
        throw new ResponseStatusException(HttpStatus.GONE, "This placeholder content has been permanently removed.");
    }

    @GetMapping("/cities")
    public String allCities(Model model) {
        var allCities = repository.getAllCities();

        // Group by State
        java.util.Map<String, java.util.List<firstrentverdict.model.dtos.CitiesData.CityEntry>> citiesByState = new java.util.TreeMap<>(
                allCities.stream()
                        .collect(java.util.stream.Collectors
                                .groupingBy(firstrentverdict.model.dtos.CitiesData.CityEntry::state)));

        // Sort cities within each state
        citiesByState.forEach((state, list) -> list
                .sort(java.util.Comparator.comparing(firstrentverdict.model.dtos.CitiesData.CityEntry::city)));

        model.addAttribute("citiesByState", citiesByState);
        return "pages/locations";
    }

    /**
     * API endpoint for "What-if" simulations.
     * Re-calculates verdict based on user adjustments to rent or cash.
     */
    @PostMapping("/what-if")
    @ResponseBody
    public WhatIfResponse whatIfSimulation(
            @RequestBody WhatIfRequest whatIf,
            HttpSession session) {
        try {
            VerdictInput originalInput = (VerdictInput) session.getAttribute(SESSION_KEY_ORIGINAL_INPUT);
            VerdictResult originalResult = (VerdictResult) session.getAttribute(SESSION_KEY_ORIGINAL_RESULT);

            if (originalInput == null || originalResult == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No original verdict found in session. Please submit an initial verdict first.");
            }

            VerdictResult newResult = whatIfService.recalculate(originalInput, whatIf);

            String prevBottleneck = originalResult.primaryBottleneck();
            String currBottleneck = newResult.primaryBottleneck();

            return new WhatIfResponse(
                    newResult,
                    prevBottleneck,
                    currBottleneck,
                    !prevBottleneck.equals(currBottleneck) || !originalResult.verdict().equals(newResult.verdict()));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    private void validateSimulationInput(SimulationInput input) {
        if (input == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulation payload is required");
        }
        if (input.monthlyRent() < 1 || input.monthlyRent() > 30000 || input.availableCash() < 0
                || input.availableCash() > 100000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid simulation parameters");
        }
        if (input.city() == null || input.city().isBlank() || input.state() == null || input.state().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "City and state are required");
        }
        if (input.creditTier() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "creditTier is required");
        }
        if (!repository.isValidCity(input.city(), input.state())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported city: " + input.city() + ", " + input.state());
        }

        if (!input.isLocalMove()) {
            boolean hasFromCity = input.fromCity() != null && !input.fromCity().isBlank();
            boolean hasFromState = input.fromState() != null && !input.fromState().isBlank();
            if (!hasFromCity && !hasFromState) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "fromCity and fromState are required for long-distance simulations");
            }
            if (hasFromCity != hasFromState) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Both fromCity and fromState are required for long-distance simulations");
            }
            if (hasFromCity && !repository.isValidCity(input.fromCity(), input.fromState())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unsupported origin city: " + input.fromCity() + ", " + input.fromState());
            }
        }
    }

    @PostMapping("/api/simulate")
    @ResponseBody
    public VerdictResult simulateVerdict(@RequestBody SimulationInput input) {
        validateSimulationInput(input);
        try {
            return verdictService.simulateVerdict(input);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
