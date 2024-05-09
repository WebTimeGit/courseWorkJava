package com.parking.management.controllers;

import com.parking.management.dto.UserProfileUpdateDTO;
import com.parking.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileUpdateController {

    @Autowired
    private UserService userService;

    @PatchMapping("/profile/{userId}")
    public ResponseEntity<?> userProfileUpdate(@PathVariable Long userId, @RequestBody UserProfileUpdateDTO request) {
        System.out.println("Received update request for userId: " + userId + " with data: " + request);
        try {
            userService.userProfileUpdate(userId, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Error during profile update: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
