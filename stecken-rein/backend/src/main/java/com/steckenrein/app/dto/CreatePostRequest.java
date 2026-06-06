package com.steckenrein.app.dto;

public record CreatePostRequest(
        Long authorId,
        String text
) {}