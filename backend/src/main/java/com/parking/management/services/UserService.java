package com.parking.management.services;

import com.parking.management.entities.User;
import com.parking.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;


    @Autowired
    public UserService(UserRepository userRepository , JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    // Отримання користувача з токена
    public Optional<User> getUserFromToken(String token) {
        String email = jwtTokenService.getUsernameFromToken(token);
        return userRepository.findByEmail(email);
    }
}
