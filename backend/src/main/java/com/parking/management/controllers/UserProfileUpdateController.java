package com.parking.management.controllers;

import com.parking.management.dto.ErrorResponseDTO;
import com.parking.management.dto.UserProfileUpdateDTO;
import com.parking.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserProfileUpdateController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileUpdateController.class);

    @Autowired
    private UserService userService;

    @PatchMapping("/profile/{userId}")
    public ResponseEntity<?> userProfileUpdate(@PathVariable Long userId, @RequestBody UserProfileUpdateDTO request) {
        logger.info("Received update request for userId: " + userId + " with data: " + request);
        try {
            userService.userProfileUpdate(userId, request);
            logger.info("User profile updated successfully for userId: " + userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error during profile update for userId: " + userId + ", error: " + e.getMessage(), e);
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
