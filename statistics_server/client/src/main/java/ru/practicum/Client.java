package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class Client {

    private final String url = "http://stats-server:9090";

    private final RestTemplate restTemplate;

    public Client(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void saveEndpoint(EndpointDto endpointDto) {
        String endpointUrl = url + "/hit";
        restTemplate.postForLocation(endpointUrl, endpointDto);
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String formattedStart = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(start);
        String formattedEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(end);

        String statsUrl = url + "/stats" +
                "?start=" + formattedStart +
                "&end=" + formattedEnd +
                "&uris=" + String.join(",", uris) +
                "&unique=" + unique;

        ResponseEntity<ViewStatsDto[]> responseResult = restTemplate.getForEntity(statsUrl, ViewStatsDto[].class);
        if (responseResult.getStatusCode() == HttpStatus.OK && responseResult.getBody() != null) {
            return Arrays.asList(responseResult.getBody());
        } else {
            throw new RuntimeException("Error fetching view stats");
        }
    }
}