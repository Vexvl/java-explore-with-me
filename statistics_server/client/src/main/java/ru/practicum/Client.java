package ru.practicum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Client {
    protected final RestTemplate rest = new RestTemplate();
    private final String path = "http://localhost:9090";

    public void saveEndpoint(EndpointDto endpointDto) {
        rest.postForLocation(path + "/hit", endpointDto);
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<ViewStatsDto[]> responseResult = rest.getForEntity(path + "/stats" + "?start=" + start + "&end=" + end +
                "&uris=" + uris + "&unique=" + unique, ViewStatsDto[].class);
        return Arrays.asList(Objects.requireNonNull(responseResult.getBody()));
    }
}