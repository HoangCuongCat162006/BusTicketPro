package org.example.busticketpro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminRedirect() {
        return "redirect:/admin/dashboard";   // ← sửa: redirect thay vì trả thẳng template
    }

    // THÊM MỚI
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
}