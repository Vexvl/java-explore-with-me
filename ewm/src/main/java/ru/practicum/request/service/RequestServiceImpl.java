package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AbsenceException;
import ru.practicum.exception.ParticipationRequestFailException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.request.model.EventRequestStatusUpdateResult;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AbsenceException("Event not exists"));
        User user = userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ParticipationRequestFailException("Request exists");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ParticipationRequestFailException("Not enough rights");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ParticipationRequestFailException("Not published event");
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequest() && event.getParticipantLimit() != 0) {
            throw new ParticipationRequestFailException("No vacancies");
        }
        ParticipationRequest request = ParticipationRequest.builder().created(LocalDateTime.now()).event(event)
                .requester(user).status(event.getParticipantLimit() == 0 || !event.getRequestModeration() ?
                        RequestStatus.CONFIRMED : RequestStatus.PENDING).build();
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequest(event.getConfirmedRequest() + 1);
            eventRepository.save(event);
        }
        return RequestMapper.toParticipationEventDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getUserRequest(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        return requestRepository.findAllByRequesterId(userId).stream().map(RequestMapper::toParticipationEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        requestRepository.findById(requestId).orElseThrow(() -> new AbsenceException("Request not exists"));
        ParticipationRequest participationRequest = requestRepository.findByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new AbsenceException("Request not exists"));
        participationRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationEventDto(requestRepository.save(participationRequest));
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        eventRepository.findById(eventId).orElseThrow(() -> new AbsenceException("Event not exists"));
        return requestRepository.findAllByEventId(eventId).stream().map(RequestMapper::toParticipationEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AbsenceException("User not exists"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AbsenceException("Event not exists"));

        int alreadyConfirmed = Math.toIntExact(event.getConfirmedRequest());
        int availableSlots = event.getParticipantLimit() - alreadyConfirmed;
        if (availableSlots <= 0) {
            throw new ParticipationRequestFailException("No vacancies");
        }

        List<ParticipationRequest> queryRequests = requestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
        if (!queryRequests.stream().allMatch(request -> request.getStatus().equals(RequestStatus.PENDING))) {
            throw new ParticipationRequestFailException("Not all PENDING");
        }

        List<ParticipationRequest> result = new ArrayList<>();
        int confirmedCount = Math.min(availableSlots, queryRequests.size());

        for (int i = 0; i < confirmedCount; i++) {
            ParticipationRequest request = queryRequests.get(i);
            request.setStatus(RequestStatus.CONFIRMED);
            result.add(request);
        }

        for (int i = confirmedCount; i < queryRequests.size(); i++) {
            ParticipationRequest request = queryRequests.get(i);
            request.setStatus(RequestStatus.REJECTED);
            result.add(request);
        }

        event.setConfirmedRequest((long) (alreadyConfirmed + confirmedCount));
        eventRepository.save(event);
        List<ParticipationRequest> savedResult = requestRepository.saveAll(result);

        return RequestMapper.toEventRequestStatusUpdateResult(savedResult);
    }
}