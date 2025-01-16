package org.laborator.lab7.observer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.laborator.lab7.event.EvaluationEvent;
import org.laborator.lab7.entity.Evaluation;

import java.util.logging.Logger;

@ApplicationScoped
public class EvaluationObserver {
    private static final Logger logger = Logger.getLogger(EvaluationObserver.class.getName());

    public void onEvaluationSubmitted(@Observes EvaluationEvent event) {
        Evaluation evaluation = event.getEvaluation();
        logger.info("New evaluation submitted:");
        logger.info("Teacher: " + evaluation.getTeacher().getUsername());
        logger.info("Student: " + evaluation.getStudent().getUsername());
        logger.info("Activity: " + evaluation.getActivity());
        logger.info("Grade: " + evaluation.getGrade());
    }
}
