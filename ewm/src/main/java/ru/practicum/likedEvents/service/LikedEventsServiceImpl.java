package ru.practicum.likedEvents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dislikedEvents.repository.DislikedEventsRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AbsenceException;
import ru.practicum.exception.RatingAlreadyExistsException;
import ru.practicum.likedEvents.mapper.LikedEventsMapper;
import ru.practicum.likedEvents.repository.LikedEventsRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class LikedEventsServiceImpl implements LikedEventsService {
    private final LikedEventsRepository likedEventsRepository;
    private final DislikedEventsRepository dislikedEventsRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void addLike(long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AbsenceException("Event not exists"));

        if (likedEventsRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new RatingAlreadyExistsException("Like exists");
        } else if (dislikedEventsRepository.existsByUserIdAndEventId(userId, eventId)) {
            dislikedEventsRepository.deleteByUserIdAndEventId(userId, eventId);
        }
        likedEventsRepository.save(LikedEventsMapper.toLikedEvents(user, event));
    }
}