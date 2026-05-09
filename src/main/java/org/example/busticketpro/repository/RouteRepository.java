package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Location;
import org.example.busticketpro.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    // Tìm tuyến theo điểm đi và điểm đến
    List<Route> findByDepartureAndArrival(
            Location departure,
            Location arrival
    );
}