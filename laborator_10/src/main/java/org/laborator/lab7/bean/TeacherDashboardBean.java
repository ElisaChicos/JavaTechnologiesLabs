package org.laborator.lab7.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.laborator.lab7.entity.Evaluation;
import org.laborator.lab7.repository.EvaluationRepository;
import org.laborator.lab7.entity.User;

import java.util.List;

@Named
@RequestScoped
public class TeacherDashboardBean {
    private List<Evaluation> evaluations;

    @Inject
    private EvaluationRepository evaluationRepository;

    @Inject
    private AuthenticationBean authenticationBean;

    public List<Evaluation> getEvaluations() {
        User loggedTeacher = authenticationBean.getLoggedUser();
        if (loggedTeacher != null && "teacher".equals(loggedTeacher.getRole())) {
            evaluations = evaluationRepository.getEvaluationsForTeacher(loggedTeacher.getId());
        }
        return evaluations;
    }
}
