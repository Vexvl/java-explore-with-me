package ru.practicum.exception;

public class EmailExistsEmail extends RuntimeException {

    public EmailExistsEmail(String message) {
        super(message);
    }
}