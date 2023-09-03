package ru.practicum.disliked_events.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "disliked")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DislikedEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users", referencedColumnName = "id", nullable = false)
    private User user;
}