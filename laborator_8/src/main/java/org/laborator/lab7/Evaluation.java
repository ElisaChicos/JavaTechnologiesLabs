package org.laborator.lab7;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Entity
@Table(name = "evaluations")
@NamedQuery(name = "Evaluation.findByTeacherId",query = "SELECT e FROM Evaluation e WHERE e.teacher.id = :teacherId")
@NamedQuery(name = "Evaluation.findByStudentId", query = "SELECT e FROM Evaluation e WHERE e.student.id = :studentId")
@NamedQuery(name = "Evaluation.findAllEvaluations", query = "SELECT e FROM Evaluation e")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @NotNull(message = "Teacher cannot be null")
    @JsonbTransient
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student cannot be null")
    @JsonbTransient
    private User student;

    @Column(name = "activity", nullable = false)
    @Size(min = 3, max = 255, message = "Activity must be between 3 and 255 characters")
    private String activity;

    @Column(name = "activity_type", nullable = false)
    @Size(min = 3, max = 50, message = "Activity type must be between 3 and 50 characters")
    private String activityType;

    @Column(name = "grade", nullable = false)
    @Min(value = 1, message = "Grade must be at least 1")
    @Max(value = 10, message = "Grade cannot exceed 10")
    private int grade;

    @Column(name="comment", nullable = false)
    @NotNull(message = "Comment cannot be null")
    @Size(min = 3, message = "Comment must be at least 3 characters")
    private String comment;

    @Column(name="timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name="registration_number", nullable = false, unique = true, updatable = false)
    @JsonbTransient
    private String registrationNumber;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getActivityType() {
        return activityType;
    }
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    public User getTeacher() {
        return teacher;
    }
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
    public User getStudent() {
        return student;
    }
    public void setStudent(User student) {
        this.student = student;
    }

}
