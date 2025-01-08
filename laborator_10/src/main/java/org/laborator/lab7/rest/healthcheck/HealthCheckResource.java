package org.laborator.lab7.rest.healthcheck;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/health")
public class HealthCheckResource {

    private static boolean isReady = true;
    private static boolean isLive = true;

    @GET
    @Path("/liveness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response livenessCheck() {
        if (isLive) {
            return Response.ok("{\"status\":\"UP\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"DOWN\"}").build();
        }
    }

    @GET
    @Path("/readiness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readinessCheck() {
        if (isReady) {
            return Response.ok("{\"status\":\"UP\"}").build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"status\":\"DOWN\"}").build();
        }
    }

    // Simulate readiness toggle
    @POST
    @Path("/toggle-readiness")
    public Response toggleReadiness() {
        isReady = !isReady;
        return Response.ok("Readiness status toggled").build();
    }

    // Simulate liveness toggle
    @POST
    @Path("/toggle-liveness")
    public Response toggleLiveness() {
        isLive = !isLive;
        return Response.ok("Liveness status toggled").build();
    }
}
