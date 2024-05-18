package com.parking.management.dto;

import java.time.LocalDateTime;

public class ParkingHistoryDTO {
    private Long parkingSpaceId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String username;
    private String email;

    // Конструктори, геттери та сеттери

    public ParkingHistoryDTO(Long parkingSpaceId, Long userId, LocalDateTime startTime, LocalDateTime endTime, String username, String email) {
        this.parkingSpaceId = parkingSpaceId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.username = username;
        this.email = email;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
