// File path: src/main/java/org/example/busticketpro/controller/admin/AdminInitController.java
package org.example.busticketpro.controller.admin;

import org.example.busticketpro.security.DataInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminInitController {

    private final DataInitializer dataInitializer;

    public AdminInitController(DataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }

    @GetMapping("/reset-data")
    @ResponseBody
    public String resetData() {
        try {
            // Xóa hết user cũ
            // dataInitializer.userRepository.deleteAll();  // uncomment nếu muốn xóa hết
            return "Đang reset data... Hãy restart ứng dụng để seed lại.";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}