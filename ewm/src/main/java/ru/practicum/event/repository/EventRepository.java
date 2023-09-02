package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Boolean existsByCategoryId(Long categoryId);

    List<Event> findAllByIdIn(List<Long> ids);

    default Optional<Event> findPublishedEventById(Long eventId) {
        return findAllByIdAndState(eventId, EventState.PUBLISHED);
    }

    Page<Event> findAllByInitiatorId(Long id, Pageable pageable);

    Optional<Event> findAllByIdAndState(Long eventId, EventState state);

    Optional<Event> findAllByIdAndInitiatorId(Long id, Long userId);

    @Query("SELECT e " +
            "FROM Event e " +
            "LEFT JOIN LikedEvents l ON e.id = l.event.id " +
            "LEFT JOIN DislikedEvents d ON e.id = d.event.id " +
            "GROUP BY e.id " +
            "ORDER BY (COUNT(l) - COUNT(d)) DESC")
    List<Event> findPopularEvents();
}