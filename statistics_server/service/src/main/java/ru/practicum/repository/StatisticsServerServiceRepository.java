package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsServerServiceRepository extends JpaRepository<Endpoint, Long> {

    @Query(value = "select new ru.practicum.ViewStatsDto(s.app, s.uri , count(s.ip)) from Endpoint s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "group by s.uri, s.app " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> findAllWithoutUrisAndNotUniqueIp(LocalDateTime before, LocalDateTime after);

    @Query(value = "select new ru.practicum.ViewStatsDto(s.app, s.uri , COUNT(s.ip)) from Endpoint s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "and s.uri in ?3 " +
            "group by s.uri, s.app " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> findAllWithUrisAndNotUniqueIp(LocalDateTime before, LocalDateTime after, List<String> uris);

    @Query(value = "select new ru.practicum.ViewStatsDto(s.app, s.uri , COUNT(distinct s.ip)) from Endpoint s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "group by s.uri, s.app " +
            "order by COUNT(distinct s.ip) desc")
    List<ViewStatsDto> findWithoutUrisAndUniqueIp(LocalDateTime before, LocalDateTime after);

    @Query(value = "select new ru.practicum.ViewStatsDto(s.app, s.uri , COUNT(distinct s.ip)) from Endpoint s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "and s.uri in ?3 " +
            "group by s.uri, s.app " +
            "order by COUNT(distinct s.ip) desc")
    List<ViewStatsDto> findWithUrisAndUniqueIp(LocalDateTime before, LocalDateTime after, List<String> uris);
}