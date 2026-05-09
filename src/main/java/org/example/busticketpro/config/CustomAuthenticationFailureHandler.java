package org.example.busticketpro.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        System.out.println("=== AUTHENTICATION FAILURE DEBUG ===");
        System.out.println("Authentication failed!");
        System.out.println("Exception: " + exception.getMessage());
        System.out.println("Exception type: " + exception.getClass().getName());
        System.out.println("Username parameter: " + request.getParameter("username"));
        
        // Set error message
        request.getSession().setAttribute("error", "Đăng nhập thất bại: " + exception.getMessage());
        
        // Redirect back to login page with error
        response.sendRedirect("/login?error");
    }
}
