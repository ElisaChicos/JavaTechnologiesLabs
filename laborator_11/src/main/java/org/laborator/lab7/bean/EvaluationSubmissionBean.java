package org.laborator.lab7.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import org.laborator.lab7.dao.EvaluationService;
import org.laborator.lab7.entity.Evaluation;
import org.laborator.lab7.entity.EvaluationConfig;
import org.laborator.lab7.entity.User;
import org.laborator.lab7.interceptor.LogExecution;
import org.laborator.lab7.producer.RegistrationNumberProducer;
import org.laborator.lab7.repository.EvaluationConfigRepository;
import org.laborator.lab7.repository.EvaluationRepository;
import org.laborator.lab7.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
@LogExecution
public class EvaluationSubmissionBean {
    private Long teacherId;
    private Long studentId;
    private String activity;
    private String activityType;
    private int grade;
    private String comment;

    private static final Logger logger = Logger.getLogger(EvaluationSubmissionBean.class.getName());

    @Inject
    private EvaluationConfigRepository configRepository;

    @Inject
    private EvaluationRepository evaluationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthenticationBean authenticationBean;

    @Inject
    private UUID registrationNumber;

    @Inject
    private EvaluationService evaluationService;
    @Inject
    private RegistrationNumberProducer registrationNumberProducer;

    public String submit() {
        EvaluationConfig config = configRepository.getConfig();

        logger.log(Level.INFO, "Configured Start Time: {0}", config.getStart_time().toString());
        logger.log(Level.INFO, "Configured End Time: {0}", config.getEnd_time().toString());
        logger.log(Level.INFO, "Current Time: {0}", LocalDateTime.now());

        if (LocalDateTime.now().isBefore(config.getStart_time()) || LocalDateTime.now().isAfter(config.getEnd_time())) {
            logger.log(Level.SEVERE, "OUTSIDE TIME ZONE!");
            return "error.xhtml?faces-redirect=true&error=outsideTimeRange";
        }


        User teacher = userRepository.findUserById(teacherId);
        User student = authenticationBean.getLoggedUser();


        logger.log(Level.INFO, "Teacher ID: {0}", teacher.getId());
        logger.log(Level.INFO, "Student ID: {0}", student.getId());

        if (teacher == null || student == null) {
            return "error.xhtml?faces-redirect=true&error=invalidTeacherOrStudent";
        }
        @Valid Evaluation evaluation = new Evaluation();
        evaluation.setTeacher(teacher);
        evaluation.setStudent(student);
        evaluation.setActivity(activity);
        evaluation.setActivityType(activityType);
        evaluation.setGrade(grade);
        evaluation.setComment(comment);
        evaluation.setTimestamp(LocalDateTime.now());
        evaluation.setRegistrationNumber(registrationNumberProducer.produceRegistrationNumber().toString());
        logger.log(Level.INFO, "Registration number: {0}", evaluation.getRegistrationNumber());
        evaluationService.saveEvaluation(evaluation);

        return "/common/success.xhtml";
    }
    public Long getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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
}
