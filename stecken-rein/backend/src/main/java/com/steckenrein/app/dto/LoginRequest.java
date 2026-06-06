package com.steckenrein.app.dto;

public record LoginRequest(
        String email,
        String password
) {}