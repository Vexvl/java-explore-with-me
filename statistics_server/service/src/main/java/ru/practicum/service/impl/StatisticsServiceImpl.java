package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.repository.StatRepo;
import ru.practicum.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final StatRepo statisticsServiceRepository;


    @Override
    public void saveEndpoint(EndpointDto endpointDto) {

    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return null;
    }
}