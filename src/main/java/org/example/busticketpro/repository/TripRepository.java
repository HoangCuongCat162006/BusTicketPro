package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Route;
import org.example.busticketpro.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    // Theo tuyến
    List<Trip> findByRoute(Route route);

    // Theo ngày khởi hành
    List<Trip> findByDepartureTimeBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    // Route + ngày
    List<Trip> findByRouteAndDepartureTimeBetween(
            Route route,
            LocalDateTime start,
            LocalDateTime end
    );

    // Chuyến còn hoạt động
    List<Trip> findByIsActiveTrue();

    // Theo bus
    List<Trip> findByBusId(Long busId);
}