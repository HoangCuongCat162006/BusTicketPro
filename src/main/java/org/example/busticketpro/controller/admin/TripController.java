package org.example.busticketpro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/trips")
public class TripController {

    // Danh sách chuyến xe
    @GetMapping
    public String listTrips() {

        return "admin/trip/list";
    }

    // Form thêm chuyến
    @GetMapping("/create")
    public String createForm() {

        return "admin/trip/form";
    }
}