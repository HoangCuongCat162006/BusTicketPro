package org.example.busticketpro.service;

import org.example.busticketpro.entity.Bus;
import org.example.busticketpro.repository.BusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusService {

    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<Bus> getAll() {
        return busRepository.findAll();
    }

    public Bus getById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe có id: " + id));
    }

    @Transactional
    public Bus save(Bus bus) {
        // Kiểm tra trùng biển số khi THÊM MỚI (id == null)
        if (bus.getId() == null) {
            if (busRepository.existsByLicensePlate(bus.getLicensePlate())) {
                throw new RuntimeException("Biển số xe '" + bus.getLicensePlate() + "' đã tồn tại trong hệ thống!");
            }
        }
        return busRepository.save(bus);
    }

    @Transactional
    public void delete(Long id) {
        busRepository.deleteById(id);
    }
}