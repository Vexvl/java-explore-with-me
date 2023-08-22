package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveEndpoint(@RequestBody EndpointDto endpointDto) {
        log.info("saveEndpoint");
        statisticsService.saveEndpoint(endpointDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getViewStats(
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("getViewStats");
        return statisticsService.getViewStats(start, end, uris, unique);
    }
}