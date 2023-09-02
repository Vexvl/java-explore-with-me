package ru.practicum.event.model;

import java.util.Optional;

public enum EventState {
    PENDING, PUBLISHED, CANCELED, REJECT_EVENT;

    public static Optional<EventState> from(String stringState) {
        for (EventState eventState : values()) {
            if (eventState.name().equalsIgnoreCase(stringState)) {
                return Optional.of(eventState);
            }
        }
        return Optional.empty();
    }
}