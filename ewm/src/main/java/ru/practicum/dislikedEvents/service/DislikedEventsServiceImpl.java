package ru.practicum.dislikedEvents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dislikedEvents.mapper.DislikedEventsMapper;
import ru.practicum.dislikedEvents.repository.DislikedEventsRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AbsenceException;
import ru.practicum.exception.RatingAlreadyExistsException;
import ru.practicum.likedEvents.repository.LikedEventsRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class DislikedEventsServiceImpl implements DislikedEventsService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LikedEventsRepository likedEventsRepository;
    private final DislikedEventsRepository dislikedEventsRepository;

    @Override
    @Transactional
    public void deleteLike(long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AbsenceException("Event not exists"));

        if (dislikedEventsRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new RatingAlreadyExistsException("Dislike exists");
        } else if (likedEventsRepository.existsByUserIdAndEventId(userId, eventId)) {
            likedEventsRepository.deleteByUserIdAndEventId(userId, eventId);
        }
        dislikedEventsRepository.save(DislikedEventsMapper.toDislikedEvents(user, event));
    }
}