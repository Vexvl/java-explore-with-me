package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validator.ValidationGroups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public final class NewCompilationDto {
    @NotBlank(groups = ValidationGroups.Create.class)
    @Size(max = 50)
    private final String title;
    private final Boolean pinned;
    private final List<Long> events = List.of();
}