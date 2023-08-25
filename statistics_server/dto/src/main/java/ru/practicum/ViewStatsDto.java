package ru.practicum;

import lombok.*;

@Data
@AllArgsConstructor
public class ViewStatsDto {
    private final String app;
    private final String uri;
    private final Long hits;
}