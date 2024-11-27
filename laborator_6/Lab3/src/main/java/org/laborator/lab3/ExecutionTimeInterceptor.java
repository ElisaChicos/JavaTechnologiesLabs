package org.laborator.lab3;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Logger;

@Interceptor
public class ExecutionTimeInterceptor {

    private static final Logger logger = Logger.getLogger(ExecutionTimeInterceptor.class.getName());

    @AroundInvoke
    public Object logExecutionTime(InvocationContext context) throws Exception {
        long startTime = System.currentTimeMillis();

        try {
            logger.info("Entering method: " + context.getMethod().getName());
            return context.proceed();  // Proceed with the method execution
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.info("Exiting method: " + context.getMethod().getName() + " | Execution time: " + duration + " ms");
        }
    }
}
