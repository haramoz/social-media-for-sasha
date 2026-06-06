package com.steckenrein.app.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        Long postId,
        Long authorId,
        String authorName,
        String text,
        Instant createdAt
) {}