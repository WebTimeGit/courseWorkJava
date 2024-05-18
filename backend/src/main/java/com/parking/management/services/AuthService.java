package com.parking.management.services;

import com.parking.management.entities.User;
import com.parking.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, SequenceGeneratorService sequenceGeneratorService, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Реєстрація користувача.
     * @param username ім'я користувача
     * @param password пароль користувача
     * @param email електронна адреса користувача
     * @return токен JWT
     */
    public String registrationUser(String username, String password, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already taken");
        }

        User newUser = new User();
        newUser.setId(sequenceGeneratorService.generateSequence("users_seq"));
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setRole("USER");
        newUser.setRegistrationDate(LocalDateTime.now());

        userRepository.save(newUser);

        return jwtTokenService.createToken(email, "USER");
    }

    /**
     * Логін користувача.
     * @param email електронна адреса користувача
     * @param password пароль користувача
     * @return токен JWT або null, якщо аутентифікація не вдалася
     */
    public String loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return jwtTokenService.createToken(user.getEmail(), user.getRole());
            }
        }
        return null;
    }

    /**
     * Перевірка дійсності токена.
     * @param token JWT токен
     * @return true, якщо токен дійсний, false - якщо ні
     */
    public boolean isTokenValid(String token) {
        return jwtTokenService.validateToken(token);
    }
}
