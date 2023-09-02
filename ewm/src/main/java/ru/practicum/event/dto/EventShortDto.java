package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public final class EventShortDto {
    private final Long id;
    private CategoryDto category;
    private final Long confirmedRequests;
    private final String annotation;
    private final UserShortDto initiator;
    private final Boolean paid;
    private final String title;
    private final Long views;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime eventDate;
}