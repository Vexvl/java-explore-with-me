package ru.practicum.exception;

public class WrongEventStateException extends RuntimeException {

    public WrongEventStateException(String message) {
        super(message);
    }
}