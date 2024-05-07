package com.parking.management.dto;

import java.time.LocalDateTime;

public class UserInfoDTO {
    private long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime registrationDate;

    public UserInfoDTO(long id, String username, String email, String role, LocalDateTime registrationDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.registrationDate = registrationDate;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}
