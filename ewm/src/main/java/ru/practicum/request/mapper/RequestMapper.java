package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.EventRequestStatusUpdateResult;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.RequestStatus;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public ParticipationRequestDto toParticipationEventDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated())
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }

    public EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<ParticipationRequest> participationRequestList) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(participationRequestList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                        .map(RequestMapper::toParticipationEventDto)
                        .collect(Collectors.toList()))
                .rejectedRequests(participationRequestList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.REJECTED))
                        .map(RequestMapper::toParticipationEventDto)
                        .collect(Collectors.toList()))
                .build();
    }
}