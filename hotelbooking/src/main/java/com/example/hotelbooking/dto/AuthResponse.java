package com.example.hotelbooking.dto;

public class AuthResponse {

    private String role;

    public AuthResponse(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
