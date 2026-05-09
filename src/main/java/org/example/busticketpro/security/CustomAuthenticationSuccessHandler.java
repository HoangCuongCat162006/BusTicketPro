// File path: src/main/java/org/example/busticketpro/security/CustomAuthenticationSuccessHandler.java
package org.example.busticketpro.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isStaff = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"));

        String redirectUrl;

        if (isAdmin) {
            redirectUrl = "/admin";           // ← Thay đổi theo trang admin cũ của bạn
            System.out.println("Redirect to ADMIN: /admin");
        } else if (isStaff) {
            redirectUrl = "/staff";           // ← Thay đổi theo trang staff cũ của bạn
            System.out.println("Redirect to STAFF: /staff");
        } else {
            redirectUrl = "/user/home";
            System.out.println("Redirect to USER HOME");
        }

        response.sendRedirect(redirectUrl);
    }
}