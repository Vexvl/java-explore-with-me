package ru.practicum.event.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PublicParams {
    private String text;
    private Integer from;
    private Integer size;
    private String state;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private List<Integer> categories;
    private String sortParameter;
    private Boolean onlyAvailable;
    private String ip;
}