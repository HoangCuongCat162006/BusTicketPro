package org.example.busticketpro.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.entity.Trip;
import org.example.busticketpro.repository.BusRepository;
import org.example.busticketpro.repository.RouteRepository;
import org.example.busticketpro.service.TripService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService     tripService;
    private final RouteRepository routeRepository;
    private final BusRepository   busRepository;

    // ==================== DANH SÁCH CHUYẾN ====================
    @GetMapping
    public String listTrips(Model model) {
        model.addAttribute("trips", tripService.getAll());
        return "admin/trip/list";
    }

    // ==================== FORM THÊM CHUYẾN ====================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("trip",   new Trip());
        model.addAttribute("routes", routeRepository.findAll());
        model.addAttribute("buses",  busRepository.findAll());
        model.addAttribute("pageTitle", "Thêm chuyến xe");
        return "admin/trip/form";
    }

    // ==================== FORM SỬA CHUYẾN ====================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            Trip trip = tripService.getById(id);
            model.addAttribute("trip",   trip);
            model.addAttribute("routes", routeRepository.findAll());
            model.addAttribute("buses",  busRepository.findAll());
            model.addAttribute("pageTitle", "Sửa chuyến xe");
            return "admin/trip/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy chuyến!");
            return "redirect:/admin/trips";
        }
    }

    // ==================== LƯU CHUYẾN ====================
    // Nhận routeId và busId riêng thay vì binding object trực tiếp
    @PostMapping("/save")
    public String save(@ModelAttribute Trip trip,
                       @RequestParam Long routeId,
                       @RequestParam Long busId,
                       RedirectAttributes redirectAttributes) {
        try {
            tripService.save(trip, routeId, busId);
            redirectAttributes.addFlashAttribute("success",
                    "✅ Lưu chuyến xe thành công! Ghế đã được tạo tự động.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/trips";
    }

    // ==================== XÓA CHUYẾN ====================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            tripService.delete(id);
            redirectAttributes.addFlashAttribute("success", "✅ Xóa chuyến thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "❌ Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/trips";
    }
}