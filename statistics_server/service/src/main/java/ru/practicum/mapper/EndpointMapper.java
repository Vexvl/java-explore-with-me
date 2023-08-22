package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointDto;
import ru.practicum.model.Endpoint;

@UtilityClass
public class EndpointMapper {

    public Endpoint toEndpoint(EndpointDto endpointDto) {
        return Endpoint.builder()
                .appName(endpointDto.getApp())
                .appUri(endpointDto.getUri())
                .ip(endpointDto.getIp())
                .timestamp(endpointDto.getTimestamp())
                .build();
    }
}