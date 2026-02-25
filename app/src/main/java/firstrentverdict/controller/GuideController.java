package firstrentverdict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/RentVerdict/guides")
public class GuideController {

    private static final List<Map<String, String>> GUIDES = List.of(
            Map.of("slug", "no-credit-check-apartments", "title", "How to Find No Credit Check Apartments in 2026",
                    "excerpt",
                    "A step-by-step strategy for bypassing traditional credit checks, including private landlords, corporate co-signing services, and proof of liquidity."),
            Map.of("slug", "how-to-rent-with-eviction", "title",
                    "Renting with an Eviction Record: The Corporate Audit Survival Guide", "excerpt",
                    "Evictions stay on public records for 7 years. Learn the specific tactics to negotiate lease terms, offer higher deposits, and find second-chance properties."),
            Map.of("slug", "first-time-renter-budget", "title",
                    "The First-Time Renter's Budgeting Checklist (Hidden Costs)", "excerpt",
                    "Moving costs aren't just first and security. Discover the average utility deposits, application fees, and moving supply costs that catch new renters off guard."));

    @GetMapping({ "", "/" })
    public String guidesHub(Model model) {
        model.addAttribute("guides", GUIDES);
        model.addAttribute("pageTitle", "Renter Strategy Guides & Hub");
        model.addAttribute("metaDescription",
                "Expert guides on renting with no credit, eviction history, and first-time budgeting strategies.");
        return "pages/guides_hub";
    }

    @GetMapping("/{slug}")
    public String guideArticle(@PathVariable String slug, Model model) {
        var guide = GUIDES.stream()
                .filter(g -> g.get("slug").equals(slug))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));

        model.addAttribute("guide", guide);
        return "pages/guide_article";
    }
}
