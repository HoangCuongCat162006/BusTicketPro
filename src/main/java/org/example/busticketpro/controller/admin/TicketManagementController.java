package org.example.busticketpro.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.entity.Ticket;
import org.example.busticketpro.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tickets")
@RequiredArgsConstructor
public class TicketManagementController {

    private final TicketService ticketService;

    // Danh sách vé
    @GetMapping
    public String listTickets() {
        return "admin/ticket/list";
    }

    // API: Xác nhận thanh toán vé (PENDING → PAID)
    @PostMapping("/{ticketId}/confirm")
    @ResponseBody
    public ResponseEntity<?> confirmPayment(@PathVariable Long ticketId) {
        try {
            Ticket ticket = ticketService.confirmPayment(ticketId);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API: Hủy vé (PENDING → CANCELLED, release seat)
    @PostMapping("/{ticketId}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelTicket(@PathVariable Long ticketId) {
        try {
            Ticket ticket = ticketService.cancelTicket(ticketId);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}