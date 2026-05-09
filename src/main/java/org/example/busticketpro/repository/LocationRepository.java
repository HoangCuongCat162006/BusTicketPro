package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Tìm theo tên tỉnh/thành
    Optional<Location> findByName(String name);

    // Kiểm tra tên tồn tại
    boolean existsByName(String name);
}