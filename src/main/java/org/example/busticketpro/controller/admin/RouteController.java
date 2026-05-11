package org.example.busticketpro.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.entity.Location;
import org.example.busticketpro.entity.Route;
import org.example.busticketpro.repository.LocationRepository;
import org.example.busticketpro.repository.RouteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;

    // Danh sách tuyến
    @GetMapping
    public String listRoutes(Model model) {
        List<Route> routes = routeRepository.findAll();
        model.addAttribute("routes", routes);
        return "admin/route/list";
    }

    // Form thêm tuyến
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("route", new Route());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("pageTitle", "Thêm tuyến đường");
        return "admin/route/form";
    }

    // Form sửa tuyến
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Route route = routeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tuyến"));
            model.addAttribute("route", route);
            model.addAttribute("locations", locationRepository.findAll());
            model.addAttribute("pageTitle", "Sửa tuyến đường");
            return "admin/route/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tuyến!");
            return "redirect:/admin/routes";
        }
    }

    // Lưu tuyến
    @PostMapping("/save")
    public String save(@ModelAttribute Route route,
                       @RequestParam Long departureId,
                       @RequestParam Long arrivalId,
                       RedirectAttributes redirectAttributes) {
        try {
            Location departure = locationRepository.findById(departureId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy điểm đi"));
            Location arrival = locationRepository.findById(arrivalId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy điểm đến"));

            route.setDeparture(departure);
            route.setArrival(arrival);
            routeRepository.save(route);
            redirectAttributes.addFlashAttribute("success", "✅ Lưu tuyến thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/routes";
    }

    // Xóa tuyến
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            routeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "✅ Xóa tuyến thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/routes";
    }
}