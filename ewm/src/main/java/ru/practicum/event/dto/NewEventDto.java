package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.event.model.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public final class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120)
    private final String title;
    @NotBlank
    @Size(min = 20, max = 2000)
    private final String annotation;
    @NotBlank
    @Size(min = 20, max = 7000)
    private final String description;
    private final Long category;
    private final Boolean paid = false;
    private final Location location;
    private final Integer participantLimit = 0;
    private final Boolean requestModeration = true;

    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime eventDate;
}