package org.example.busticketpro.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.entity.Ticket;
import org.example.busticketpro.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/tickets")
@RequiredArgsConstructor
public class AdminTicketController {

    private final TicketService ticketService;

    @GetMapping
    public String listTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        model.addAttribute("pageTitle", "Quản lý Vé");
        return "admin/ticket/list";   // tên template
    }

    @PostMapping("/{id}/confirm")
    public String confirmTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ticketService.confirmPayment(id);
            redirectAttributes.addFlashAttribute("success", "✅ Xác nhận vé thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/tickets";
    }

    @PostMapping("/{id}/cancel")
    public String cancelTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ticketService.cancelTicketByStaff(id);
            redirectAttributes.addFlashAttribute("success", "✅ Hủy vé thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/tickets";
    }
}