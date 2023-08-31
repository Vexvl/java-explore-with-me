package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ParticipationRequestFailException("Event exists"));
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
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequest(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        return requestRepository.findAllByRequesterId(userId).stream().map(RequestMapper::toParticipationEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        requestRepository.findById(requestId).orElseThrow(() -> new AbsenceException("Request not exists"));
        ParticipationRequest participationRequest = requestRepository.findByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new AbsenceException("Request not exists"));
        participationRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationEventDto(requestRepository.save(participationRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        eventRepository.findById(eventId).orElseThrow(() -> new AbsenceException("Event not exists"));
        return requestRepository.findAllByEventId(eventId).stream().map(RequestMapper::toParticipationEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId).orElseThrow(() -> new AbsenceException("User not exists"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AbsenceException("Event not exists"));
        Long alreadyConfirmed = requestRepository.findConfirmed(eventId);

        if (event.getParticipantLimit() <= alreadyConfirmed) {
            throw new ParticipationRequestFailException("No vacancies");
        }

        List<ParticipationRequest> queryRequests = requestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
        if (!queryRequests.isEmpty()) {
            for (ParticipationRequest request : queryRequests) {
                if (!request.getStatus().equals(RequestStatus.PENDING)) {
                    throw new ParticipationRequestFailException("Not all state PENDING");
                }
            }
        }

        if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            queryRequests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
        } else {
            long availableVacancies = event.getParticipantLimit() - alreadyConfirmed;
            long numConfirmed = Math.min(availableVacancies, queryRequests.size());

            for (int i = 0; i < numConfirmed; i++) {
                ParticipationRequest request = queryRequests.get(i);
                request.setStatus(RequestStatus.CONFIRMED);
            }

            for (long i = numConfirmed; i < queryRequests.size(); i++) {
                ParticipationRequest request = queryRequests.get((int) i);
                request.setStatus(RequestStatus.REJECTED);
            }

            event.setConfirmedRequest(alreadyConfirmed + numConfirmed);
            eventRepository.save(event);
        }

        List<ParticipationRequest> savedRequests = requestRepository.saveAll(queryRequests);
        return RequestMapper.toEventRequestStatusUpdateResult(savedRequests);

    }
}