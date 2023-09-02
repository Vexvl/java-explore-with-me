package ru.practicum.dislikedEvents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dislikedEvents.model.DislikedEvents;

public interface DislikedEventsRepository extends JpaRepository<DislikedEvents, Long> {
    Boolean existsByUserIdAndEventId(long userId, long eventId);

    void deleteByUserIdAndEventId(long userId, long eventId);
}