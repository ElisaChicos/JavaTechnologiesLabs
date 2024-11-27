package org.laborator.lab7;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class AdminDashboardBean {
    private List<Evaluation> allEvaluations;

    @Inject
    private EvaluationRepository evaluationRepository;

    public List<Evaluation> getAllEvaluations() {
        if (allEvaluations == null) {
            allEvaluations = evaluationRepository.getAllEvaluations();
        }
        return allEvaluations;
    }

    public long getTotalEvaluations() {
        return getAllEvaluations().size();
    }

    public double getAverageGrade() {
        return getAllEvaluations().stream()
                .mapToInt(Evaluation::getGrade)
                .average()
                .orElse(0.0);
    }
}
