package ru.practicum.disliked_events.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.disliked_events.model.DislikedEvents;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

@UtilityClass
public class DislikedEventsMapper {
    public DislikedEvents toDislikedEvents(User user, Event event) {
        return DislikedEvents.builder()
                .user(user)
                .event(event)
                .build();
    }
}