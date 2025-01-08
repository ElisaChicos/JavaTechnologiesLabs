package org.laborator.lab7.rest.metrics;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/metrics")
public class MetricsResource {

    private static final CustomMetricsRegistry registry = new CustomMetricsRegistry();

    @GET
    @Path("/monitored-endpoint")
    @Produces(MediaType.TEXT_PLAIN)
    public Response monitoredEndpoint() {
        long startTime = System.currentTimeMillis();
        simulateProcessing();
        long endTime = System.currentTimeMillis();

        // Record metrics
        registry.incrementCounter("monitoredEndpointInvocations");
        registry.recordResponseTime("monitoredEndpointResponseTime", endTime - startTime);

        return Response.ok("Monitored endpoint accessed").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetrics() {
        return Response.ok(registry.getAllMetrics()).build();
    }

    private void simulateProcessing() {
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
