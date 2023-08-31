package ru.practicum.exception;

public class ParticipationRequestFailException extends RuntimeException{
    public ParticipationRequestFailException(String message) {
        super(message);
    }
}