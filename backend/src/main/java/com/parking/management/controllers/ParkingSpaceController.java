package com.parking.management.controllers;

import com.parking.management.dto.ErrorResponseDTO;
import com.parking.management.dto.ParkingHistoryDTO;
import com.parking.management.dto.ParkingSpaceDTO;
import com.parking.management.dto.ParkingSpaceCountsDTO;
import com.parking.management.entities.ParkingSpace;
import com.parking.management.entities.ParkingHistory;
import com.parking.management.entities.User;
import com.parking.management.services.ParkingSpaceService;
import com.parking.management.services.ParkingService;
import com.parking.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parkingspaces")
public class ParkingSpaceController {
    private static final Logger logger = LoggerFactory.getLogger(ParkingSpaceController.class);

    private final ParkingSpaceService parkingSpaceService;
    private final ParkingService parkingService;
    private final UserService userService;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService, ParkingService parkingService, UserService userService) {
        this.parkingSpaceService = parkingSpaceService;
        this.parkingService = parkingService;
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ParkingSpaceDTO>> getAllParkingSpaces() {
        List<ParkingSpaceDTO> parkingSpaces = parkingSpaceService.findAll().stream()
                .map(space -> new ParkingSpaceDTO(space.getId(), space.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(parkingSpaces);
    }

    @GetMapping("/count")
    public ResponseEntity<ParkingSpaceCountsDTO> getParkingSpaceCounts() {
        long total = parkingSpaceService.countAll();
        long free = parkingSpaceService.countByStatus(ParkingSpace.Status.FREE);
        return ResponseEntity.ok(new ParkingSpaceCountsDTO(total, free));
    }

    @PostMapping("/reserve/{id}")
    public ResponseEntity<?> reserveParkingSpace(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("No authentication found in SecurityContextHolder");
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: No authentication found.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        String currentRole = authentication.getAuthorities().iterator().next().getAuthority();
        String email = authentication.getName();
        if (!"ROLE_USER".equals(currentRole)) {
            logger.error("User does not have the necessary role. Role found: " + currentRole);
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only users can reserve parking spaces.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
        if (parkingSpaceOptional.isPresent()) {
            ParkingSpace parkingSpace = parkingSpaceOptional.get();
            if (parkingSpace.getStatus() == ParkingSpace.Status.FREE) {
                parkingSpace.setStatus(ParkingSpace.Status.OCCUPIED);
                parkingSpaceService.save(parkingSpace);

                Optional<User> userOptional = userService.getUserByEmail(email);
                if (!userOptional.isPresent()) {
                    ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "User not found for email: " + email, System.currentTimeMillis());
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }

                ParkingHistory parkingHistory = new ParkingHistory(
                        userOptional.get().getId(),
                        parkingSpace.getId(),
                        LocalDateTime.now(),
                        null
                );
                parkingService.saveParkingHistory(parkingHistory);

                logger.info("Parking space reserved successfully: " + parkingSpace);
                ParkingSpaceDTO parkingSpaceDTO = new ParkingSpaceDTO(parkingSpace.getId(), parkingSpace.getStatus());
                return ResponseEntity.ok(parkingSpaceDTO);
            } else {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), "Parking space is already occupied.", System.currentTimeMillis());
                logger.error("Parking space is already occupied: " + parkingSpace);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } else {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Parking space not found for ID: " + id, System.currentTimeMillis());
            logger.error("Parking space not found for ID: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/release/{id}")
    public ResponseEntity<?> releaseParkingSpace(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("No authentication found in SecurityContextHolder");
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: No authentication found.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        String currentRole = authentication.getAuthorities().iterator().next().getAuthority();
        String email = authentication.getName();
        if (!"ROLE_USER".equals(currentRole)) {
            logger.error("User does not have the necessary role. Role found: " + currentRole);
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only users can release parking spaces.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
        if (parkingSpaceOptional.isPresent()) {
            ParkingSpace parkingSpace = parkingSpaceOptional.get();
            if (parkingSpace.getStatus() == ParkingSpace.Status.OCCUPIED) {
                parkingSpace.setStatus(ParkingSpace.Status.FREE);
                parkingSpaceService.save(parkingSpace);

                Optional<User> userOptional = userService.getUserByEmail(email);
                if (!userOptional.isPresent()) {
                    ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "User not found for email: " + email, System.currentTimeMillis());
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }

                List<ParkingHistory> userParkingHistory = parkingService.getParkingHistoryByUserId(userOptional.get().getId());
                ParkingHistory latestHistory = userParkingHistory.stream()
                        .filter(ph -> ph.getParkingSpaceId() == parkingSpace.getId() && ph.getEndTime() == null)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No active parking history found for the user"));

                parkingService.updateParkingHistoryEndTime(latestHistory);

                logger.info("Parking space released successfully: " + parkingSpace);
                ParkingSpaceDTO parkingSpaceDTO = new ParkingSpaceDTO(parkingSpace.getId(), parkingSpace.getStatus());
                return ResponseEntity.ok(parkingSpaceDTO);
            } else {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), "Parking space is already free.", System.currentTimeMillis());
                logger.error("Parking space is already free: " + parkingSpace);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } else {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Parking space not found for ID: " + id, System.currentTimeMillis());
            logger.error("Parking space not found for ID: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/history/user")
    public ResponseEntity<?> getUserParkingHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("No authentication found in SecurityContextHolder");
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: No authentication found.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        String email = authentication.getName();
        Optional<User> userOptional = userService.getUserByEmail(email);
        if (!userOptional.isPresent()) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "User not found for email: " + email, System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        List<ParkingHistory> parkingHistory = parkingService.getParkingHistoryByUserId(userOptional.get().getId());
        List<ParkingHistoryDTO> parkingHistoryDTOs = parkingHistory.stream()
                .map(history -> new ParkingHistoryDTO(
                        history.getParkingSpaceId(),
                        history.getUserId(),
                        history.getStartTime(),
                        history.getEndTime(),
                        userOptional.get().getUsername(),
                        userOptional.get().getEmail()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(parkingHistoryDTOs);
    }
}
