package com.steckenrein.app.dto;

import java.time.LocalDateTime;

public record CreateEventRequest(
        String title,
        String description,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}