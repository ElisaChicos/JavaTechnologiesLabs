package org.laborator.lab7;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class StudentDashboardBean {
    private List<Evaluation> evaluations;

    @Inject
    private EvaluationRepository evaluationRepository;

    @Inject
    private AuthenticationBean authenticationBean;

    public List<Evaluation> getEvaluations() {
        if (evaluations == null) {
            User loggedStudent = authenticationBean.getLoggedUser();
            if (loggedStudent != null && "student".equals(loggedStudent.getRole())) {
                evaluations = evaluationRepository.getEvaluationsByStudentId(loggedStudent.getId());
            }
        }
        return evaluations;
    }
}
