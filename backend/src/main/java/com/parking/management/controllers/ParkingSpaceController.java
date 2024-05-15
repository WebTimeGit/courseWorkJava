package com.parking.management.controllers;

import com.parking.management.config.ResourceNotFoundException;
import com.parking.management.dto.ParkingSpaceCountsDTO;
import com.parking.management.entities.ParkingSpace;
import com.parking.management.services.ParkingSpaceService;
import com.parking.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/getAll")
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceService.findAll();
    }

    @GetMapping("/count")
    public ResponseEntity<ParkingSpaceCountsDTO> getParkingSpaceCounts() {
        long total = parkingSpaceService.countAll();
        long free = parkingSpaceService.countByStatus(ParkingSpace.Status.FREE);
        return ResponseEntity.ok(new ParkingSpaceCountsDTO(total, free));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateParkingSpace(@PathVariable Long id, @RequestBody ParkingSpace parkingSpaceDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("No authentication found in SecurityContextHolder");
            throw new org.springframework.security.access.AccessDeniedException("Forbidden: No authentication found.");
        }

        String currentRole = authentication.getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_ADMIN".equals(currentRole) && !"ROLE_USER".equals(currentRole)) {
            logger.error("User does not have the necessary role. Role found: " + currentRole);
            throw new org.springframework.security.access.AccessDeniedException("Forbidden: You don't have the necessary role to update parking spaces.");
        }

        Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
        if (parkingSpaceOptional.isPresent()) {
            ParkingSpace existingParkingSpace = parkingSpaceOptional.get();
            existingParkingSpace.setStatus(parkingSpaceDetails.getStatus());
            ParkingSpace updatedParkingSpace = parkingSpaceService.save(existingParkingSpace);
            logger.info("Parking space updated successfully: " + updatedParkingSpace);
            return ResponseEntity.ok(updatedParkingSpace);
        } else {
            logger.error("Parking space not found for ID: " + id);
            throw new ResourceNotFoundException("Parking space not found for ID: " + id);
        }
    }

    @RestController
    @RequestMapping("/api/admin/parkingspaces")
    public static class AdminParkingSpaceController {

        private final ParkingSpaceService parkingSpaceService;
        private final UserService userService;

        @Autowired
        public AdminParkingSpaceController(ParkingSpaceService parkingSpaceService, UserService userService) {
            this.parkingSpaceService = parkingSpaceService;
            this.userService = userService;
        }

        @PostMapping("/create")
        public ResponseEntity<?> createParkingSpace(@RequestHeader("Authorization") String token, @RequestBody ParkingSpace parkingSpace) {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (userService.isAdmin(token)) {
                    ParkingSpace createdSpace = parkingSpaceService.save(parkingSpace);
                    return ResponseEntity.ok(createdSpace);
                } else {
                    throw new org.springframework.security.access.AccessDeniedException("Forbidden: Only admins can create parking spaces.");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> deleteParkingSpace(@RequestHeader("Authorization") String token, @PathVariable Long id) {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (userService.isAdmin(token)) {
                    Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
                    if (parkingSpaceOptional.isPresent()) {
                        parkingSpaceService.deleteById(id);
                        return ResponseEntity.ok().build();
                    } else {
                        throw new ResourceNotFoundException("Parking space not found.");
                    }
                } else {
                    throw new org.springframework.security.access.AccessDeniedException("Forbidden: Only admins can delete parking spaces.");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
