package org.example.busticketpro.service;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.TripResponseDto;
import org.example.busticketpro.entity.*;
import org.example.busticketpro.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository    tripRepository;
    private final RouteRepository   routeRepository;
    private final BusRepository     busRepository;
    private final SeatRepository    seatRepository;

    // ==================== LẤY TẤT CẢ CHUYẾN ====================
    public List<Trip> getAll() {
        return tripRepository.findAll();
    }

    // ==================== LẤY THEO ID ====================
    public Trip getById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến có id: " + id));
    }

    // ==================== LƯU CHUYẾN + TỰ ĐỘNG TẠO SEAT ====================
    @Transactional
    public Trip save(Trip trip, Long routeId, Long busId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tuyến đường!"));
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe!"));

        boolean isNew = (trip.getId() == null);

        trip.setRoute(route);
        trip.setBus(bus);
        trip.setActive(true);

        Trip savedTrip = tripRepository.save(trip);

        // Chỉ tạo ghế khi thêm MỚI, không tạo lại khi sửa
        if (isNew) {
            generateSeats(savedTrip, bus.getTotalSeats());
        }

        return savedTrip;
    }

    // ==================== XÓA CHUYẾN ====================
    @Transactional
    public void delete(Long id) {
        tripRepository.deleteById(id);
    }

    // ==================== TÌM KIẾM CHUYẾN (CORE-05) ====================
    public List<TripResponseDto> searchTrips(Long departureId, Long arrivalId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        List<Trip> trips = tripRepository.searchTrips(departureId, arrivalId, start, end);

        return trips.stream().map(trip -> {
            TripResponseDto dto = new TripResponseDto();
            dto.setTripId(trip.getId());
            dto.setBusPlate(trip.getBus().getLicensePlate());
            dto.setRouteName(
                    trip.getRoute().getDeparture().getName()
                            + " → "
                            + trip.getRoute().getArrival().getName()
            );
            dto.setDepartureTime(trip.getDepartureTime());
            dto.setPrice(trip.getPrice().doubleValue());

            // Đếm số ghế còn trống
            long bookedCount = seatRepository.countByTripIdAndStatus(trip.getId(), SeatStatus.BOOKED)
                    + seatRepository.countByTripIdAndStatus(trip.getId(), SeatStatus.PENDING);
            int totalSeats     = trip.getBus().getTotalSeats();
            int availableSeats = (int) (totalSeats - bookedCount);

            dto.setTotalSeats(totalSeats);
            dto.setAvailableSeats(Math.max(availableSeats, 0));
            return dto;
        }).collect(Collectors.toList());
    }

    // ==================== TẠO GHẾ TỰ ĐỘNG ====================
    private void generateSeats(Trip trip, int totalSeats) {
        // Đặt tên ghế theo dạng: A1, A2, B1, B2, ... (2 ghế mỗi hàng)
        String[] rows = {
                "A","B","C","D","E","F","G","H","I","J",
                "K","L","M","N","O","P","Q","R","S","T"
        };
        int cols     = 2;
        int rowCount = (int) Math.ceil((double) totalSeats / cols);

        List<Seat> seats = new ArrayList<>();
        int count = 0;

        for (int r = 0; r < rowCount && count < totalSeats; r++) {
            for (int c = 1; c <= cols && count < totalSeats; c++) {
                Seat seat = new Seat();
                seat.setTrip(trip);
                seat.setSeatNumber(rows[r] + c);
                seat.setFloor(1);
                seat.setStatus(SeatStatus.AVAILABLE);
                seats.add(seat);
                count++;
            }
        }

        seatRepository.saveAll(seats);
        System.out.println("✅ Đã tạo " + count + " ghế cho chuyến ID: " + trip.getId());
    }
}