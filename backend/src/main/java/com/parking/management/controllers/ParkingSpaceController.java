package com.parking.management.controllers;

import com.parking.management.entities.ParkingSpace;
import com.parking.management.services.ParkingSpaceService;
import com.parking.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parkingspaces")
public class ParkingSpaceController {
    private static final Logger logger = LoggerFactory.getLogger(ParkingSpaceController.class);

    private final ParkingSpaceService parkingSpaceService;
    private final UserService userService;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService, UserService userService) {
        this.parkingSpaceService = parkingSpaceService;
        this.userService = userService;
    }

    @GetMapping
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceService.findAll();
    }

    @GetMapping("/count")
    public ResponseEntity<?> getParkingSpaceCounts() {
        long total = parkingSpaceService.countAll();
        long free = parkingSpaceService.countByStatus(ParkingSpace.Status.FREE);
        return ResponseEntity.ok(new ParkingSpaceCountsDTO(total, free));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getParkingSpaceById(@PathVariable Long id) {
        Optional<ParkingSpace> parkingSpace = parkingSpaceService.findById(id);
        if (parkingSpace.isPresent()) {
            return ResponseEntity.ok(parkingSpace.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ParkingSpace> createParkingSpace(@RequestHeader("Authorization") String token, @RequestBody ParkingSpace parkingSpace) {
        String cleanToken = token.substring(7);
        if (userService.isAdmin(cleanToken)) {
            ParkingSpace createdSpace = parkingSpaceService.save(parkingSpace);
            return ResponseEntity.ok(createdSpace);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpace> updateParkingSpace(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody ParkingSpace parkingSpaceDetails) {
        Optional<ParkingSpace> parkingSpace = parkingSpaceService.findById(id);
        if (parkingSpace.isPresent()) {
            ParkingSpace existingParkingSpace = parkingSpace.get();
            existingParkingSpace.setStatus(parkingSpaceDetails.getStatus());
            ParkingSpace updatedParkingSpace = parkingSpaceService.save(existingParkingSpace);
            return ResponseEntity.ok(updatedParkingSpace);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpace(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        String cleanToken = token.substring(7); // Видалення префіксу "Bearer "
        if (userService.isAdmin(cleanToken)) {
            if (parkingSpaceService.findById(id).isPresent()) {
                parkingSpaceService.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }

    static class ParkingSpaceCountsDTO {
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
}
