package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointDto;
import ru.practicum.model.Endpoint;

@UtilityClass
public class EndpointMapper {

    public Endpoint toEndpoint(EndpointDto endpointDto) {
        return Endpoint.builder()
                .app(endpointDto.getApp())
                .uri(endpointDto.getUri())
                .ip(endpointDto.getIp())
                .timestamp(endpointDto.getRequestTime())
                .build();
    }
}