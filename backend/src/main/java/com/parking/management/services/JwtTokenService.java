package com.parking.management.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenService {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Генерація безпечного ключа
    private final long validityInMilliseconds = 3600000; // 1 година

    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role); // Зберігає роль користувача

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        logger.info("Decoding token: " + token); // Логування токена перед декодуванням
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token.trim())
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token.trim()).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token.trim());
        String role = (String) claims.get("role");
        logger.info("Role from token: " + role); // Логування ролі
        return role;
    }
}
