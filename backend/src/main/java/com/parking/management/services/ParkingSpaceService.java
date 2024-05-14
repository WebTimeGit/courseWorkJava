package com.parking.management.services;

import com.parking.management.entities.ParkingSpace;
import com.parking.management.repositories.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public List<ParkingSpace> findAll() {
        return parkingSpaceRepository.findAll();
    }

    public Optional<ParkingSpace> findById(Long id) {
        return parkingSpaceRepository.findById(id);
    }

    public ParkingSpace save(ParkingSpace parkingSpace) {
        if (parkingSpace.getId() == 0) {
            parkingSpace.setId(sequenceGeneratorService.generateSequence(ParkingSpace.SEQUENCE_NAME));
        }
        return parkingSpaceRepository.save(parkingSpace);
    }

    public void deleteById(Long id) {
        parkingSpaceRepository.deleteById(id);
    }

    public long countAll() {
        return parkingSpaceRepository.count();
    }

    public long countByStatus(ParkingSpace.Status status) {
        return parkingSpaceRepository.countByStatus(status);
    }
}
