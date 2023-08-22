package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsServerServiceRepository extends JpaRepository<Endpoint, Long> {

    List<ViewStatsDto> findStatsByTimestampBetweenAndUriInOrderByIpDesc(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<ViewStatsDto> findStatsByTimestampBetweenOrderByIpDesc(LocalDateTime start, LocalDateTime end);

    List<ViewStatsDto> findUniqueStatsByTimestampBetweenAndUriInOrderByIpDesc(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<ViewStatsDto> findUniqueStatsByTimestampBetweenOrderByIpDesc(LocalDateTime start, LocalDateTime end);
}