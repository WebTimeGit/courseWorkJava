package com.parking.management.services;

import com.parking.management.entities.ParkingSpace;
import com.parking.management.repositories.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParkingService {
    private final ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public ParkingService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public List<ParkingSpace> getAllFreeSpaces() {
        return parkingSpaceRepository.findByStatus("FREE");
    }
}
