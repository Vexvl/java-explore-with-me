package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false)
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .id(compilation.getId())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Compilation updateCompilation(NewCompilationDto newCompilationDto, Compilation compilation, List<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle() != null ?
                        newCompilationDto.getTitle() : compilation.getTitle())
                .pinned(newCompilationDto.getPinned() != null ?
                        newCompilationDto.getPinned() : compilation.getPinned())
                .id(compilation.getId())
                .events(events)
                .build();
    }
}