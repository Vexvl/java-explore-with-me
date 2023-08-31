package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.EndpointDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.criteria.EventCriteria;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.EventSpecification;
import ru.practicum.exception.AbsenceException;
import ru.practicum.exception.EventWrongTimeException;
import ru.practicum.exception.WrongEventStateException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final static String APP = "EwmService";
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Client client;

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exist"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new AbsenceException("Category not exist"));
        Event event = EventMapper.toEvent(newEventDto, user, category);
        event.setState(EventState.PENDING);
        event.setConfirmedRequest(0L);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsAdminParams(List<Long> users, List<String> states, List<Integer> categories,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Integer from, Integer size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new EventWrongTimeException("Wrong event time");
        }
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from / size, size);

        List<EventState> convertStates = null;
        if (states != null) {
            convertStates = states.stream()
                    .map(EventState::valueOf)
                    .collect(Collectors.toList());
        }

        EventCriteria criteria = EventCriteria.builder()
                .users(users)
                .states(convertStates)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        EventSpecification eventSpecification = new EventSpecification(criteria);
        Page<Event> eventsPage = eventRepository.findAll(eventSpecification, pageable);

        Map<Long, Long> eventViews = getEventsViews(eventsPage.getContent());

        List<Event> eventsWithHits = eventsPage.getContent().stream()
                .map(event -> {
                    event.setViews(eventViews.getOrDefault(event.getId(), 0L));
                    return event;
                })
                .collect(Collectors.toList());

        return eventsWithHits.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> userEventsPage = eventRepository.findAllByInitiatorId(userId, pageable);
        List<Event> userEvents = userEventsPage.getContent();

        Map<Long, Long> eventViews = getEventsViews(userEvents);

        userEvents.forEach(event -> event.setViews(eventViews.getOrDefault(event.getId(), 0L)));

        return userEvents.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long id, String ip) {
        Event event = eventRepository.findPublishedEventById(id)
                .orElseThrow(() -> new AbsenceException("Published events not exists"));

        client.saveEndpoint(EndpointDto.builder()
                .app(APP)
                .uri("/events/" + id)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build());

        Map<Long, Long> eventViews = getEventsViews(List.of(event));
        event.setViews(eventViews.getOrDefault(id, 0L));

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventDto updateEventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AbsenceException("Event not exists"));
        if (event.getState().equals(EventState.PUBLISHED) || event.getState().equals(EventState.REJECT_EVENT)) {
            throw new WrongEventStateException("WrongEventStateException");
        }
        Category category = event.getCategory();
        if (updateEventDto.getCategory() != null) {
            category = categoryRepository.findById(updateEventDto.getCategory()).orElseThrow(() -> new AbsenceException("Category not exists"));
        }
        EventState state = event.getState();
        if (updateEventDto.getStateAction() != null) {
            AdminRequestState adminRequestState = AdminRequestState.valueOf(updateEventDto.getStateAction());
            switch (adminRequestState) {
                case PUBLISH_EVENT:
                    state = EventState.PUBLISHED;
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    state = EventState.REJECT_EVENT;
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.updateEvent(updateEventDto, event, category, state)));
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        Event event = eventRepository.findAllByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new AbsenceException("Events not exists"));

        Map<Long, Long> eventViews = getEventsViews(List.of(event));

        event.setViews(eventViews.getOrDefault(event.getId(), 0L));

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        Event event = eventRepository.findAllByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new AbsenceException("Events not exists"));
        if (!(event.getState().equals(EventState.PENDING) || event.getState().equals(EventState.REJECT_EVENT))) {
            throw new WrongEventStateException("WrongEventStateException");
        }
        Category patchCategory = event.getCategory();
        if (updateEventDto.getCategory() != null) {
            patchCategory = categoryRepository.findById(updateEventDto.getCategory())
                    .orElseThrow(() -> new AbsenceException("Category not exists"));
        }
        EventState state = event.getState();
        if (updateEventDto.getStateAction() != null) {
            try {
                UserRequestState updateState = UserRequestState.valueOf(updateEventDto.getStateAction());
                switch (updateState) {
                    case CANCEL_REVIEW:
                        state = EventState.CANCELED;
                        break;
                    case SEND_TO_REVIEW:
                        state = EventState.PENDING;
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Not correct event State in query");
            }
        }
        return EventMapper.toEventFullDto(eventRepository.save(
                EventMapper.updateEvent(updateEventDto, event, patchCategory, state)));
    }

    @Override
    public List<EventFullDto> getEventsParams(String text, List<Integer> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, String sort, Integer from, Integer size, String ip) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new EventWrongTimeException("Wrong event time");
        }
        client.saveEndpoint(EndpointDto.builder()
                .app(APP)
                .uri("/events")
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build());
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        EventCriteria criteria = EventCriteria.builder()
                .states(List.of(EventState.PUBLISHED))
                .text(text)
                .categories(categories)
                .rangeEnd(rangeEnd)
                .rangeStart(rangeStart)
                .onlyAvailable(onlyAvailable)
                .sortParam(sort != null ? SortParameter.valueOf(sort) : null)
                .build();
        EventSpecification eventSpecification = new EventSpecification(criteria);

        Page<Event> eventsPage = eventRepository.findAll(eventSpecification, pageable);
        Map<Long, Long> eventViews = getEventsViews(eventsPage.getContent());

        List<Event> eventsWithHits = eventsPage.getContent().stream()
                .map(event -> {
                    event.setViews(eventViews.getOrDefault(event.getId(), 0L));
                    return event;
                })
                .collect(Collectors.toList());

        return eventsWithHits.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    private Map<Long, Long> getEventsViews(List<Event> events) {
        if (events.isEmpty()) {
            return Collections.emptyMap();
        }

        List<String> uris = events.stream()
                .map(event -> String.format("/events/%d", event.getId()))
                .collect(Collectors.toList());

        List<ViewStatsDto> stats = getClientViewStats(uris);

        return stats.stream()
                .collect(Collectors.toMap(
                        viewStatsDto -> extractEventIdFromUri(viewStatsDto.getUri()),
                        ViewStatsDto::getHits
                ));
    }

    private List<ViewStatsDto> getClientViewStats(List<String> uris) {
        try {
            return client.getViewStats(
                    LocalDateTime.now().minusMinutes(1),
                    LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(1),
                    uris,
                    true
            );
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Long extractEventIdFromUri(String uri) {
        String[] uriElements = uri.split("/");
        return Long.parseLong(uriElements[uriElements.length - 1]);
    }
}