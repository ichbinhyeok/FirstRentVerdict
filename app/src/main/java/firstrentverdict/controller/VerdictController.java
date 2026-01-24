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
