package ru.practicum.disliked_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.disliked_events.model.DislikedEvents;

public interface DislikedEventsRepository extends JpaRepository<DislikedEvents, Long> {
    Boolean existsByUserIdAndEventId(long userId, long eventId);

    void deleteByUserIdAndEventId(long userId, long eventId);
}