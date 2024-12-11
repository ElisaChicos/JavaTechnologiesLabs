package org.laborator.lab7;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

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
