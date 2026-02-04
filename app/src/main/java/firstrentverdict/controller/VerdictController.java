package firstrentverdict.controller;

import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.model.dtos.RentData;
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

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/RentVerdict")
public class VerdictController {

    public static final String SESSION_KEY_ORIGINAL_INPUT = "originalVerdictInput";
    public static final String SESSION_KEY_ORIGINAL_RESULT = "originalVerdictResult";

    private final VerdictDataRepository repository;
    private final VerdictService verdictService;
    private final WhatIfService whatIfService;
    private final firstrentverdict.service.seo.CityContentGenerator cityContentGenerator;

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

    private String[] parseCitySlug(String slug) {
        if (slug == null || slug.lastIndexOf('-') == -1) {
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
    public String cityPage(@PathVariable("slug") String slug, Model model) {
        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found in database");
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

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, firstrentverdict.service.seo.CityContentGenerator.Intent.GENERAL, null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();

        model.addAttribute("pageData", pageContent);
        model.addAttribute("relatedCities", relatedCities);
        model.addAttribute("canonicalUrl", "https://movecostinfo.com/RentVerdict/verdict/" + slug);

        return "pages/city_landing";
    }

    @GetMapping("/verdict/credit/{tier}/{slug}")
    public String creditPage(
            @PathVariable("tier") String tier,
            @PathVariable("slug") String slug,
            Model model) {

        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
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

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight,
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
                "https://movecostinfo.com/RentVerdict/verdict/credit/" + tier.toLowerCase() + "/" + slug);

        return "pages/credit_landing";
    }

    @GetMapping("/verdict/credit/good/{slug}")
    public String creditGoodPage(
            @PathVariable("slug") String slug,
            Model model) {

        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
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

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, firstrentverdict.service.seo.CityContentGenerator.Intent.CREDIT_GOOD, null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();

        model.addAttribute("pageData", pageContent);
        model.addAttribute("relatedCities", relatedCities);
        model.addAttribute("tier", "Good");
        model.addAttribute("canonicalUrl", "https://movecostinfo.com/RentVerdict/verdict/credit/good/" + slug);

        return "pages/credit_landing";
    }

    @GetMapping("/verdict/moving-to/{slug}")
    public String relocationPage(@PathVariable("slug") String slug, Model model) {
        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
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

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, firstrentverdict.service.seo.CityContentGenerator.Intent.RELOCATION, null);

        // Get related cities in same state
        List<CitiesData.CityEntry> relatedCities = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state) && !c.city().equalsIgnoreCase(city))
                .limit(5)
                .toList();

        model.addAttribute("pageData", pageContent);
        model.addAttribute("relatedCities", relatedCities);
        model.addAttribute("canonicalUrl", "https://movecostinfo.com/RentVerdict/verdict/moving-to/" + slug);

        return "pages/relocation_landing";
    }

    @GetMapping({ "/verdict/with-pet/{slug}", "/verdict/pet-friendly-apartments/{slug}" })
    public String petPage(@PathVariable("slug") String slug, Model model) {
        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        }

        var rentData = repository.getRent(city, state).orElse(null);
        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, firstrentverdict.service.seo.CityContentGenerator.Intent.PET_FRIENDLY, null);

        model.addAttribute("pageData", pageContent);
        model.addAttribute("canonicalUrl", "https://movecostinfo.com/RentVerdict/verdict/with-pet/" + slug);
        return "pages/city_landing";
    }

    @GetMapping("/verdict/can-i-move-with/{amount}/to/{slug}")
    public String savingsPage(
            @PathVariable("amount") int amount,
            @PathVariable("slug") String slug,
            Model model) {
        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        }

        var rentData = repository.getRent(city, state).orElse(null);
        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, firstrentverdict.service.seo.CityContentGenerator.Intent.SAVINGS_BASED, amount);

        model.addAttribute("pageData", pageContent);
        model.addAttribute("savings", amount);
        model.addAttribute("canonicalUrl",
                "https://movecostinfo.com/RentVerdict/verdict/can-i-move-with/" + amount + "/to/" + slug);
        return "pages/city_landing";
    }

    @GetMapping("/verdict/moving-from/{from}/to/{to}")
    public String relocationPairPage(
            @PathVariable("from") String from,
            @PathVariable("to") String to,
            Model model) {

        String[] location = parseCitySlug(to);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination city not found");
        }

        var rentData = repository.getRent(city, state).orElse(null);
        var depositData = repository.getSecurityDeposit(city, state).orElse(null);
        var movingData = repository.getMoving(city, state).orElse(null);
        var petData = repository.getPet(city, state).orElse(null);
        var insight = repository.getCityInsight(city, state).orElse(null);

        // Readable 'From' city name
        String readableFrom = java.util.Arrays.stream(from.split("-"))
                .map(s -> s.length() > 0 ? s.substring(0, 1).toUpperCase() + s.substring(1) : "")
                .collect(java.util.stream.Collectors.joining(" "));

        var pageContent = cityContentGenerator.generate(city, state, rentData, depositData, movingData, petData,
                insight, firstrentverdict.service.seo.CityContentGenerator.Intent.RELOCATION_PAIR, readableFrom);

        model.addAttribute("pageData", pageContent);
        model.addAttribute("canonicalUrl",
                "https://movecostinfo.com/RentVerdict/verdict/moving-from/" + from + "/to/" + to);
        return "pages/city_landing";
    }

    @GetMapping("/first-month-cost/{rent}/{state}")
    public String firstMonthCostPage(
            @PathVariable("rent") int rent,
            @PathVariable("state") String state,
            Model model) {

        // Find a representative city in this state or use a generic "State Average"
        String repCity = repository.getAllCities().stream()
                .filter(c -> c.state().equalsIgnoreCase(state))
                .findFirst()
                .map(c -> c.city())
                .orElse("Local");

        var depositData = repository.getSecurityDeposit(repCity, state).orElse(null);
        var movingData = repository.getMoving(repCity, state).orElse(null);
        var petData = repository.getPet(repCity, state).orElse(null);
        var insight = repository.getCityInsight(repCity, state).orElse(null);

        // Dummy rent data to satisfy generator
        RentData.CityRent dummyRent = new RentData.CityRent(repCity, state.toUpperCase(), 2026, rent,
                (int) (rent * 0.8), (int) (rent * 1.2), null, null, false);

        var pageContent = cityContentGenerator.generate(repCity, state.toUpperCase(), dummyRent, depositData,
                movingData, petData, insight,
                firstrentverdict.service.seo.CityContentGenerator.Intent.RENT_STATE, rent);

        model.addAttribute("pageData", pageContent);
        model.addAttribute("canonicalUrl",
                "https://movecostinfo.com/RentVerdict/first-month-cost/" + rent + "/" + state.toLowerCase());
        return "pages/city_landing";
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
    }

    @PostMapping("/api/simulate")
    @ResponseBody
    public VerdictResult simulateVerdict(@RequestBody SimulationInput input) {
        return verdictService.simulateVerdict(input);
    }
}
