package firstrentverdict.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class TrailingSlashRedirectFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_TRAILING_SLASH_PATHS = Set.of(
            "/",
            "/RentVerdict/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (uri != null
                && uri.length() > 1
                && uri.endsWith("/")
                && !ALLOWED_TRAILING_SLASH_PATHS.contains(uri)) {
            String targetPath = uri.substring(0, uri.length() - 1);
            String query = request.getQueryString();
            if (query != null && !query.isBlank()) {
                targetPath += "?" + query;
            }
            response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
            response.setHeader(HttpHeaders.LOCATION, targetPath);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
