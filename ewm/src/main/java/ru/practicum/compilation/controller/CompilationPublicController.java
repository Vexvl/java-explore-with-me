package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
@Slf4j
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilation(@PathVariable(name = "compId") Long compilationId) {
        return compilationService.getCompilation(compilationId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        return compilationService.getCompilations(pinned, from, size);
    }
}