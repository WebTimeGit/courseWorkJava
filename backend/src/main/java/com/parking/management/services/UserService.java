package com.parking.management.services;

import com.parking.management.dto.UserProfileUpdateDTO;
import com.parking.management.entities.User;
import com.parking.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;


    @Autowired
    public UserService(UserRepository userRepository, JwtTokenService jwtTokenService) {
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

    public boolean isAdmin(String token) {
        String role = jwtTokenService.getRoleFromToken(token);
        logger.info("Role from token: " + role); // Логування ролі
        boolean isAdmin = "ADMIN".equals(role);
        logger.info("Is admin: " + isAdmin); // Логування результату перевірки
        return isAdmin;
    }
}
