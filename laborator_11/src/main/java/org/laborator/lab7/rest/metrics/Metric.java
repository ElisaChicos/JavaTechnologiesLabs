package org.laborator.lab7.rest.metrics;

class Metric {
    private final String name;
    private long count = 0;
    private long totalResponseTime = 0;

    public Metric(String name) {
        this.name = name;
    }

    public synchronized void increment() {
        count++;
    }

    public synchronized void recordResponseTime(long timeInMillis) {
        count++;
        totalResponseTime += timeInMillis;
    }

    public long getCount() {
        return count;
    }

    public long getAverageResponseTime() {
        return count > 0 ? totalResponseTime / count : 0;
    }
}