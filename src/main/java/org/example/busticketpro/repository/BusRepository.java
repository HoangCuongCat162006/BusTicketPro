package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    boolean existsByLicensePlate(String licensePlate);

    Optional<Bus> findByLicensePlate(String licensePlate);
}