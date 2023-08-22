package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsServerServiceRepository extends JpaRepository<Endpoint, Long> {

    List<ViewStatsDto> findStatsByRequestTimeBetweenAndUriInOrderByIpDesc(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<ViewStatsDto> findStatsByRequestTimeBetweenOrderByIpDesc(LocalDateTime start, LocalDateTime end);

    List<ViewStatsDto> findUniqueStatsByRequestTimeBetweenAndUriInOrderByIpDesc(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<ViewStatsDto> findUniqueStatsByRequestTimeBetweenOrderByIpDesc(LocalDateTime start, LocalDateTime end);
}