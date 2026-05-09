package org.example.busticketpro.controller.staff;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffController {

    @GetMapping("/staff")
    public String staffDashboard() {
        return "staff/index";
    }
}