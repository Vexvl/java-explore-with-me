package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}