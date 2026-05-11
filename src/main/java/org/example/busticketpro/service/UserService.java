package org.example.busticketpro.service;

import org.example.busticketpro.dto.RegisterRequest;
import org.example.busticketpro.dto.UpdateProfileRequest;
import org.example.busticketpro.dto.UserResponse;
import org.example.busticketpro.entity.Role;
import org.example.busticketpro.entity.User;
import org.example.busticketpro.entity.UserProfile;
import org.example.busticketpro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==================== REGISTER ====================
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username '" + request.getUsername() + "' đã tồn tại. Vui lòng chọn tên khác.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email '" + request.getEmail() + "' đã được sử dụng. Vui lòng dùng email khác.");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName().trim());
        user.setPhone(request.getPhone().trim());
        user.setRole(Role.PASSENGER);

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        user.setProfile(profile);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }

    // ==================== FIND BY USERNAME ====================
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với username: " + username));
    }

    // ==================== UPDATE PROFILE ====================
    @Transactional
    public void updateProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        // Cập nhật thông tin User
        user.setFullName(request.getFullName().trim());
        user.setPhone(request.getPhone().trim());

        // Cập nhật UserProfile
        UserProfile profile = user.getProfile();
        if (profile == null) {
            // Trường hợp profile chưa tồn tại → tạo mới
            profile = new UserProfile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress().trim());
        }
        if (request.getIdentityCard() != null) {
            profile.setIdentityCard(request.getIdentityCard().trim());
        }

        userRepository.save(user);
    }
}