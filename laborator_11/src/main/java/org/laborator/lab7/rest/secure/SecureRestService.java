package org.laborator.lab7.rest.secure;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.faulttolerance.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/secure")
public class SecureRestService {
    private static final AtomicInteger failureCount = new AtomicInteger(0);
    private static final int FAILURE_THRESHOLD = 3;
    private static final Semaphore semaphore = new Semaphore(5);

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Timeout(500)
    @Fallback(fallbackMethod = "fallbackAdminEndpoint")
    public Response adminEndpoint(@Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
        }
        return Response.ok("Admin endpoint accessed.").build();
    }

    public Response fallbackAdminEndpoint(SecurityContext securityContext) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Fallback: Service unavailable for admin").build();
    }

    @GET
    @Path("/teacher")
    @Produces(MediaType.TEXT_PLAIN)
    @Retry(maxRetries = 3, delay = 200)
    public Response teacherEndpoint(@Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("teacher")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
        }
        return Response.ok("Teacher endpoint accessed.").build();
    }

    @GET
    @Path("/student")
    @Produces(MediaType.TEXT_PLAIN)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 2000)
    public Response studentEndpoint(@Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("student")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
        }
        return Response.ok("Student endpoint accessed.").build();
    }

    @GET
    @Path("/bulkhead-thread")
    @Produces(MediaType.TEXT_PLAIN)
    @Bulkhead(value = 5)
    public Response bulkheadThreadPool() {
        return Response.ok("Bulkhead thread-pool success").build();
    }

    @GET
    @Path("/bulkhead-semaphore")
    @Produces(MediaType.TEXT_PLAIN)
    @Bulkhead(value = 2, waitingTaskQueue = 5)
    public Response bulkheadSemaphore() {
        return Response.ok("Bulkhead semaphore success").build();
    }
}
