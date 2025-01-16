package org.laborator.lab7.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Provider
@ApplicationScoped
@Priority(Priorities.USER)
public class CacheFilter implements ContainerResponseFilter {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(CacheFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String method = requestContext.getMethod();

        if ("GET".equals(method)) {
            String fullUri = requestContext.getUriInfo().getRequestUri().toString();
            String cacheKey = fullUri.substring(fullUri.indexOf("/api"));

            if (!cache.containsKey(cacheKey)) {
                cache.put(cacheKey, responseContext.getEntity());
                logger.info("Cache entry was created with key: " + cacheKey);
            }
            else
            {
                logger.info("Cache entry already exists with key: " + cacheKey);
            }
        }
    }

    public void clearCacheByEvaluationId(Long evaluationId) {
        cache.keySet().removeIf(key -> key.contains("evaluationId=" + evaluationId));
        logger.info("Clear cache by evaluationId: " + evaluationId + " was called!");
    }

    public void clearCacheForUser(Long userId) {
        cache.keySet().removeIf(key -> key.contains("teacherId=" + userId) || key.contains("studentId=" + userId));
        logger.info("Clear cache by userId: " + userId + " was called!");
    }

    private String generateCacheKey(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo().getRequestUri().getPath().substring("/Lab3_war".length());
    }

    public void clearCacheForAllEvaluations() {
        cache.remove("/api/evaluations");
        logger.info("Clear cache by all evaluations was called!");
    }

    public Map<String, Object> getCache() {
        return cache;
    }
}
