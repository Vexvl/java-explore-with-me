package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Endpoint;

public interface StatisticsServerServiceRepository extends JpaRepository<Endpoint, Long> {

}