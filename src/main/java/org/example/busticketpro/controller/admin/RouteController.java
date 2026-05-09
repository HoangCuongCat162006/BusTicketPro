package org.example.busticketpro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/routes")
public class RouteController {

    // Danh sách tuyến
    @GetMapping
    public String listRoutes() {

        return "admin/route/list";
    }

    // Form thêm tuyến
    @GetMapping("/create")
    public String createForm() {

        return "admin/route/form";
    }
}