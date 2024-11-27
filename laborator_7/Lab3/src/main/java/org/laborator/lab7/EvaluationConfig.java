package org.laborator.lab7;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_config")
@NamedQuery(name = "EvaluationConfig.findConfig", query = "SELECT c FROM EvaluationConfig c")
public class EvaluationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime start_time;
    private LocalDateTime end_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getStart_time() {
        return start_time;
    }
    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }
    public LocalDateTime getEnd_time() {
        return end_time;
    }
    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }
}
