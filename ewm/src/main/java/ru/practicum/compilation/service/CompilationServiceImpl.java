package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.repository.CompilationSpecification;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AbsenceException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compilationId, NewCompilationDto newCompilationDto) {
        Compilation updateCompilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new AbsenceException("Compilation not exist"));
        List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        updateCompilation = CompilationMapper.updateCompilation(newCompilationDto, updateCompilation, events);
        return CompilationMapper.toCompilationDto(compilationRepository.save(updateCompilation));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compilationId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compilationId)
                .orElseThrow(() -> new AbsenceException("Compilation with id %d does not exist")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationRepository.findAll(new CompilationSpecification(pinned), pageable).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compilationId) {
        compilationRepository.findById(compilationId).orElseThrow(() -> new AbsenceException("Compilation not exists"));
        compilationRepository.deleteById(compilationId);
    }
}