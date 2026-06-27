package com.steckenrein.app.dto;

import java.time.Instant;

public record ChatMessageResponse(
        Long id,
        Long senderId,
        Long receiverId,
        String senderName,
        String text,
        String imagePath,
        Instant createdAt
) {}