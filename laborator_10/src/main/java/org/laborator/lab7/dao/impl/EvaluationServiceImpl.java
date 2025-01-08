package org.laborator.lab7.dao.impl;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.laborator.lab7.event.EvaluationEvent;
import org.laborator.lab7.dao.EvaluationService;
import org.laborator.lab7.entity.Evaluation;
import org.laborator.lab7.repository.EvaluationRepository;

@Dependent
public class EvaluationServiceImpl implements EvaluationService {
    @Inject
    private EvaluationRepository evaluationRepository;

    @Inject
    private Event<EvaluationEvent> evaluationEvent;

    @Override
    public void saveEvaluation(Evaluation evaluation) {
        evaluationRepository.saveEvaluation(evaluation);

        evaluationEvent.fire(new EvaluationEvent(evaluation));
    }
}
