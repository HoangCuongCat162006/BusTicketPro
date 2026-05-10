package org.example.busticketpro.config;

import org.example.busticketpro.config.CustomAuthenticationFailureHandler;
import org.example.busticketpro.security.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler,
                          CustomAuthenticationFailureHandler failureHandler) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Các đường dẫn công khai
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/home", "/auth/**", "/login").permitAll()

                        // Passenger có thể truy cập profile và booking
                        .requestMatchers("/user/**", "/booking/**", "/profile/**").hasAnyRole("PASSENGER", "STAFF", "ADMIN")

                        // Staff
                        .requestMatchers("/staff/**").hasAnyRole("STAFF", "ADMIN")

                        // Admin - Chỉ ADMIN mới vào được
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .permitAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}