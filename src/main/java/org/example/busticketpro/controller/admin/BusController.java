package org.example.busticketpro.controller.admin;

import org.example.busticketpro.entity.Bus;
import org.example.busticketpro.service.BusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    // ==================== DANH SÁCH XE ====================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("buses", busService.getAll());
        model.addAttribute("pageTitle", "Quản lý Xe");
        return "admin/bus/list";
    }

    // ==================== FORM THÊM XE ====================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("bus", new Bus());
        model.addAttribute("pageTitle", "Thêm Xe Mới");
        return "admin/bus/form";
    }

    // ==================== LƯU XE (THÊM / SỬA) ====================
    @PostMapping("/save")
    public String save(@ModelAttribute Bus bus, RedirectAttributes redirectAttributes) {
        try {
            busService.save(bus);
            redirectAttributes.addFlashAttribute("success",
                    "✅ Xe đã được lưu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "❌ Lỗi khi lưu xe: " + e.getMessage());
        }
        return "redirect:/admin/buses";
    }

    // ==================== FORM SỬA XE ====================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Bus bus = busService.getById(id);
            model.addAttribute("bus", bus);
            model.addAttribute("pageTitle", "Cập nhật Xe");
            return "admin/bus/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy xe!");
            return "redirect:/admin/buses";
        }
    }

    // ==================== XÓA XE ====================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            busService.delete(id);
            redirectAttributes.addFlashAttribute("success",
                    "✅ Xóa xe thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "❌ Không thể xóa xe: " + e.getMessage());
        }
        return "redirect:/admin/buses";
    }
}