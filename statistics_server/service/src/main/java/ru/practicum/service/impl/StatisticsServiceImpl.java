package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointMapper;
import ru.practicum.model.Endpoint;
import ru.practicum.repository.StatisticsServerServiceRepository;
import ru.practicum.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsServerServiceRepository statisticsServerServiceRepository;

    @Override
    public void saveEndpoint(EndpointDto endpointHitDto) {
        Endpoint endpointHit = EndpointMapper.toEndpoint(endpointHitDto);
        statisticsServerServiceRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            if (uris.isEmpty()) {
                return statisticsServerServiceRepository.findWithoutUrisAndUniqueIp(start, end);
            } else {
                return statisticsServerServiceRepository.findWithUrisAndUniqueIp(start, end, uris);
            }
        } else {
            if (uris.isEmpty()) {
                return statisticsServerServiceRepository.findAllWithoutUrisAndNotUniqueIp(start, end);
            } else {
                return statisticsServerServiceRepository.findAllWithUrisAndNotUniqueIp(start, end, uris);
            }
        }
    }
}