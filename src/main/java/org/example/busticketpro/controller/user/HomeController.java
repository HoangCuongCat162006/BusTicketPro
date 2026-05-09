// File path: src/main/java/org/example/busticketpro/controller/user/HomeController.java
package org.example.busticketpro.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/user/home";
    }

    @GetMapping("/user/home")
    public String home() {
        return "user/home";
    }
}