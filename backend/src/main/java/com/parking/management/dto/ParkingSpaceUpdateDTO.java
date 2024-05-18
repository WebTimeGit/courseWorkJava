package com.parking.management.dto;

public class ParkingSpaceUpdateDTO {
    private String status;
    private String serviceReason;

    // Конструктори
    public ParkingSpaceUpdateDTO() {}

    public ParkingSpaceUpdateDTO(String status, String serviceReason) {
        this.status = status;
        this.serviceReason = serviceReason;
    }

    // Геттери та сеттери
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceReason() {
        return serviceReason;
    }

    public void setServiceReason(String serviceReason) {
        this.serviceReason = serviceReason;
    }
}
