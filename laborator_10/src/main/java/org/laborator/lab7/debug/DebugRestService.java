package org.laborator.lab7.debug;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;

@Path("/secure")
public class DebugRestService {
    @GET
    @Path("/debug")
    @Produces(MediaType.TEXT_PLAIN)
    public Response debugEndpoint(@Context SecurityContext securityContext) {
        String user = securityContext.getUserPrincipal() != null
                ? securityContext.getUserPrincipal().getName()
                : "Anonymous";
        boolean isAdmin = securityContext.isUserInRole("admin");
        boolean isTeacher = securityContext.isUserInRole("teacher");
        boolean isStudent = securityContext.isUserInRole("student");

        return Response.ok("User: " + user +
                "\nIs Admin: " + isAdmin +
                "\nIs Teacher: " + isTeacher +
                "\nIs Student: " + isStudent).build();
    }
}
