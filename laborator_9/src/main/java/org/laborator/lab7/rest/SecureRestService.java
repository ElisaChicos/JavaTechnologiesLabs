package org.laborator.lab7.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.inject.Inject;

@Path("/secure")
public class SecureRestService {

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    public Response adminEndpoint(@Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
        }
        return Response.ok("Admin endpoint accessed.").build();
    }

    @GET
    @Path("/teacher")
    @Produces(MediaType.TEXT_PLAIN)
    public Response teacherEndpoint(@Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("teacher")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
        }
        return Response.ok("Teacher endpoint accessed.").build();
    }

    @GET
    @Path("/student")
    @Produces(MediaType.TEXT_PLAIN)
    public Response studentEndpoint(@Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("student")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
        }
        return Response.ok("Student endpoint accessed.").build();
    }
}
