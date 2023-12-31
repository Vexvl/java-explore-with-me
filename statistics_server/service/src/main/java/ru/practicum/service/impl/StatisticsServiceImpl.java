package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.WrongTimeException;
import ru.practicum.mapper.EndpointMapper;
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
    @Transactional
    public void saveEndpoint(EndpointDto endpointDto) {
        statisticsServerServiceRepository.save(EndpointMapper.toEndpoint(endpointDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new WrongTimeException("Start after end time");
        }
        if (unique != null && unique) {
            if (uris.isEmpty()) {
                return statisticsServerServiceRepository.findStatsWithUris(start, end, null);
            } else {
                return statisticsServerServiceRepository.findStatsWithUris(start, end, uris);
            }
        } else {
            if (uris == null) {
                return statisticsServerServiceRepository.findStatsWithoutUris(start, end, null);
            } else {
                return statisticsServerServiceRepository.findStatsWithoutUris(start, end, uris);
            }
        }
    }
}