package com.parking.management.dto;


public class ParkingSpaceCreateDTO {
    private String status;

    // Конструктор без параметрів (потрібен для десеріалізації)
    public ParkingSpaceCreateDTO() {}

    // Конструктор з параметрами
    public ParkingSpaceCreateDTO(String status) {
        this.status = status;
    }

    // Геттер і сеттер
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
