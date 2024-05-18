package com.parking.management.controllers;

import com.parking.management.dto.ErrorResponseDTO;
import com.parking.management.dto.UserParkingHistoryDTO;
import com.parking.management.entities.ParkingHistory;
import com.parking.management.entities.User;
import com.parking.management.services.ParkingService;
import com.parking.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private final UserService userService;
    private final ParkingService parkingService;

    @Autowired
    public AdminUserController(UserService userService, ParkingService parkingService) {
        this.userService = userService;
        this.parkingService = parkingService;
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getUserParkingHistory(@RequestHeader("Authorization") String token, @PathVariable Long userId) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.info("Token received: " + token);  // Логування токена

            if (userService.isAdmin(token)) {
                Optional<User> userOptional = userService.findById(userId);
                if (userOptional.isPresent()) {
                    List<ParkingHistory> parkingHistory = parkingService.getParkingHistoryByUserId(userId);
                    List<UserParkingHistoryDTO> parkingHistoryDTOs = parkingHistory.stream()
                            .map(history -> new UserParkingHistoryDTO(
                                    history.getParkingSpaceId(),
                                    history.getUserId(),
                                    history.getStartTime(),
                                    history.getEndTime(),
                                    userOptional.get().getUsername(),
                                    userOptional.get().getEmail()
                            ))
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(parkingHistoryDTOs);
                } else {
                    logger.error("User not found for ID: " + userId);
                    ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "User not found for ID: " + userId, System.currentTimeMillis());
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }
            } else {
                logger.error("Access denied: Only admins can view user parking history.");
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Forbidden: Only admins can view user parking history.", System.currentTimeMillis());
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        }
        logger.error("Unauthorized: Invalid token.");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
