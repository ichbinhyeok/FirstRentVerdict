package firstrentverdict.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RobotsController {

    private final String baseUrl;

    public RobotsController(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @GetMapping(value = { "/robots.txt", "/RentVerdict/robots.txt" }, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots() {
        return String.join("\n",
                "User-agent: *",
                "Allow: /",
                "Sitemap: " + baseUrl + "/sitemap.xml");
    }
}
