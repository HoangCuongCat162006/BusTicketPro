package org.example.busticketpro.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {

    @GetMapping("/home")
    public String home() {
        return "user/home";   // đảm bảo file này tồn tại
    }
}