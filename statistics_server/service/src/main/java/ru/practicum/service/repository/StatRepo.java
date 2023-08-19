package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepo extends JpaRepository<Endpoint, Long> {

}