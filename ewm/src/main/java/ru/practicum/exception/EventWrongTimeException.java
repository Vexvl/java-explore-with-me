package ru.practicum.exception;

public class EventWrongTimeException extends RuntimeException {
    public EventWrongTimeException(String message) {
        super(message);
    }
}