package org.example.busticketpro.controller.staff;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.TicketDetailDTO;
import org.example.busticketpro.entity.TicketStatus;
import org.example.busticketpro.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff/tickets")
@RequiredArgsConstructor
public class TicketManagementController {

    private final TicketService ticketService;

    /**
     * CORE-08: Danh sách vé để Staff duyệt
     */
    @GetMapping
    public String listTickets(Model model) {
        // Lấy tất cả vé, ưu tiên vé PENDING lên đầu
        List<TicketDetailDTO> tickets = ticketService.getAllTicketsForStaff();

        model.addAttribute("tickets", tickets);
        model.addAttribute("pageTitle", "Quản lý vé - Duyệt thanh toán");

        return "staff/ticket/list";   // Đường dẫn template
    }

    /**
     * Xác nhận thanh toán (PENDING → PAID)
     */
    @PostMapping("/{id}/confirm")
    @ResponseBody
    public String confirmPayment(@PathVariable Long id) {
        try {
            ticketService.confirmPayment(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    /**
     * Hủy vé (Staff có quyền hủy)
     */
    @PostMapping("/{id}/cancel")
    @ResponseBody
    public String cancelTicket(@PathVariable Long id) {
        try {
            ticketService.cancelTicketByStaff(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    /**
     * Xem chi tiết vé
     */
    @GetMapping("/{id}/detail")
    @ResponseBody
    public TicketDetailDTO getTicketDetail(@PathVariable Long id) {
        return ticketService.getTicketDetailById(id);
    }
}