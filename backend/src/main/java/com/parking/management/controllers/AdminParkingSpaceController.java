package com.parking.management.controllers;

import com.parking.management.dto.AdminParkingSpaceDTO;
import com.parking.management.dto.ErrorResponseDTO;
import com.parking.management.dto.ParkingHistoryDTO;
import com.parking.management.dto.ParkingSpaceCreateDTO;
import com.parking.management.dto.ParkingSpaceUpdateDTO;
import com.parking.management.entities.ParkingHistory;
import com.parking.management.entities.ParkingSpace;
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
@RequestMapping("/api/admin/parkingspaces")
public class AdminParkingSpaceController {

    private static final Logger logger = LoggerFactory.getLogger(AdminParkingSpaceController.class);

    private final ParkingSpaceService parkingSpaceService;
    private final UserService userService;
    private final ParkingService parkingService;

    @Autowired
    public AdminParkingSpaceController(ParkingSpaceService parkingSpaceService, UserService userService, ParkingService parkingService) {
        this.parkingSpaceService = parkingSpaceService;
        this.userService = userService;
        this.parkingService = parkingService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AdminParkingSpaceDTO>> getAllParkingSpaces() {
        List<AdminParkingSpaceDTO> parkingSpaces = parkingSpaceService.findAll().stream()
                .map(space -> {
                    Optional<User> userOptional = userService.findById(space.getOccupiedByUserId());
                    String username = userOptional.map(User::getUsername).orElse(null);
                    String email = userOptional.map(User::getEmail).orElse(null);
                    return new AdminParkingSpaceDTO(
                            space.getId(),
                            space.getStatus(),
                            space.getOccupiedByUserId(),
                            username,
                            email,
                            space.getServiceByAdminId(),
                            space.getServiceReason()
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(parkingSpaces);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createParkingSpace(@RequestHeader("Authorization") String token, @RequestBody ParkingSpaceCreateDTO parkingSpaceCreateDTO) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.info("Token received: " + token);  // Логування токена
            if (userService.isAdmin(token)) {
                ParkingSpace parkingSpace = new ParkingSpace();
                parkingSpace.setStatus(ParkingSpace.Status.valueOf(parkingSpaceCreateDTO.getStatus()));
                ParkingSpace createdSpace = parkingSpaceService.save(parkingSpace);
                logger.info("Parking space created successfully: " + createdSpace);
                return ResponseEntity.ok(createdSpace);
            } else {
                logger.error("Access denied: Only admins can create parking spaces.");
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only admins can create parking spaces.", System.currentTimeMillis());
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        }
        logger.error("Unauthorized: Invalid token.");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateParkingSpace(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody ParkingSpaceUpdateDTO parkingSpaceUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("No authentication found in SecurityContextHolder");
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: No authentication found.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        String currentRole = authentication.getAuthorities().iterator().next().getAuthority();
        logger.debug("Current role: " + currentRole);

        if (!"ROLE_ADMIN".equals(currentRole)) {
            logger.error("User does not have the necessary role. Role found: " + currentRole);
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: You don't have the necessary role to update parking spaces.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
        if (parkingSpaceOptional.isPresent()) {
            ParkingSpace existingParkingSpace = parkingSpaceOptional.get();
            ParkingSpace.Status oldStatus = existingParkingSpace.getStatus();
            ParkingSpace.Status newStatus = ParkingSpace.Status.valueOf(parkingSpaceUpdateDTO.getStatus());
            existingParkingSpace.setStatus(newStatus);

            if (newStatus == ParkingSpace.Status.FREE) {
                if (oldStatus == ParkingSpace.Status.OCCUPIED) {
                    // Оновлення часу звільнення у історії паркування
                    List<ParkingHistory> parkingHistoryList = parkingService.getParkingHistoryByParkingSpaceId(existingParkingSpace.getId());
                    Optional<ParkingHistory> latestHistoryOptional = parkingHistoryList.stream()
                            .filter(ph -> ph.getEndTime() == null)
                            .findFirst();

                    if (latestHistoryOptional.isPresent()) {
                        ParkingHistory latestHistory = latestHistoryOptional.get();
                        latestHistory.setEndTime(LocalDateTime.now());
                        parkingService.updateParkingHistoryEndTime(latestHistory);
                    }
                }
                existingParkingSpace.setOccupiedByUserId(null);
                existingParkingSpace.setServiceByAdminId(null);
                existingParkingSpace.setServiceReason(null);
            } else if (newStatus == ParkingSpace.Status.SERVICE) {
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    existingParkingSpace.setServiceReason(parkingSpaceUpdateDTO.getServiceReason());
                    existingParkingSpace.setServiceByAdminId(userService.getUserFromToken(token).orElseThrow(() -> new RuntimeException("Admin user not found")).getId());
                }
            } else if (newStatus == ParkingSpace.Status.OCCUPIED && oldStatus == ParkingSpace.Status.SERVICE) {
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    existingParkingSpace.setOccupiedByUserId(userService.getUserFromToken(token).orElseThrow(() -> new RuntimeException("User not found")).getId());
                }
            }

            ParkingSpace updatedParkingSpace = parkingSpaceService.save(existingParkingSpace);
            logger.info("Parking space updated successfully: " + updatedParkingSpace);

            Optional<User> userOptional = userService.findById(existingParkingSpace.getOccupiedByUserId());
            String username = userOptional.map(User::getUsername).orElse(null);
            String email = userOptional.map(User::getEmail).orElse(null);

            AdminParkingSpaceDTO adminParkingSpaceDTO = new AdminParkingSpaceDTO(
                    updatedParkingSpace.getId(),
                    updatedParkingSpace.getStatus(),
                    updatedParkingSpace.getOccupiedByUserId(),
                    username,
                    email,
                    updatedParkingSpace.getServiceByAdminId(),
                    updatedParkingSpace.getServiceReason()
            );

            return ResponseEntity.ok(adminParkingSpaceDTO);
        } else {
            logger.error("Parking space not found for ID: " + id);
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Parking space not found for ID: " + id, System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteParkingSpace(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.info("Token received: " + token);  // Логування токена
            if (userService.isAdmin(token)) {
                Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
                if (parkingSpaceOptional.isPresent()) {
                    parkingSpaceService.deleteById(id);
                    logger.info("Parking space deleted successfully: ID " + id);
                    return ResponseEntity.ok().build();
                } else {
                    logger.error("Parking space not found for ID: " + id);
                    ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Parking space not found.", System.currentTimeMillis());
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }
            } else {
                logger.error("Access denied: Only admins can delete parking spaces.");
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only admins can delete parking spaces.", System.currentTimeMillis());
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        }
        logger.error("Unauthorized: Invalid token.");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/history/space/{id}")
    public ResponseEntity<?> getParkingSpaceHistory(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.info("Token received: " + token);  // Логування токена

            if (userService.isAdmin(token)) {
                Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceService.findById(id);
                if (!parkingSpaceOptional.isPresent()) {
                    logger.error("Parking space not found for ID: " + id);
                    ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Parking space not found for ID: " + id, System.currentTimeMillis());
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }

                List<ParkingHistory> parkingHistory = parkingService.getParkingHistoryByParkingSpaceId(id);
                List<ParkingHistoryDTO> parkingHistoryDTOs = parkingHistory.stream()
                        .map(history -> {
                            Optional<User> userOptional = userService.findById(history.getUserId());
                            String username = userOptional.map(User::getUsername).orElse("Unknown");
                            String email = userOptional.map(User::getEmail).orElse("Unknown");
                            return new ParkingHistoryDTO(history.getParkingSpaceId(), history.getUserId(), history.getStartTime(), history.getEndTime(), username, email);
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(parkingHistoryDTOs);
            } else {
                logger.error("Access denied: Only admins can view parking space history.");
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only admins can view parking space history.", System.currentTimeMillis());
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        }
        logger.error("Unauthorized: Invalid token.");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
