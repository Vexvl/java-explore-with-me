package ru.practicum.likedEvents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.likedEvents.model.LikedEvents;

public interface LikedEventsRepository extends JpaRepository<LikedEvents, Long> {

    Boolean existsByUserIdAndEventId(long userId, long eventId);

    void deleteByUserIdAndEventId(long userId, long eventId);
}