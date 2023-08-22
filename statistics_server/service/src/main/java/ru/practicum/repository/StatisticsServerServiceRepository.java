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

    @Query(value = "select new ru.practicum.ViewStatsDto(s.appName, s.appUri , count(s.ip)) from Endpoint s" +
            " where s.timestamp > ?1 " +
            "and s.timestamp < ?2 " +
            "group by s.appUri, s.appName " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> findAllWithoutUrisAndNotUniqueIp(LocalDateTime before, LocalDateTime after);

    @Query(value = "select new ru.practicum.ViewStatsDto(s.appName, s.appUri , COUNT(s.ip)) from Endpoint s" +
            " where s.timestamp > ?1 " +
            "and s.timestamp < ?2 " +
            "and s.appUri in ?3 " +
            "group by s.appUri, s.appName " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> findAllWithUrisAndNotUniqueIp(LocalDateTime before, LocalDateTime after, List<String> uris);

    @Query(value = "select new ru.practicum.ViewStatsDto(s.appName, s.appUri , COUNT(distinct s.ip)) from Endpoint s" +
            " where s.timestamp > ?1 " +
            "and s.timestamp < ?2 " +
            "group by s.appUri, s.appName " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> findWithoutUrisAndUniqueIp(LocalDateTime before, LocalDateTime after);

    @Query(value = "select new ru.practicum.ViewStatsDto(s.appName, s.appUri , COUNT(distinct s.ip)) from Endpoint s" +
            " where s.timestamp > ?1 " +
            "and s.timestamp < ?2 " +
            "and s.appUri in ?3 " +
            "group by s.appUri, s.appName " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> findWithUrisAndUniqueIp(LocalDateTime before, LocalDateTime after, List<String> uris);
}