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

    public VerdictController(
            VerdictDataRepository repository,
            VerdictService verdictService,
            WhatIfService whatIfService) {
        this.repository = repository;
        this.verdictService = verdictService;
        this.whatIfService = whatIfService;

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
                city, state, monthlyRent, availableCash, hasPet, isLocalMove, null);

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

        // Fetch Median Rent for Simulation Default
        var rentData = repository.getRent(city, state).orElse(null);
        int medianRent = (rentData != null) ? (int) rentData.median() : 2000;
        int cash = medianRent * 3 + 1500; // Standard buffer

        SimulationInput simInput = new SimulationInput(
                city, state, medianRent, cash, false, null, null,
                firstrentverdict.model.verdict.CreditTier.GOOD, true, null, null);

        VerdictResult result = verdictService.simulateVerdict(simInput);
        VerdictInput viewInput = new VerdictInput(city, state, medianRent, cash, false, true, null);

        model.addAttribute("input", viewInput);
        model.addAttribute("result", result);
        model.addAttribute("pageType", "GENERAL");
        model.addAttribute("scenarioTitle", "Rent Verdict: " + city + ", " + state);

        return "pages/result";
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

        var rentData = repository.getRent(city, state).orElse(null);
        int medianRent = (rentData != null) ? (int) rentData.median() : 2000;
        int cash = medianRent * 3 + 1500;

        firstrentverdict.model.verdict.CreditTier creditTier;
        try {
            creditTier = firstrentverdict.model.verdict.CreditTier.valueOf(tier.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid credit tier");
        }

        SimulationInput simInput = new SimulationInput(
                city, state, medianRent, cash, false, null, null,
                creditTier, true, null, null);

        VerdictResult result = verdictService.simulateVerdict(simInput);
        VerdictInput viewInput = new VerdictInput(city, state, medianRent, cash, false, true, null);

        model.addAttribute("input", viewInput);
        model.addAttribute("result", result);
        model.addAttribute("pageType", "CREDIT_" + creditTier.name());
        model.addAttribute("scenarioTitle",
                "Renting in " + city + " with " + tier.substring(0, 1).toUpperCase() + tier.substring(1) + " Credit");

        return "pages/result";
    }

    @GetMapping("/verdict/moving-to/{slug}")
    public String relocationPage(@PathVariable("slug") String slug, Model model) {
        String[] location = parseCitySlug(slug);
        String city = location[0];
        String state = location[1];

        if (!repository.isValidCity(city, state)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        }

        var rentData = repository.getRent(city, state).orElse(null);
        int medianRent = (rentData != null) ? (int) rentData.median() : 2000;
        int cash = medianRent * 4 + 2000; // Moving cross-country is expensive

        SimulationInput simInput = new SimulationInput(
                city, state, medianRent, cash, false, null, null,
                firstrentverdict.model.verdict.CreditTier.GOOD, false, null, null // isLocalMove = false
        );

        VerdictResult result = verdictService.simulateVerdict(simInput);
        VerdictInput viewInput = new VerdictInput(city, state, medianRent, cash, false, false, null);

        model.addAttribute("input", viewInput);
        model.addAttribute("result", result);
        model.addAttribute("pageType", "RELOCATION");
        model.addAttribute("scenarioTitle", "Relocation Guide: Moving to " + city + ", " + state);

        return "pages/result";
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
