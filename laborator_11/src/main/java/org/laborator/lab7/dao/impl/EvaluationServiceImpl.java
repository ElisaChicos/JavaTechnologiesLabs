package org.laborator.lab7.dao.impl;

import com.google.gson.Gson;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.laborator.lab7.entity.User;
import org.laborator.lab7.event.EvaluationEvent;
import org.laborator.lab7.dao.EvaluationService;
import org.laborator.lab7.entity.Evaluation;
import org.laborator.lab7.event.EventManager;
import org.laborator.lab7.interceptor.GsonConfig;
import org.laborator.lab7.repository.EvaluationRepository;
import org.laborator.lab7.repository.UserRepository;

@Dependent
public class EvaluationServiceImpl implements EvaluationService {
    @Inject
    private EvaluationRepository evaluationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Event<EvaluationEvent> evaluationEvent;

    @Inject
    private EventManager eventManager;


    @Override
    public void saveEvaluation(Evaluation evaluation) {
        try {
            evaluationRepository.saveEvaluation(evaluation);

            User student = evaluation.getStudent();
            student.setCounter(student.getCounter() + 1);
            userRepository.saveUser(student);

            // Persist event
            String payload = GsonConfig.getGson().toJson(evaluation);
            eventManager.saveEvent("EvaluationSubmitted", payload);

            evaluationEvent.fire(new EvaluationEvent(evaluation));
        } catch (Exception e) {
            // Compensation: Rollback evaluation save
            evaluationRepository.deleteEvaluation(evaluation.getId());
            throw new RuntimeException("Saga failed, rollback executed: " + e.getMessage());
        }
    }

}
