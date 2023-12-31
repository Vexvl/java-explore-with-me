package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.PublicParams;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Slf4j
public class EventPublicController {

    private final EventService eventsService;
    private final HttpServletRequest httpServletRequest;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsParams(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) List<Integer> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(name = "rangeStart", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                              @RequestParam(name = "rangeEnd", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                              @RequestParam(required = false) Boolean onlyAvailable,
                                              @RequestParam(required = false) String sort,
                                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {

        PublicParams publicParams = PublicParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sortParameter(sort)
                .from(from)
                .size(size)
                .ip(httpServletRequest.getRemoteAddr())
                .build();

        return eventsService.getEventsParams(publicParams);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable Long id) {
        return eventsService.getEventById(id, httpServletRequest.getRemoteAddr());
    }

    @GetMapping("/popular")
    public List<EventFullDto> getPopularEvents(@RequestParam(defaultValue = "10") int count) {
        return eventsService.getPopularEvents(count);
    }
}