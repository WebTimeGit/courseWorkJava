package com.parking.management.controllers;

import com.parking.management.entities.User;
import com.parking.management.services.UserService;
import com.parking.management.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parking.management.dto.UserInfoDTO;

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
    public ResponseEntity<UserInfoDTO> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            if (authService.isTokenValid(token)) {
                Optional<User> userOptional = userService.getUserFromToken(token);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    UserInfoDTO userInfo = new UserInfoDTO(
                            user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getRegistrationDate()
                    );
                    return ResponseEntity.ok(userInfo);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
