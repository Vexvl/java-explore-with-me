package ru.practicum.service;

import ru.practicum.EndpointDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    void saveEndpoint(EndpointDto endpointDto);

    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}