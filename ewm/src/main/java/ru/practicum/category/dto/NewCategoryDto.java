package ru.practicum.category.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(force = true)
public final class NewCategoryDto {
    @NotBlank
    @Size(max = 50)
    private final String name;
}