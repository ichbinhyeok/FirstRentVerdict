package firstrentverdict.controller;

import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.repository.VerdictDataRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final VerdictDataRepository repository;

    public HomeController(VerdictDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Fetch cities for the dropdown menu in the calculator
        List<CitiesData.CityEntry> sortedCities = repository.getAllCities();
        sortedCities.sort(Comparator.comparing(CitiesData.CityEntry::city));

        model.addAttribute("cities", sortedCities);

        // Directly serve the calculator page instead of redirecting
        return "pages/index";
    }
}
