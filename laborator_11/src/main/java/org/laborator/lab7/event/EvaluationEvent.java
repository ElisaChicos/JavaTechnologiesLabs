package org.laborator.lab7.event;

import org.laborator.lab7.entity.Evaluation;

public class EvaluationEvent {
    private Evaluation evaluation;

    public EvaluationEvent(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
