package com.parking.management.dto;

import com.parking.management.entities.ParkingSpace;

public class ParkingSpaceDTO {
    private Long id;
    private ParkingSpace.Status status;

    public ParkingSpaceDTO(Long id, ParkingSpace.Status status) {
        this.id = id;
        this.status = status;
    }

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
}
