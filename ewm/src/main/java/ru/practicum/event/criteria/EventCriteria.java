package ru.practicum.event.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.SortParameter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCriteria {
    private List<Long> users;
    private String text;
    private Boolean onlyAvailable;
    private SortParameter sortParam;
    private List<Integer> categories;
    private Boolean paid;
    private List<EventState> states;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}