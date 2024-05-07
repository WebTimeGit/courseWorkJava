package com.parking.management.controllers;

import com.parking.management.entities.ParkingSpace;
import com.parking.management.services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    private final ParkingService parkingService;

    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/free")
    public List<ParkingSpace> getAllFreeSpaces() {
        return parkingService.getAllFreeSpaces();
    }
}
