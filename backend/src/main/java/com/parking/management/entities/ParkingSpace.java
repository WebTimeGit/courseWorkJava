package com.parking.management.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parking_spaces")
public class ParkingSpace {
    @Id
    private String id;
    private String location;
    private String status; // "FREE", "OCCUPIED"
    private String userId;

    // Getters та Setters
}
