package com.parking.management.controllers;

import com.parking.management.dto.ErrorResponseDTO;
import com.parking.management.dto.UserInfoDTO;
import com.parking.management.entities.User;
import com.parking.management.services.AuthService;
import com.parking.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/userInfo/")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        token = token.substring(7);

        if (!authService.isTokenValid(token)) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid token.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        Optional<User> userOptional = userService.getUserFromToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserInfoDTO userInfo = new UserInfoDTO(
                    user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getRegistrationDate()
            );
            return ResponseEntity.ok(userInfo);
        } else {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "User not found.", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}
