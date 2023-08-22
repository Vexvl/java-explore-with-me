package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointDto;
import ru.practicum.ViewStatsDto;
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
    public void saveEndpoint(EndpointDto endpointDto) {
        statisticsServerServiceRepository.save(EndpointMapper.toEndpoint(endpointDto));
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique != null && unique) {
            if (uris != null && !uris.isEmpty()) {
                return statisticsServerServiceRepository.findUniqueStatsByTimestampBetweenAndAppNameInOrderByIpDesc(start, end, uris);
            } else {
                return statisticsServerServiceRepository.findUniqueStatsByTimestampBetweenOrderByIpDesc(start, end);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                return statisticsServerServiceRepository.findStatsByTimestampBetweenAndAppNameInOrderByIpDesc(start, end, uris);
            } else {
                return statisticsServerServiceRepository.findStatsByTimestampBetweenOrderByIpDesc(start, end);
            }
        }
    }
}