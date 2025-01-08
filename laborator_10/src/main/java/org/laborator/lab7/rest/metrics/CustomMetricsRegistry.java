package org.laborator.lab7.rest.metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CustomMetricsRegistry {

    private final ConcurrentMap<String, Metric> metrics = new ConcurrentHashMap<>();

    public void incrementCounter(String name) {
        metrics.computeIfAbsent(name, Metric::new).increment();
    }

    public void recordResponseTime(String name, long timeInMillis) {
        metrics.computeIfAbsent(name, Metric::new).recordResponseTime(timeInMillis);
    }

    public Metric getMetric(String name) {
        return metrics.get(name);
    }

    public ConcurrentMap<String, Metric> getAllMetrics() {
        return metrics;
    }
}
