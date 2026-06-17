package com.steckenrein.app.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        Long createdBy,
        String creatorName,
        String title,
        String description,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime,
        long goingCount,
        long maybeCount,
        long notGoingCount,
        String myRsvp
) {}