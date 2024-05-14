package com.parking.management.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parking_spaces")
public class ParkingSpace {
    @Id
    private long id;
    private Status status;

    public static final String SEQUENCE_NAME = "parking_space_sequence";

    public enum Status {
        OCCUPIED,
        FREE
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
}
