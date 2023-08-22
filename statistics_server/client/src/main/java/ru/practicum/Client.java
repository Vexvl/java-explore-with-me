package ru.practicum;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class Client {

    private final String path = "http://localhost:9090";
    private final RestTemplate restTemplate;

    public Client() {
        this.restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(path))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void saveEndpoint(EndpointDto endpointDto) {
        restTemplate.postForLocation(path + "/hit", endpointDto);
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<ViewStatsDto[]> responseResult = restTemplate.getForEntity(path + "/stats" + "?start=" + start + "&end=" + end +
                "&uris=" + uris + "&unique=" + unique, ViewStatsDto[].class);
        return Arrays.asList(Objects.requireNonNull(responseResult.getBody()));
    }
}