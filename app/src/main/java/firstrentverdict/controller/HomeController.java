package firstrentverdict.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public org.springframework.web.servlet.view.RedirectView home() {
        org.springframework.web.servlet.view.RedirectView rv = new org.springframework.web.servlet.view.RedirectView(
                "/RentVerdict/");
        rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        rv.setExposeModelAttributes(false);
        return rv;
    }
}
