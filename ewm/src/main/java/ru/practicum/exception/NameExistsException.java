package ru.practicum.exception;

public class NameExistsException extends RuntimeException {

    public NameExistsException(String message) {
        super(message);
    }
}