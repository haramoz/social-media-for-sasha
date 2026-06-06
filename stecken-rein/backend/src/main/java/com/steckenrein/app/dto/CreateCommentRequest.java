package com.steckenrein.app.dto;

public record CreateCommentRequest(
        Long authorId,
        String text
) {}