package org.laborator.lab7.bean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.laborator.lab7.entity.User;
import org.laborator.lab7.repository.UserRepository;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class AuthenticationBean implements Serializable {
    private String username;
    private String password;
    private String role;
    private boolean loggedIn;
    private User loggedUser;

    private static final Logger logger = Logger.getLogger(AuthenticationBean.class.getName());

    @PersistenceContext(unitName = "Lab7PU")
    private EntityManager em;

    @Inject
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab7PU");
        em = emf.createEntityManager();
    }

//    public String login() {
//        try {
//            User user = userRepository.findUserByUsername(username);
//
//            if (user != null) {
//                String storedPassword = user.getPassword();
//                role = user.getRole();
//                logger.info("User role: " + role);
//
//                if (password.equals(storedPassword)) {
//                    loggedIn = true;
//                    loggedUser = user;
//                    return switch (role) {
//                        case "admin" -> "adminDashboard";
//                        case "teacher" -> "teacherDashboard";
//                        case "student" -> "studentDashboard";
//                        default -> "login";
//                    };
//                }
//            }
//
//            logger.info("Login failed! Username taken?: " + userRepository.isUsernameTaken(username));
//            loggedIn = false;
//            return "login";
//        } catch (Exception e) {
//            logger.info("Login failed: " + e.getMessage());
//            loggedIn = false;
//            return "login";
//        }
//    }

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try{
            logger.info("Login attempt: " + username + " password:" + password);
            DataSource dataSource = (DataSource) externalContext.getApplicationMap().get("jdbc/Lab7PU");
            request.login(username, password);
            externalContext.log("Login successful: " + username);
            loggedUser = userRepository.findUserByUsername(username);
            if(request.isUserInRole("admin"))
            {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../admin/adminDashboard.xhtml");
            }
            else if(request.isUserInRole("teacher"))
            {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../teacher/teacherDashboard.xhtml");
            }
            else if(request.isUserInRole("student"))
            {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../student/studentDashboard.xhtml");
            }
        }catch (ServletException | RuntimeException | IOException e) {
            ErrorMessage(e);
        }
    }

    public void logout() {
        loggedIn = false;
        username = null;
        password = null;
        role = null;
        loggedUser = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();
        try {
            request.logout();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../common/login.xhtml");
        } catch (ServletException | IOException e) {
            ErrorMessage(e);
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public User getLoggedUser() {
        return loggedUser;
    }
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void ErrorMessage(Exception e) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(),"Error: " + e.getMessage());
        logger.log(Level.SEVERE, e.toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
