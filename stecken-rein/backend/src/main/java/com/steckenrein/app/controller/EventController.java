package com.steckenrein.app.controller;

import com.steckenrein.app.dto.CreateEventRequest;
import com.steckenrein.app.dto.EventResponse;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.entity.NeighborhoodEvent;
import com.steckenrein.app.repository.AppUserRepository;
import com.steckenrein.app.repository.EventRsvpRepository;
import com.steckenrein.app.repository.NeighborhoodEventRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.steckenrein.app.dto.RsvpRequest;
import com.steckenrein.app.entity.EventRsvp;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final NeighborhoodEventRepository eventRepository;
    private final AppUserRepository userRepository;
    private final EventRsvpRepository rsvpRepository;
    

    public EventController(
        NeighborhoodEventRepository eventRepository,
        AppUserRepository userRepository,
        EventRsvpRepository rsvpRepository
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.rsvpRepository = rsvpRepository;
    }

    @GetMapping
    public List<EventResponse> getEvents(Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        return eventRepository.findAllByOrderByStartTimeAsc()
                .stream()
                .map(event -> toResponse(event, currentUser.getId()))
                .toList();
    }

    @PostMapping("/{eventId}/rsvp")
    public EventResponse rsvp(
            @PathVariable Long eventId,
            @RequestBody RsvpRequest request,
            Authentication authentication
    ) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        NeighborhoodEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        String status = request.status();

        if (!status.equals("GOING") && !status.equals("MAYBE") && !status.equals("NOT_GOING")) {
            throw new RuntimeException("Invalid RSVP status");
        }

        EventRsvp rsvp = rsvpRepository
                .findByEventIdAndUserId(eventId, currentUser.getId())
                .orElseGet(EventRsvp::new);

        rsvp.setEventId(eventId);
        rsvp.setUserId(currentUser.getId());
        rsvp.setStatus(status);

        rsvpRepository.save(rsvp);

        return toResponse(event, currentUser.getId());
    }

    @PostMapping
    public EventResponse createEvent(
            @RequestBody CreateEventRequest request,
            Authentication authentication
    ) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        NeighborhoodEvent event = new NeighborhoodEvent();
        event.setCreatedBy(currentUser.getId());
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setLocation(request.location());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());

        return toResponse(eventRepository.save(event), currentUser.getId());
    }

    private EventResponse toResponse(NeighborhoodEvent event, Long currentUserId) {
        AppUser creator = userRepository.findById(event.getCreatedBy()).orElse(null);

        String creatorName = creator == null
                ? "Unknown neighbor"
                : creator.getFirstName() + " " + creator.getLastName();

        var rsvps = rsvpRepository.findByEventId(event.getId());

        long goingCount = rsvps.stream().filter(r -> r.getStatus().equals("GOING")).count();
        long maybeCount = rsvps.stream().filter(r -> r.getStatus().equals("MAYBE")).count();
        long notGoingCount = rsvps.stream().filter(r -> r.getStatus().equals("NOT_GOING")).count();

        String myRsvp = rsvps.stream()
                .filter(r -> r.getUserId().equals(currentUserId))
                .map(EventRsvp::getStatus)
                .findFirst()
                .orElse(null);

        return new EventResponse(
                event.getId(),
                event.getCreatedBy(),
                creatorName,
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getStartTime(),
                event.getEndTime(),
                goingCount,
                maybeCount,
                notGoingCount,
                myRsvp
        );
    }
}