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
        try {
            String email = jwtTokenService.getUsernameFromToken(token);
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("Error getting user from token", e);
            return Optional.empty();
        }
    }

    // Отримання користувача за email
    public Optional<User> getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("Error getting user by email", e);
            return Optional.empty();
        }
    }

    // Отримання користувача за ID
    public Optional<User> findById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error getting user by ID", e);
            return Optional.empty();
        }
    }

    // Оновлення профілю користувача
    public void userProfileUpdate(Long userId, UserProfileUpdateDTO userProfile) throws Exception {
        Optional<User> userOptional;
        try {
            userOptional = userRepository.findById(userId);
        } catch (Exception e) {
            logger.error("Error finding user by ID for update", e);
            throw new Exception("Error finding user by ID for update", e);
        }

        if (!userOptional.isPresent()) {
            throw new Exception("User with ID " + userId + " not found");
        }

        User user = userOptional.get();
        user.setUsername(userProfile.getUsername());
        user.setEmail(userProfile.getEmail());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error saving updated user profile", e);
            throw new Exception("Error saving updated user profile", e);
        }
    }

    // Перевірка, чи є користувач адміністратором
    public boolean isAdmin(String token) {
        try {
            String role = jwtTokenService.getRoleFromToken(token);
            logger.info("Role from token: " + role); // Логування ролі
            boolean isAdmin = "ADMIN".equals(role);
            logger.info("Is admin: " + isAdmin); // Логування результату перевірки
            return isAdmin;
        } catch (Exception e) {
            logger.error("Error checking if user is admin", e);
            return false;
        }
    }
}
