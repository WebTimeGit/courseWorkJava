package com.parking.management.controllers;

import com.parking.management.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parking.management.dto.RegistrationDTO;
import com.parking.management.dto.LoginDTO;
import com.parking.management.dto.TokenDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration/")
    public ResponseEntity<?> registrationUser(@RequestBody RegistrationDTO registrationDTO) {
        try {
            String token = authService.registrationUser(registrationDTO.getUsername(), registrationDTO.getPassword(), registrationDTO.getEmail());
            return ResponseEntity.ok(new TokenDTO(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @PostMapping("/login/")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new TokenDTO(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
