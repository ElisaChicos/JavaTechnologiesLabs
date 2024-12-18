package org.laborator.lab7.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.laborator.lab7.repository.EvaluationRepository;

public class AppBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(EvaluationRepository.class).to(EvaluationRepository.class);
    }
}
