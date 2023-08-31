package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private final Long id;
    private final String name;
}