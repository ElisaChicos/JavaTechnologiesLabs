package org.laborator.lab7.rest.secure;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.faulttolerance.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/test")
public class SecureRestServiceTest {
    private static final AtomicInteger failureCount = new AtomicInteger(0);
    private static final int FAILURE_THRESHOLD = 3;
    private static final Semaphore semaphore = new Semaphore(5);

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String adminTestEndpoint(@Context SecurityContext securityContext) {
        return executeWithTimeout(() -> {
            simulateDelay();
            return "Admin test endpoint accessed after delay.";
        }); // Timeout in 500ms
    }

    @GET
    @Path("/teacher")
    @Produces(MediaType.TEXT_PLAIN)
    public String teacherTestEndpoint(@Context SecurityContext securityContext) {
        return retryOperation(() -> {
            simulateUnstableService();
            return "Teacher test endpoint accessed.";
        }); // Retry 3 times with 200ms delay
    }

    @GET
    @Path("/student")
    @Produces(MediaType.TEXT_PLAIN)
    public String studentTestEndpoint(@Context SecurityContext securityContext) {
        return circuitBreakerOperation(() -> {
            simulateUnstableService();
            return "Student test endpoint accessed.";
        });
    }

    @GET
    @Path("/bulkhead-thread")
    @Produces(MediaType.TEXT_PLAIN)
    public String bulkheadThreadPoolTest() {
        return bulkheadOperation(() -> {
            simulateHeavyLoad();
            return "Bulkhead thread-pool test success";
        });
    }

    @GET
    @Path("/bulkhead-semaphore")
    @Produces(MediaType.TEXT_PLAIN)
    @Bulkhead(value = 2, waitingTaskQueue = 5)
    public Response bulkheadSemaphoreTest() {
        simulateHeavyLoad(); // Simulate heavy load for testing
        return Response.ok("Bulkhead semaphore test success").build();
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void simulateUnstableService() {
        throw new RuntimeException("Simulated failure");
    }

    private void simulateHeavyLoad() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String bulkheadOperation(Callable<String> task) {
        if (!semaphore.tryAcquire()) {
            return "Fallback: Too many concurrent requests";
        }
        try {
            return task.call();
        } catch (Exception e) {
            return "Fallback: Operation failed";
        } finally {
            semaphore.release();
        }
    }

    private String executeWithTimeout(Callable<String> task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(task);

        try {
            return future.get(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return "Fallback: Timeout occurred";
        } catch (Exception e) {
            return "Fallback: An error occurred";
        } finally {
            executor.shutdown();
        }
    }

    private String retryOperation(Callable<String> task) {
        int attempt = 0;
        while (attempt < 3) {
            try {
                return task.call();
            } catch (Exception e) {
                attempt++;
                if (attempt == 3) {
                    return "Fallback: Retry attempts failed";
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {}
            }
        }
        return "Fallback: Retry attempts failed";
    }

    private boolean isCircuitOpen() {
        return failureCount.get() >= FAILURE_THRESHOLD;
    }

    private String circuitBreakerOperation(Callable<String> task) {
        if (isCircuitOpen()) {
            return "Fallback: Circuit breaker open";
        }
        try {
            return task.call();
        } catch (Exception e) {
            failureCount.incrementAndGet();
            return "Fallback: Operation failed";
        }
    }
}