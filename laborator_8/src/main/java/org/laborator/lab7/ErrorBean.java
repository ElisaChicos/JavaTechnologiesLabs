package org.laborator.lab7;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;

import java.util.Map;

@Named
@RequestScoped
public class ErrorBean {
    private String errorMessage;

    public String getErrorMessage() {
        if (errorMessage == null) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String error = params.get("error");

            switch (error) {
                case "outsideTimeRange":
                    errorMessage = "You cannot submit evaluations outside the allowed time range.";
                    break;
                case "invalidTeacherOrStudent":
                    errorMessage = "Invalid teacher or student. Please try again.";
                    break;
                case "invalidStudent":
                    errorMessage = "You are not authorized to submit evaluations.";
                    break;
                case "invalidTeacher":
                    errorMessage = "The selected teacher is not valid.";
                    break;
                default:
                    errorMessage = "An unknown error occurred. Please try again.";
                    break;
            }
        }
        return errorMessage;
    }
}
