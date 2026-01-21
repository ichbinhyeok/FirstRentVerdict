package firstrentverdict.controller;

import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.model.verdict.VerdictInput;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.core.VerdictService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class VerdictController {

    private final VerdictDataRepository repository;
    private final VerdictService verdictService;

    // Direct access to internal map for cities list is awkward,
    // ideally repository should expose a list method.
    // For MVP, we will assume we can get it via a public access point or Reflection
    // isn't needed if we add a method.
    // Let's assume we modify Repository or just load CitiesData again or simpler:
    // Actually, Repository has `validCities`. We need a getter for values.

    // Quick fix: Since I can't modify Repository interface easily in this turn
    // without valid file view again,
    // I made `validCities` private.
    // Correct approach: I will add `getAllCities()` to Repository first.

    public VerdictController(VerdictDataRepository repository, VerdictService verdictService) {
        this.repository = repository;
        this.verdictService = verdictService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // We need a way to get all cities.
        // Since I haven't added `getAllCities` to repo yet, I will do it in the next
        // tool call.
        // For now, let's write the controller logic assuming the repository has it.
        List<CitiesData.CityEntry> sortedCities = repository.getAllCities();
        sortedCities.sort(Comparator.comparing(CitiesData.CityEntry::city));

        model.addAttribute("cities", sortedCities);
        return "pages/index";
    }

    @PostMapping("/verdict")
    public String getVerdict(
            @RequestParam String cityState, // Format: City|State
            @RequestParam int monthlyRent,
            @RequestParam int availableCash,
            @RequestParam(defaultValue = "false") boolean hasPet,
            @RequestParam(defaultValue = "false") boolean isLocalMove,
            Model model) {
        String[] parts = cityState.split("\\|");
        String city = parts[0];
        String state = parts[1];

        VerdictInput input = new VerdictInput(
                city, state, monthlyRent, availableCash, hasPet, isLocalMove, null);

        VerdictResult result = verdictService.assessVerdict(input);
        model.addAttribute("result", result);

        return "pages/result";
    }
}
