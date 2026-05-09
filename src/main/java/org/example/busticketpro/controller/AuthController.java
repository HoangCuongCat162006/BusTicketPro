// File path: src/main/java/org/example/busticketpro/controller/AuthController.java
package org.example.busticketpro.controller;

import jakarta.validation.Valid;
import org.example.busticketpro.dto.LoginRequest;
import org.example.busticketpro.dto.RegisterRequest;
import org.example.busticketpro.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ====================== REGISTER ======================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("registerRequest", request);
            return "auth/register";   // Quay lại form + giữ dữ liệu + hiển thị lỗi
        }

        try {
            userService.register(request);
            redirectAttributes.addFlashAttribute("success", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            // Lỗi business (username/email tồn tại, ...)
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    // ====================== LOGIN ======================
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }
}