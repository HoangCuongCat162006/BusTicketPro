package org.example.busticketpro.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my-tickets")
public class UserTicketController {

    @GetMapping
    public String myTickets() {

        return "user/ticket/list";
    }
}