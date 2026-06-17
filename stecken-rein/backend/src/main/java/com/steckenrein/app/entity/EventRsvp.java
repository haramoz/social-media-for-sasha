package com.steckenrein.app.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "event_rsvps",
    uniqueConstraints = @UniqueConstraint(columnNames = {"eventId", "userId"})
)
public class EventRsvp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private Long userId;

    @Column(nullable = false)
    private String status;

    private Instant updatedAt = Instant.now();

    public Long getId() { return id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public Instant getUpdatedAt() { return updatedAt; }
}