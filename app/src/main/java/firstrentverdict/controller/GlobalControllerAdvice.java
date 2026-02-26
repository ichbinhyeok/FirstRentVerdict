package firstrentverdict.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${app.base-url}")
    private String baseUrl;

    @ModelAttribute("baseUrl")
    public String addBaseUrlToModel() {
        return baseUrl;
    }
}
