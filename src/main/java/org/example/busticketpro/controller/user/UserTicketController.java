package org.example.busticketpro.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.TicketDetailDTO;
import org.example.busticketpro.dto.TicketSummaryDTO;
import org.example.busticketpro.security.CustomUserDetails;
import org.example.busticketpro.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/my-tickets")
@RequiredArgsConstructor
public class UserTicketController {

    private final TicketService ticketService;

    // === Ticket Lookup Page ===
    @GetMapping("/lookup")
    public String ticketLookup() {
        return "user/ticket/lookup";
    }

    // === SỬA Ở ĐÂY: Load danh sách vé của user đang login ===
    @GetMapping
    public String myTickets(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        List<TicketSummaryDTO> tickets = ticketService.getTicketsByUser(currentUser.getId());

        model.addAttribute("tickets", tickets);
        model.addAttribute("currentUser", currentUser);

        return "user/ticket/list";
    }

    // API Tra cứu vé (giữ nguyên - đã khá tốt)
    @GetMapping("/api/lookup")
    @ResponseBody
    public ResponseEntity<?> lookupTicket(
            @RequestParam String ticketCode,
            @RequestParam String phoneNumber) {
        try {
            TicketDetailDTO ticketDetail = ticketService.getTicketDetail(ticketCode, phoneNumber);
            return ResponseEntity.ok(ticketDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API Hủy vé — có kiểm tra ownership
    @PostMapping("/{ticketId}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelTicket(@PathVariable Long ticketId, Authentication authentication) {
        try {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

            ticketService.cancelTicketForPassenger(ticketId, currentUser.getId());
            return ResponseEntity.ok("Hủy vé thành công");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}