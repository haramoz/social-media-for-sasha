package com.steckenrein.app.dto;

public record LoginResponse(
        String token,
        Long id,
        String firstName,
        String email
) {}