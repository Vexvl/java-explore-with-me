package ru.practicum.validator;

import ru.practicum.event.dto.NewEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventStartValidator implements ConstraintValidator<EventStartBefore, NewEventDto> {
    EventStartBefore check;

    @Override
    public void initialize(EventStartBefore check) {
        this.check = check;
    }

    @Override
    public boolean isValid(NewEventDto newEventDto, ConstraintValidatorContext ctx) {
        return (newEventDto.getEventDate() != null &&
                newEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(check.min())));
    }
}