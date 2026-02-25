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
    public String home() {
        // Redirect to canonical path to prevent Duplicate Content SEO penalty
        return "redirect:/RentVerdict/";
    }
}
