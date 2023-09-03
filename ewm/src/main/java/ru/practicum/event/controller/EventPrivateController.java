package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.disliked_events.service.DislikedEventsService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.liked_events.service.LikedEventsService;
import ru.practicum.validator.EventStartBefore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@Slf4j
public class EventPrivateController {

    private final EventService eventsService;
    private final LikedEventsService likedEventsService;
    private final DislikedEventsService dislikedEventsService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId, @EventStartBefore(min = 2) @RequestBody @Valid NewEventDto newEventDto) {
        return eventsService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        return eventsService.getUserEvents(userId, from, size);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid @EventStartBefore(min = 2) UpdateEventDto updateEventDto) {
        return eventsService.updateEvent(userId, eventId, updateEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        return eventsService.getUserEventById(userId, eventId);
    }

    @PostMapping("/{userId}/events/{eventId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long userId, @PathVariable Long eventId) {
        likedEventsService.addLike(userId, eventId);
    }

    @DeleteMapping("/{userId}/events/{eventId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long userId, @PathVariable Long eventId) {
        dislikedEventsService.deleteLike(userId, eventId);
    }
}