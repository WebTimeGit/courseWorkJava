package com.parking.management.dto;

public class ParkingSpaceUpdateDTO {
    private String status;

    // Конструктор без параметрів (потрібен для десеріалізації)
    public ParkingSpaceUpdateDTO() {}

    // Конструктор з параметрами
    public ParkingSpaceUpdateDTO(String status) {
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