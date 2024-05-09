package com.parking.management.dto;

public class UserProfileUpdateDTO {
    private String username;
    private String email;


    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) { this.username = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
