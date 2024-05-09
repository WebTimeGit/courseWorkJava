package com.parking.management.services;

import com.parking.management.dto.UserProfileUpdateDTO;
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

    public void userProfileUpdate(Long userId, UserProfileUpdateDTO userProfile) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new Exception("User with ID " + userId + " not found");
        }
        User user = userOptional.get();
        user.setUsername(userProfile.getUsername());
        user.setEmail(userProfile.getEmail());
        userRepository.save(user);
    }
}
