package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Role;
import org.example.busticketpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Username
    Optional<User> findByUsername(String username);

    // Email
    Optional<User> findByEmail(String email);

    // Username tồn tại
    boolean existsByUsername(String username);

    // Email tồn tại
    boolean existsByEmail(String email);

    // Theo role
    List<User> findByRole(Role role);
}