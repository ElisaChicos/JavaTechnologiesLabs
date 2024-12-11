package org.laborator.lab7;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jsonb.JsonBindingFeature;

@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages("org.laborator.lab7"); // Scan for resources
        register(new AppBinder()); // Register the custom binder
        register(JsonBindingFeature.class);
    }
}
