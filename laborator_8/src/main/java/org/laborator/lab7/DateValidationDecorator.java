package org.laborator.lab7;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

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
