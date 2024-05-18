package com.parking.management.services;

import com.parking.management.entities.ParkingHistory;
import com.parking.management.repositories.ParkingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingService {

    private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    private final ParkingHistoryRepository parkingHistoryRepository;

    @Autowired
    public ParkingService(ParkingHistoryRepository parkingHistoryRepository) {
        this.parkingHistoryRepository = parkingHistoryRepository;
    }

    public void saveParkingHistory(ParkingHistory parkingHistory) {
        try {
            parkingHistoryRepository.save(parkingHistory);
            logger.info("Parking history saved successfully for user ID: " + parkingHistory.getUserId());
        } catch (Exception e) {
            logger.error("Failed to save parking history for user ID: " + parkingHistory.getUserId(), e);
            throw new RuntimeException("Failed to save parking history", e);
        }
    }

    public List<ParkingHistory> getParkingHistoryByUserId(Long userId) {
        try {
            List<ParkingHistory> historyList = parkingHistoryRepository.findByUserId(userId);
            logger.info("Retrieved parking history for user ID: " + userId);
            return historyList;
        } catch (Exception e) {
            logger.error("Failed to retrieve parking history for user ID: " + userId, e);
            throw new RuntimeException("Failed to retrieve parking history", e);
        }
    }

    public List<ParkingHistory> getParkingHistoryByParkingSpaceId(Long parkingSpaceId) {
        try {
            List<ParkingHistory> historyList = parkingHistoryRepository.findByParkingSpaceId(parkingSpaceId);
            logger.info("Retrieved parking history for parking space ID: " + parkingSpaceId);
            return historyList;
        } catch (Exception e) {
            logger.error("Failed to retrieve parking history for parking space ID: " + parkingSpaceId, e);
            throw new RuntimeException("Failed to retrieve parking history", e);
        }
    }

    public void updateParkingHistoryEndTime(ParkingHistory parkingHistory) {
        try {
            parkingHistory.setEndTime(LocalDateTime.now());
            parkingHistoryRepository.save(parkingHistory);
            logger.info("Updated parking history end time for user ID: " + parkingHistory.getUserId());
        } catch (Exception e) {
            logger.error("Failed to update parking history end time for user ID: " + parkingHistory.getUserId(), e);
            throw new RuntimeException("Failed to update parking history end time", e);
        }
    }
}
