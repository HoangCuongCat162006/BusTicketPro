package org.example.busticketpro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tickets")
public class TicketManagementController {

    // Danh sách vé
    @GetMapping
    public String listTickets() {

        return "admin/ticket/list";
    }
}