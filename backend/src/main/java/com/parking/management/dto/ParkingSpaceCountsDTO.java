package com.parking.management.dto;

public class ParkingSpaceCountsDTO {
    private long total;
    private long free;

    public ParkingSpaceCountsDTO(long total, long free) {
        this.total = total;
        this.free = free;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }
}
