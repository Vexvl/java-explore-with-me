package ru.practicum.validator;

import ru.practicum.event.dto.UpdateEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventStartTimeValidator implements ConstraintValidator<EventStartBefore, UpdateEventDto> {
    EventStartBefore check;

    @Override
    public void initialize(EventStartBefore check) {
        this.check = check;
    }

    @Override
    public boolean isValid(UpdateEventDto updateEventDto, ConstraintValidatorContext ctx) {
        if (updateEventDto.getEventDate() != null) {
            return (updateEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(check.min())));
        } else {
            return true;
        }
    }
}