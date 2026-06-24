package com.steckenrein.app.dto;

import java.time.Instant;

public record PostResponse(
        Long id,
        Long authorId,
        String authorName,
        String text,
        String imagePath,
        Instant createdAt
) {}