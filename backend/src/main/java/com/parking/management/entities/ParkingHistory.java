package com.parking.management.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "parking_history")
public class ParkingHistory {
    @Id
    private String id;
    private Long userId;
    private Long parkingSpaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ParkingHistory(Long userId, Long parkingSpaceId, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.parkingSpaceId = parkingSpaceId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
