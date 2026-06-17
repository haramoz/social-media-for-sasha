package com.steckenrein.app.repository;

import com.steckenrein.app.entity.EventRsvp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRsvpRepository extends JpaRepository<EventRsvp, Long> {
    Optional<EventRsvp> findByEventIdAndUserId(Long eventId, Long userId);
    List<EventRsvp> findByEventId(Long eventId);
}