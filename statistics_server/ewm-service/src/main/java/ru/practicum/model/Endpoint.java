package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String app;
    private String uri;
    private String ip;
    @Column(name = "request_time")
    private LocalDateTime requestTime;
}