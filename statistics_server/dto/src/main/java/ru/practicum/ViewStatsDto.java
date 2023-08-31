package ru.practicum;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ViewStatsDto {
    private final String app;
    private final String uri;
    private final Long hits;
}