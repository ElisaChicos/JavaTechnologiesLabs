package org.laborator.lab7.decorator;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.laborator.lab7.entity.EvaluationConfig;
import org.laborator.lab7.dao.EvaluationService;
import org.laborator.lab7.entity.Evaluation;
import org.laborator.lab7.repository.EvaluationConfigRepository;

import java.time.LocalDateTime;

@Decorator
public class DateValidationDecorator implements EvaluationService {
    @Inject
    @Delegate
    private EvaluationService evaluationService;

    @Inject
    private EvaluationConfigRepository configRepository;

    @Override
    public void saveEvaluation(Evaluation evaluation) {
        EvaluationConfig config = configRepository.getConfig();
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(config.getStart_time()) || now.isAfter(config.getEnd_time())) {
            throw new IllegalArgumentException("Evaluations can only be submitted within the allowed time range.");
        }

        evaluationService.saveEvaluation(evaluation); // Delegate to the original service
    }
}
