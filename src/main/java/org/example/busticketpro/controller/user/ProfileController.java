package org.example.busticketpro.controller.user;

import jakarta.validation.Valid;
import org.example.busticketpro.dto.UpdateProfileRequest;
import org.example.busticketpro.entity.User;
import org.example.busticketpro.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    // ==================== XEM PROFILE ====================
    @GetMapping
    public String showProfile(Authentication authentication, Model model) {

        // Lấy username từ session Spring Security
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        // Tạo DTO và điền dữ liệu hiện tại của user
        UpdateProfileRequest profileRequest = new UpdateProfileRequest();
        profileRequest.setFullName(user.getFullName());
        profileRequest.setPhone(user.getPhone());

        if (user.getProfile() != null) {
            profileRequest.setAddress(user.getProfile().getAddress());
            profileRequest.setIdentityCard(user.getProfile().getIdentityCard());
        }

        model.addAttribute("user", user);
        model.addAttribute("profileRequest", profileRequest);

        return "user/profile/index";
    }

    // ==================== CẬP NHẬT PROFILE ====================
    @PostMapping("/update")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute("profileRequest") UpdateProfileRequest profileRequest,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        // Nếu có lỗi validation → trả lại form, giữ nguyên dữ liệu user hiển thị
        if (bindingResult.hasErrors()) {
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("profileRequest", profileRequest);
            return "user/profile/index";
        }

        try {
            userService.updateProfile(username, profileRequest);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        // PRG Pattern: redirect sau khi POST để tránh submit lại form
        return "redirect:/profile";
    }
}