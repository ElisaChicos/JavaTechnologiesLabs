package org.laborator.lab7;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.UUID;

@ApplicationScoped
public class RegistrationNumberProducer {

    @Produces
    public UUID produceRegistrationNumber() {
        return UUID.randomUUID();
    }
}
