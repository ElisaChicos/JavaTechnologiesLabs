package org.laborator.lab7;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.function.Supplier;
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

    public String login() {
        try {
            User user = userRepository.findUserByUsername(username);

            if (user != null) {
                String storedPassword = user.getPassword();
                role = user.getRole();
                logger.info("User role: " + role);

                if (password.equals(storedPassword)) {
                    loggedIn = true;
                    loggedUser = user;
                    return switch (role) {
                        case "admin" -> "adminDashboard";
                        case "teacher" -> "teacherDashboard";
                        case "student" -> "studentDashboard";
                        default -> "login";
                    };
                }
            }

            logger.info("Login failed! Username taken?: " + userRepository.isUsernameTaken(username));
            loggedIn = false;
            return "login";
        } catch (Exception e) {
            logger.info("Login failed: " + e.getMessage());
            loggedIn = false;
            return "login";
        }
    }

    public String logout() {
        loggedIn = false;
        username = null;
        password = null;
        role = null;
        return "login.xhtml?faces-redirect=true";
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

}
