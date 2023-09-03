package ru.practicum.disliked_events.service;

public interface DislikedEventsService {
    void deleteLike(long userId, long eventId);
}