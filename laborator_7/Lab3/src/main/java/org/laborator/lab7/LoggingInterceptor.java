package org.laborator.lab7;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.logging.Logger;

@LogExecution
@Interceptor
public class LoggingInterceptor {
    private static final Logger logger = Logger.getLogger(LoggingInterceptor.class.getName());

    @AroundInvoke
    public Object logMethodExecution(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        long startTime = System.currentTimeMillis();

        logger.info("Entering method: " + methodName);
        try {
            return context.proceed(); // Proceed with the intercepted method
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("Exiting method: " + methodName + " | Execution time: " + (endTime - startTime) + " ms");
        }
    }
}
