package com.parking.management.dto;

import com.parking.management.entities.ParkingSpace;

public class AdminParkingSpaceDTO {
    private Long id;
    private ParkingSpace.Status status;
    private Long occupiedByUserId;
    private String occupiedByUsername;
    private String occupiedByEmail;
    private Long serviceByAdminId;
    private String serviceReason;

    public AdminParkingSpaceDTO(Long id, ParkingSpace.Status status, Long occupiedByUserId, String occupiedByUsername, String occupiedByEmail, Long serviceByAdminId, String serviceReason) {
        this.id = id;
        this.status = status;
        this.occupiedByUserId = occupiedByUserId;
        this.occupiedByUsername = occupiedByUsername;
        this.occupiedByEmail = occupiedByEmail;
        this.serviceByAdminId = serviceByAdminId;
        this.serviceReason = serviceReason;
    }

    // Геттери та сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParkingSpace.Status getStatus() {
        return status;
    }

    public void setStatus(ParkingSpace.Status status) {
        this.status = status;
    }

    public Long getOccupiedByUserId() {
        return occupiedByUserId;
    }

    public void setOccupiedByUserId(Long occupiedByUserId) {
        this.occupiedByUserId = occupiedByUserId;
    }

    public String getOccupiedByUsername() {
        return occupiedByUsername;
    }

    public void setOccupiedByUsername(String occupiedByUsername) {
        this.occupiedByUsername = occupiedByUsername;
    }

    public String getOccupiedByEmail() {
        return occupiedByEmail;
    }

    public void setOccupiedByEmail(String occupiedByEmail) {
        this.occupiedByEmail = occupiedByEmail;
    }

    public Long getServiceByAdminId() {
        return serviceByAdminId;
    }

    public void setServiceByAdminId(Long serviceByAdminId) {
        this.serviceByAdminId = serviceByAdminId;
    }

    public String getServiceReason() {
        return serviceReason;
    }

    public void setServiceReason(String serviceReason) {
        this.serviceReason = serviceReason;
    }
}
