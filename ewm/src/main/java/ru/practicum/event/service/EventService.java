package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    List<EventFullDto> getEventsAdminParams(List<Long> users,
                                            List<String> states,
                                            List<Integer> categories,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Integer from,
                                            Integer size);

    List<EventFullDto> getEventsParams(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        Integer from,
                                        Integer size,
                                        String ip);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getEventById(Long id, String ip);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventDto updateEventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventDto updateEventDto);
}