package org.example.busticketpro.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.TicketDetailDTO;
import org.example.busticketpro.entity.Ticket;
import org.example.busticketpro.exception.SeatAlreadyBookedException;
import org.example.busticketpro.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/my-tickets")
@RequiredArgsConstructor
public class UserTicketController {

    private final TicketService ticketService;

    @GetMapping
    public String myTickets() {
        return "user/ticket/list";
    }

    // API: Lookup ticket by code and phone number
    @GetMapping("/api/lookup")
    @ResponseBody
    public ResponseEntity<?> lookupTicket(
            @RequestParam String ticketCode,
            @RequestParam String phoneNumber) {
        try {
            TicketDetailDTO ticketDetail = ticketService.getTicketDetail(ticketCode, phoneNumber);
            return ResponseEntity.ok(ticketDetail);
        } catch (SeatAlreadyBookedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ticket lookup failed: " + e.getMessage());
        }
    }

    // API: Cancel ticket (passenger can cancel up to 12 hours before departure)
    @PostMapping("/{ticketId}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelTicket(@PathVariable Long ticketId) {
        try {
            Ticket ticket = ticketService.cancelTicketForPassenger(ticketId);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}