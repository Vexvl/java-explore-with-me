package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.AdminParams;
import ru.practicum.event.model.PublicParams;

import java.util.List;

public interface EventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    List<EventFullDto> getEventsAdminParams(AdminParams adminParams);

    List<EventFullDto> getEventsParams(PublicParams publicParams);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getEventById(Long id, String ip);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventDto updateEventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventDto updateEventDto);

    List<EventFullDto> getPopularEvents(int count);
}