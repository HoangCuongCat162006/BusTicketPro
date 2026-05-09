package org.example.busticketpro.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("=== LOGIN SUCCESS DEBUG ===");
        System.out.println("Username: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        // ADMIN
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("→ Redirecting to ADMIN dashboard");
            response.sendRedirect("/admin");
            return;
        }

        // STAFF
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
            System.out.println("→ Redirecting to STAFF dashboard");
            response.sendRedirect("/staff");
            return;
        }

        // PASSENGER / USER
        System.out.println("→ Redirecting to USER home");
        response.sendRedirect("/home");
    }
}