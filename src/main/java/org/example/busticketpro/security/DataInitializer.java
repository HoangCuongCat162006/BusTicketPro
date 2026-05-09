// File path: src/main/java/org/example/busticketpro/security/DataInitializer.java
package org.example.busticketpro.security;

import org.example.busticketpro.entity.Role;
import org.example.busticketpro.entity.User;
import org.example.busticketpro.entity.UserProfile;
import org.example.busticketpro.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("✅ Dữ liệu người dùng đã tồn tại. Bỏ qua seed.");
            return;
        }

        try {
            // Admin
            createUser("admin", "admin@bus.com", "admin123", "Administrator", "0123456789", Role.ADMIN);

            // Staff
            createUser("staff", "staff@bus.com", "staff123", "Nhân viên", "0987654321", Role.STAFF);

            // Passenger
            createUser("user", "user@gmail.com", "user123", "Khách hàng", "0912345678", Role.PASSENGER);

            System.out.println("✅ Tạo tài khoản mẫu thành công!");
            System.out.println("Admin  -> admin / admin123");
            System.out.println("Staff  -> staff / staff123");
            System.out.println("User   -> user / user123");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi seed data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createUser(String username, String email, String rawPassword,
                            String fullName, String phone, Role role) {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setRole(role);

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
        System.out.println("   → Đã tạo: " + username);
    }
}