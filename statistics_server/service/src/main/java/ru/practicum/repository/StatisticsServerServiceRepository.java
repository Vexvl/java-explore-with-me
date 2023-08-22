package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsServerServiceRepository extends JpaRepository<Endpoint, Long> {

    @Query("SELECT NEW ru.practicum.ViewStatsDto(s.appName, s.appUri, " +
            "COUNT(s.ip)) " +
            "FROM Endpoint s " +
            "WHERE s.timestamp > ?1 " +
            "AND s.timestamp < ?2 " +
            "AND (s.appUri IN ?3 OR ?3 IS NULL) " +
            "GROUP BY s.appUri, s.appName " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> findStatsWithoutUris(LocalDateTime before, LocalDateTime after, List<String> uris);

    @Query("SELECT NEW ru.practicum.ViewStatsDto(s.appName, s.appUri, " +
            "COUNT(DISTINCT s.ip)) " +
            "FROM Endpoint s " +
            "WHERE s.timestamp > ?1 " +
            "AND s.timestamp < ?2 " +
            "AND (s.appUri IN ?3 OR ?3 IS NULL) " +
            "GROUP BY s.appUri, s.appName " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> findStatsWithUris(LocalDateTime before, LocalDateTime after, List<String> uris);
}