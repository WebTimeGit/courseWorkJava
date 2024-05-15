package com.parking.management.controllers;

import com.parking.management.dto.ErrorResponseDTO;
import com.parking.management.dto.ParkingSpaceCountsDTO;
import com.parking.management.entities.ParkingSpace;
import com.parking.management.services.ParkingSpaceService;
import com.parking.management.services.UserService;
import com.parking.management.config.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: No authentication found.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        String currentRole = authentication.getAuthorities().iterator().next().getAuthority();
        if (!"ROLE_ADMIN".equals(currentRole) && !"ROLE_USER".equals(currentRole)) {
            logger.error("User does not have the necessary role. Role found: " + currentRole);
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: You don't have the necessary role to update parking spaces.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
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
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Parking space not found for ID: " + id, System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createParkingSpace(@RequestHeader("Authorization") String token, @RequestBody ParkingSpace parkingSpace) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.info("Token received: " + token);  // Додаємо журнал
            if (userService.isAdmin(token)) {
                ParkingSpace createdSpace = parkingSpaceService.save(parkingSpace);
                return ResponseEntity.ok(createdSpace);
            } else {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only admins can create parking spaces.", System.currentTimeMillis());
                logger.error("Access denied: Only admins can create parking spaces.");  // Додаємо журнал
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        }
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
        logger.error("Invalid token.");  // Додаємо журнал
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteParkingSpace(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.info("Token received: " + token);  // Додаємо журнал
            if (userService.isAdmin(token)) {
                Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
                if (parkingSpaceOptional.isPresent()) {
                    parkingSpaceService.deleteById(id);
                    return ResponseEntity.ok().build();
                } else {
                    ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Parking space not found.", System.currentTimeMillis());
                    logger.error("Parking space not found for ID: " + id);  // Додаємо журнал
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }
            } else {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only admins can delete parking spaces.", System.currentTimeMillis());
                logger.error("Access denied: Only admins can delete parking spaces.");  // Додаємо журнал
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        }
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
        logger.error("Invalid token.");  // Додаємо журнал
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
