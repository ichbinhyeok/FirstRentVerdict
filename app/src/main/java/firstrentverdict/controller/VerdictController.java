package firstrentverdict.controller;

import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.model.dtos.WhatIfRequest;
import firstrentverdict.model.dtos.WhatIfResponse;
import firstrentverdict.model.verdict.VerdictInput;
import firstrentverdict.model.verdict.VerdictResult;
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

    @GetMapping("/verdict")
    public String verdictRedirect() {
        return "redirect:/";
    }

    @PostMapping("/verdict")
    public String getVerdict(
            @RequestParam("cityState") String cityState, // Format: City|State
            @RequestParam("monthlyRent") int monthlyRent,
            @RequestParam("availableCash") int availableCash,
            @RequestParam(value = "hasPet", defaultValue = "false") boolean hasPet,
            @RequestParam(value = "isLocalMove", defaultValue = "false") boolean isLocalMove,
            Model model,
            HttpSession session) {
        String[] parts = cityState.split("\\|");
        String city = parts[0];
        String state = parts[1];

        VerdictInput input = new VerdictInput(
                city, state, monthlyRent, availableCash, hasPet, isLocalMove, null);

        VerdictResult result = verdictService.assessVerdict(input);

        // SAVE TO SESSION for what-if simulations
        session.setAttribute(SESSION_KEY_ORIGINAL_INPUT, input);
        session.setAttribute(SESSION_KEY_ORIGINAL_RESULT, result);

        model.addAttribute("result", result);

        return "pages/result";
    }

    @GetMapping("/verdict/{slug}")
    public String cityPage(@PathVariable("slug") String slug, Model model) {
        // Parse slug: e.g. "austin-tx" -> city="Austin", state="TX"
        if (slug == null || slug.length() < 3) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid city URL");
        }

        String state = slug.substring(slug.lastIndexOf('-') + 1).toUpperCase();
        String citySlug = slug.substring(0, slug.lastIndexOf('-'));
        // Capitalize words: "new-york" -> "New York"
        String city = java.util.Arrays.stream(citySlug.split("-"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(java.util.stream.Collectors.joining(" "));

        if (!repository.isValidCity(city, state)) {
            // Try fallback (maybe basic casing issue, though repository checks exact key
            // usually)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not supported: " + city + ", " + state);
        }

        firstrentverdict.service.seo.CityContentGenerator.CityPageContent content = cityContentGenerator.generate(
                city,
                state,
                repository.getRent(city, state).orElseThrow(),
                repository.getSecurityDeposit(city, state).orElse(null),
                repository.getMoving(city, state).orElse(null));

        model.addAttribute("pageData", content);
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
}
