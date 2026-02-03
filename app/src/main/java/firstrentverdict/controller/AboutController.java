package firstrentverdict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
@org.springframework.web.bind.annotation.RequestMapping("/RentVerdict")
public class AboutController {

    @GetMapping("/about")
    public String about(Model model) {
        return "pages/about";
    }

    @GetMapping("/methodology")
    public String methodology(Model model) {
        return "pages/methodology";
    }

    @GetMapping("/guide/rent-affordability-rule")
    public String anchorGuide(Model model) {
        return "pages/guide_rent_rule";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        return "pages/privacy";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        return "pages/terms";
    }
}
