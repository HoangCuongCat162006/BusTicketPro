package org.example.busticketpro.repository;

import org.example.busticketpro.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // XÓA hoặc COMMENT method này vì email nằm ở User
    // Optional<UserProfile> findByEmail(String email);
    // boolean existsByEmail(String email);
}