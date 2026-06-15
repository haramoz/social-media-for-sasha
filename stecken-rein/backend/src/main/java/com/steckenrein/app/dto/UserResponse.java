package com.steckenrein.app.dto;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String role,
        boolean approved
) {}