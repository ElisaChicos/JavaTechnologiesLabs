package org.laborator.lab7;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AppBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(EvaluationRepository.class).to(EvaluationRepository.class);
    }
}
