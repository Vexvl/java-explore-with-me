package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto getCompilation(Long compilationId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto updateCompilation(Long compilationId, NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compilationId);
}