package com.parking.management.services;

import com.parking.management.entities.ParkingSpace;
import com.parking.management.repositories.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpaceService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingSpaceService.class);

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public List<ParkingSpace> findAll() {
        try {
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();
            logger.info("Retrieved all parking spaces");
            return parkingSpaces;
        } catch (Exception e) {
            logger.error("Failed to retrieve all parking spaces", e);
            throw new RuntimeException("Failed to retrieve all parking spaces", e);
        }
    }

    public Optional<ParkingSpace> findById(Long id) {
        try {
            Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.findById(id);
            if (parkingSpace.isPresent()) {
                logger.info("Retrieved parking space with ID: " + id);
            } else {
                logger.warn("Parking space not found with ID: " + id);
            }
            return parkingSpace;
        } catch (Exception e) {
            logger.error("Failed to retrieve parking space with ID: " + id, e);
            throw new RuntimeException("Failed to retrieve parking space", e);
        }
    }

    public ParkingSpace save(ParkingSpace parkingSpace) {
        try {
            if (parkingSpace.getId() == 0) {
                parkingSpace.setId(sequenceGeneratorService.generateSequence(ParkingSpace.SEQUENCE_NAME));
            }
            ParkingSpace savedParkingSpace = parkingSpaceRepository.save(parkingSpace);
            logger.info("Saved parking space with ID: " + savedParkingSpace.getId());
            return savedParkingSpace;
        } catch (Exception e) {
            logger.error("Failed to save parking space", e);
            throw new RuntimeException("Failed to save parking space", e);
        }
    }

    public void deleteById(Long id) {
        try {
            parkingSpaceRepository.deleteById(id);
            logger.info("Deleted parking space with ID: " + id);
        } catch (Exception e) {
            logger.error("Failed to delete parking space with ID: " + id, e);
            throw new RuntimeException("Failed to delete parking space", e);
        }
    }

    public long countAll() {
        try {
            long count = parkingSpaceRepository.count();
            logger.info("Counted all parking spaces: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to count all parking spaces", e);
            throw new RuntimeException("Failed to count all parking spaces", e);
        }
    }

    public long countByStatus(ParkingSpace.Status status) {
        try {
            long count = parkingSpaceRepository.countByStatus(status);
            logger.info("Counted parking spaces with status " + status + ": " + count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to count parking spaces with status " + status, e);
            throw new RuntimeException("Failed to count parking spaces by status", e);
        }
    }
}
