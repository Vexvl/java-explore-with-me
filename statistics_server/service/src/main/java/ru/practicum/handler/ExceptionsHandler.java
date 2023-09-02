package ru.practicum.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.WrongTimeException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(WrongTimeException.class)
    public ResponseEntity<String> handleWrongTimeException(WrongTimeException e) {
        return new ResponseEntity<>("WrongTimeException", HttpStatus.BAD_REQUEST);
    }
}