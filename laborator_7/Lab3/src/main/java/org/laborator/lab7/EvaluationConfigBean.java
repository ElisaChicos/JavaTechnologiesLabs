package org.laborator.lab7;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class EvaluationConfigBean implements Serializable {
    private EvaluationConfig config = new EvaluationConfig();

    @Inject
    private EvaluationConfigRepository configRepository;

    @PostConstruct
    public void init() {
        config = configRepository.getConfig();
    }

    public void saveConfig() {
        configRepository.updateConfig(config);
    }

    public EvaluationConfig getConfig() {
        return config;
    }

    public void setConfig(EvaluationConfig config) {
        this.config = config;
    }
}
