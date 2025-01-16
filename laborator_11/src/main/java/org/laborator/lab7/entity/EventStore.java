package org.laborator.lab7.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_store")
public class EventStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public EventStore() {}

    public EventStore(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }
}
