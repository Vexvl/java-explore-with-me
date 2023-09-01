package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public final class UpdateEventDto {
    @Size(min = 20, max = 2000)
    private final String annotation;
    @Size(min = 3, max = 120)
    private final String title;
    @Size(min = 20, max = 7000)
    private final String description;
    private final Boolean paid;
    private final Integer participantLimit;
    private final Boolean requestModeration;
    private final String stateAction;
    private final Long category;
    private final Location location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime eventDate;
}