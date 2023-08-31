package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .build();
    }

    public Event updateEvent(UpdateEventDto updateEventDto, Event event, Category category, EventState state) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation() != null ?
                        updateEventDto.getAnnotation() : event.getAnnotation())
                .category(category)
                .paid(updateEventDto.getPaid() != null ?
                        updateEventDto.getPaid() : event.getPaid())
                .participantLimit(updateEventDto.getParticipantLimit() != null ?
                        updateEventDto.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(updateEventDto.getRequestModeration() != null ?
                        updateEventDto.getRequestModeration() : event.getRequestModeration())
                .state(state)
                .title(updateEventDto.getTitle() != null ?
                        updateEventDto.getTitle() : event.getTitle())
                .confirmedRequest(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .id(event.getId())
                .initiator(event.getInitiator())
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .description(updateEventDto.getDescription() != null ?
                        updateEventDto.getDescription() : event.getDescription())
                .eventDate(updateEventDto.getEventDate() != null ?
                        updateEventDto.getEventDate() : event.getEventDate())
                .longitude(updateEventDto.getLocation() != null ?
                        updateEventDto.getLocation().getLongitude() : event.getLongitude())
                .latitude(updateEventDto.getLocation() != null ?
                        updateEventDto.getLocation().getLatitude() : event.getLatitude())
                .build();
    }

    public Event toEvent(NewEventDto newEventDto, User user, Category category) {
        return Event.builder()
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .latitude(newEventDto.getLocation().getLatitude())
                .longitude(newEventDto.getLocation().getLongitude())
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .paid(newEventDto.getPaid())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(Location.builder()
                        .longitude(event.getLongitude())
                        .latitude(event.getLatitude())
                        .build())
                .paid(event.getPaid())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .views(event.getViews())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .build();
    }
}