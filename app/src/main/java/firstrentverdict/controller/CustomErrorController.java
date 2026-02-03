package firstrentverdict.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int statusCode = 500;
        String message = "We're experiencing an internal glitch. Our team has been notified.";

        if (status != null) {
            statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                message = "The page you're looking for doesn't exist or has been moved to a new neighborhood.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                message = "You don't have permission to access this area.";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                message = "Something was wrong with the request. Try starting over from the home page.";
            }
        }

        model.addAttribute("status", statusCode);
        model.addAttribute("message", message);

        return "pages/error";
    }
}
