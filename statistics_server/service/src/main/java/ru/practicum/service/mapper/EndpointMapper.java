package ru.practicum.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointDto;
import ru.practicum.service.model.Endpoint;

@UtilityClass
public class EndpointMapper {

    public Endpoint toEndpoint(EndpointDto endpointDto) {
        return Endpoint.builder()
                .app(endpointDto.getApp())
                .uri(endpointDto.getUri())
                .ip(endpointDto.getIp())
                .requestTime(endpointDto.getTimestamp())
                .build();
    }
}