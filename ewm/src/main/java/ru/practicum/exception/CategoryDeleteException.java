package ru.practicum.exception;

public class CategoryDeleteException extends RuntimeException {
    public CategoryDeleteException(String message) {
        super(message);
    }
}
