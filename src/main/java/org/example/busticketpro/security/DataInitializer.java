package org.example.busticketpro.security;

import org.example.busticketpro.entity.*;
import org.example.busticketpro.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LocationRepository locationRepository;
    private final RouteRepository routeRepository;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           LocationRepository locationRepository,
                           RouteRepository routeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.locationRepository = locationRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        seedLocations();
        seedRoutes();
    }

    // ==================== SEED USERS ====================
    private void seedUsers() {
        if (userRepository.count() > 0) {
            System.out.println("✅ Users đã tồn tại. Bỏ qua seed users.");
            return;
        }
        try {
            createUser("admin", "admin@bus.com", "admin123", "Quản trị viên", "0123456789", Role.ADMIN);
            createUser("staff", "staff@bus.com", "staff123", "Nhân viên", "0987654321", Role.STAFF);
            createUser("user",  "user@gmail.com", "user123",  "Khách hàng",  "0912345678", Role.PASSENGER);

            System.out.println("✅ Seed users thành công!");
            System.out.println("   Admin   → admin / admin123");
            System.out.println("   Staff   → staff / staff123");
            System.out.println("   User    → user / user123");
        } catch (Exception e) {
            System.err.println("❌ Lỗi seed users: " + e.getMessage());
        }
    }

    // ==================== SEED LOCATIONS ====================
    private void seedLocations() {
        if (locationRepository.count() > 0) {
            System.out.println("✅ Locations đã tồn tại. Bỏ qua seed locations.");
            return;
        }
        try {
            createLocation("Hà Nội",           "Hà Nội");
            createLocation("Đà Nẵng",          "Đà Nẵng");
            createLocation("TP. Hồ Chí Minh",  "TP. Hồ Chí Minh");
            createLocation("Huế",              "Thừa Thiên Huế");
            createLocation("Nha Trang",        "Khánh Hòa");
            createLocation("Hải Phòng",        "Hải Phòng");
            createLocation("Cần Thơ",          "Cần Thơ");
            createLocation("Vinh",             "Nghệ An");
            createLocation("Đà Lạt",           "Lâm Đồng");
            createLocation("Quy Nhơn",         "Bình Định");

            System.out.println("✅ Seed locations thành công! (10 tỉnh/thành)");
        } catch (Exception e) {
            System.err.println("❌ Lỗi seed locations: " + e.getMessage());
        }
    }

    // ==================== SEED ROUTES ====================
    private void seedRoutes() {
        if (routeRepository.count() > 0) {
            System.out.println("✅ Routes đã tồn tại. Bỏ qua seed routes.");
            return;
        }
        try {
            // Lấy locations đã seed
            Location haNoi    = getLocation("Hà Nội");
            Location daNang   = getLocation("Đà Nẵng");
            Location tphcm    = getLocation("TP. Hồ Chí Minh");
            Location hue      = getLocation("Huế");
            Location nhaTrang = getLocation("Nha Trang");
            Location haiPhong = getLocation("Hải Phòng");
            Location canTho   = getLocation("Cần Thơ");
            Location vinh     = getLocation("Vinh");
            Location dalat    = getLocation("Đà Lạt");
            Location quiNhon  = getLocation("Quy Nhơn");

            // distance (km), duration (phút)
            createRoute(haNoi,    daNang,   new BigDecimal("770"),  780);
            createRoute(daNang,   haNoi,    new BigDecimal("770"),  780);

            createRoute(haNoi,    tphcm,    new BigDecimal("1700"), 1680);
            createRoute(tphcm,    haNoi,    new BigDecimal("1700"), 1680);

            createRoute(daNang,   tphcm,    new BigDecimal("960"),  960);
            createRoute(tphcm,    daNang,   new BigDecimal("960"),  960);

            createRoute(haNoi,    hue,      new BigDecimal("660"),  720);
            createRoute(hue,      haNoi,    new BigDecimal("660"),  720);

            createRoute(haNoi,    vinh,     new BigDecimal("295"),  300);
            createRoute(vinh,     haNoi,    new BigDecimal("295"),  300);

            createRoute(haNoi,    haiPhong, new BigDecimal("120"),  150);
            createRoute(haiPhong, haNoi,    new BigDecimal("120"),  150);

            createRoute(daNang,   hue,      new BigDecimal("100"),  120);
            createRoute(hue,      daNang,   new BigDecimal("100"),  120);

            createRoute(tphcm,    nhaTrang, new BigDecimal("450"),  480);
            createRoute(nhaTrang, tphcm,    new BigDecimal("450"),  480);

            createRoute(tphcm,    canTho,   new BigDecimal("170"),  180);
            createRoute(canTho,   tphcm,    new BigDecimal("170"),  180);

            createRoute(tphcm,    dalat,    new BigDecimal("300"),  360);
            createRoute(dalat,    tphcm,    new BigDecimal("300"),  360);

            createRoute(daNang,   quiNhon,  new BigDecimal("300"),  330);
            createRoute(quiNhon,  daNang,   new BigDecimal("300"),  330);

            System.out.println("✅ Seed routes thành công! (22 tuyến)");
        } catch (Exception e) {
            System.err.println("❌ Lỗi seed routes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== HELPER METHODS ====================
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
        System.out.println("   → Đã tạo user: " + username);
    }

    private void createLocation(String name, String province) {
        Location location = new Location();
        location.setName(name);
        location.setProvince(province);
        locationRepository.save(location);
    }

    private Location getLocation(String name) {
        return locationRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy location: " + name));
    }

    private void createRoute(Location departure, Location arrival,
                             BigDecimal distance, Integer duration) {
        Route route = new Route();
        route.setDeparture(departure);
        route.setArrival(arrival);
        route.setDistance(distance);
        route.setDuration(duration);
        routeRepository.save(route);
    }
}