package firstrentverdict.controller;

import firstrentverdict.content.GuideCatalog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/RentVerdict/guides")
public class GuideController {

    @GetMapping({ "", "/" })
    public String guidesHub(Model model) {
        model.addAttribute("guides", GuideCatalog.all());
        model.addAttribute("pageTitle", "Apartment Approval and Move-In Cash Guides (2026)");
        model.addAttribute("metaDescription",
                "Practical renter playbooks for bad credit, no cosigner, guarantor services, pet fees, and the real cash required to move in.");
        return "pages/guides_hub";
    }

    @GetMapping("/{slug}")
    public String guideArticle(@PathVariable String slug, Model model) {
        var guide = GuideCatalog.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));

        model.addAttribute("guide", guide);
        return "pages/guide_article";
    }
}
