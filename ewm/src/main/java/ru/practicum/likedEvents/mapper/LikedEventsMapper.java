package ru.practicum.likedEvents.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Event;
import ru.practicum.likedEvents.model.LikedEvents;
import ru.practicum.user.model.User;

@UtilityClass
public class LikedEventsMapper {
    public LikedEvents toLikedEvents(User user, Event event) {
        return LikedEvents.builder()
                .user(user)
                .event(event)
                .build();
    }
}