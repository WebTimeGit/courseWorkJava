package com.parking.management.controllers;

import com.parking.management.dto.ErrorResponseDTO;
import com.parking.management.dto.RegistrationDTO;
import com.parking.management.dto.LoginDTO;
import com.parking.management.dto.TokenDTO;
import com.parking.management.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", System.currentTimeMillis());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login/")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new TokenDTO(token));
        }
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
