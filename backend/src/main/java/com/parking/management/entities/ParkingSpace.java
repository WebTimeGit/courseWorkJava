package com.parking.management.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parking_spaces")
public class ParkingSpace {
    @Id
    private long id;
    private Status status;
    private Long occupiedByUserId;
    private Long serviceByAdminId;
    private String serviceReason;

    public static final String SEQUENCE_NAME = "parking_space_seq";

    public enum Status {
        OCCUPIED,
        FREE,
        SERVICE
    }

    // Конструктори
    public ParkingSpace() {}

    public ParkingSpace(Status status) {
        this.status = status;
    }

    // Геттери та сеттери
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getOccupiedByUserId() {
        return occupiedByUserId;
    }

    public void setOccupiedByUserId(Long occupiedByUserId) {
        this.occupiedByUserId = occupiedByUserId;
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
