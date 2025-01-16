package org.laborator.lab7.repository;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.core.UriInfo;
import org.laborator.lab7.entity.Evaluation;
import org.laborator.lab7.entity.User;
import org.laborator.lab7.filter.CacheFilter;
import org.laborator.lab7.producer.RegistrationNumberProducer;


@Path("/evaluations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationResource {

    @Inject
    private EvaluationRepository evaluationRepository;
    @Inject
    private RegistrationNumberProducer registrationNumberProducer;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CacheFilter cacheFilter;

    @POST
    @Operation(summary = "Add a new evaluation", description = "Creates a new evaluation and stores it in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evaluation created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Evaluation.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json"))
    })
    public Response addEvaluation(
            @QueryParam("teacherId") Long teacherId,
            @QueryParam("studentId") Long studentId,
            Evaluation evaluation) {
        try {
            if (teacherId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Teacher ID is missing or invalid.").build();
            }

            if (studentId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Student ID is missing or invalid.").build();
            }
            User teacher = userRepository.findUserById(teacherId);
            if (teacher == null || !Objects.equals(teacher.getRole(), "teacher")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Teacher with the given ID does not exist.").build();
            }
            User student = userRepository.findUserById(studentId);
            if (student == null || !Objects.equals(student.getRole(), "student")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Student with the given ID does not exist.").build();
            }
            evaluation.setTeacher(teacher);
            evaluation.setStudent(student);
            evaluation.setTimestamp(LocalDateTime.now());
            evaluation.setRegistrationNumber(registrationNumberProducer.produceRegistrationNumber().toString());
            evaluationRepository.saveEvaluation(evaluation);

            cacheFilter.clearCacheForUser(teacherId);
            cacheFilter.clearCacheForUser(studentId);
            cacheFilter.clearCacheForAllEvaluations();

            return Response.status(Response.Status.CREATED).entity(evaluation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error adding evaluation: " + e.getMessage()).build();
        }
    }


    @PUT
    @Operation(summary = "Update an evaluation", description = "Updates an existing evaluation in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluation updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Evaluation.class))),
            @ApiResponse(responseCode = "404", description = "Evaluation not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json"))
    })
    public Response updateEvaluation(
            @QueryParam("evaluationId") Long id,
            Evaluation updatedEvaluation) {
        try {
            if (id == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("NULL ID").build();
            }
            Evaluation existingEvaluation = evaluationRepository.findById(id);
            if (existingEvaluation == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Evaluation not found").build();
            }

            if (updatedEvaluation.getTeacher() != null) {
                User teacher = userRepository.findUserById(updatedEvaluation.getTeacher().getId());
                if (teacher == null || !Objects.equals(teacher.getRole(), "teacher")) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid teacher ID").build();
                }
                existingEvaluation.setTeacher(teacher);
            }

            if (updatedEvaluation.getStudent() != null) {
                User student = userRepository.findUserById(updatedEvaluation.getStudent().getId());
                if (student == null || !Objects.equals(student.getRole(), "student")) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid student ID").build();
                }
                existingEvaluation.setStudent(student);
            }

            if (updatedEvaluation.getActivity() != null) {
                existingEvaluation.setActivity(updatedEvaluation.getActivity());
            }
            if (updatedEvaluation.getActivityType() != null) {
                existingEvaluation.setActivityType(updatedEvaluation.getActivityType());
            }
            if (updatedEvaluation.getGrade() != 0) {
                existingEvaluation.setGrade(updatedEvaluation.getGrade());
            }
            if (updatedEvaluation.getComment() != null) {
                existingEvaluation.setComment(updatedEvaluation.getComment());
            }

            evaluationRepository.updateEvaluation(existingEvaluation);
            cacheFilter.clearCacheForUser(existingEvaluation.getStudent().getId());
            cacheFilter.clearCacheForUser(existingEvaluation.getTeacher().getId());
            cacheFilter.clearCacheByEvaluationId(existingEvaluation.getId());
            cacheFilter.clearCacheForAllEvaluations();
            return Response.ok(existingEvaluation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error updating evaluation: " + e.getMessage()).build();
        }
    }


    @DELETE
    @Operation(summary = "Delete an evaluation", description = "Deletes an existing evaluation by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evaluation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Evaluation not found",
                    content = @Content(mediaType = "application/json"))
    })
    public Response deleteEvaluation(
            @QueryParam("evaluationId")  Long id) {
        try {
            if (id == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("NULL ID").build();
            }
            Evaluation existingEvaluation = evaluationRepository.findById(id);
            if (existingEvaluation == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Evaluation not found").build();
            }

            evaluationRepository.deleteEvaluation(id);
            cacheFilter.clearCacheForUser(existingEvaluation.getStudent().getId());
            cacheFilter.clearCacheForUser(existingEvaluation.getTeacher().getId());
            cacheFilter.clearCacheByEvaluationId(existingEvaluation.getId());
            cacheFilter.clearCacheForAllEvaluations();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting evaluation: " + e.getMessage()).build();
        }
    }


    @GET
    @Operation(summary = "Get evaluations", description = "Fetches evaluations from the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluations fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Evaluation.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json"))
    })
    public Response getEvaluations(
            @QueryParam("teacherId") Long teacherId,
            @QueryParam("studentId") Long studentId,
            @Context UriInfo uriInfo) {
        try {
            String requestUri = uriInfo.getRequestUri().toString();
            String cacheKey = requestUri.substring(requestUri.indexOf("/api"));
            if(cacheFilter.getCache().containsKey(cacheKey))
            {
                return Response.ok(cacheFilter.getCache().get(cacheKey)).build();
            }
            List<Evaluation> evaluations;

            if (teacherId != null) {
                User teacher = userRepository.findUserById(teacherId);
                if (teacher == null || !Objects.equals(teacher.getRole(), "teacher")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Teacher with the given ID does not exist.").build();
                }
                evaluations = evaluationRepository.getEvaluationsForTeacher(teacherId);
            } else if (studentId != null) {
                User student = userRepository.findUserById(studentId);
                if (student == null || !Objects.equals(student.getRole(), "student")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Student with the given ID does not exist.").build();
                }
                evaluations = evaluationRepository.getEvaluationsByStudentId(studentId);
            } else {
                evaluations = evaluationRepository.getAllEvaluations();
            }
            cacheFilter.getCache().put(cacheKey, evaluations);
            return Response.ok(evaluations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error fetching evaluations: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/count")
    @Operation(summary = "Get evaluation count for a user", description = "Fetches the number of evaluations submitted by a specific user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluation count fetched successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    public Response getEvaluationCount(@QueryParam("userId") Long userId) {
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User ID is missing or invalid.").build();
            }

            User user = userRepository.findUserById(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User with the given ID does not exist.").build();
            }

            int evaluationCount = evaluationRepository.getEvaluationCountForUser(userId);

            return Response.ok(evaluationCount).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching evaluation count: " + e.getMessage()).build();
        }
    }
}
