package org.laborator.lab7;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@RequestScoped
public class RegisterUserBean implements Serializable {
    private String username;
    private String password;
    private String role;

    @Inject
    private UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(RegisterUserBean.class.getName());

    public String register() {
        if (userRepository.isUsernameTaken(username)) {
            logger.warning("Username already exists: " + username);
            return "register.xhtml?faces-redirect=true&error=usernameTaken";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        try {
            userRepository.saveUser(user);
            logger.info("User registered successfully: " + username);
            return "login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error during registration: " + e.getMessage());
            return "register.xhtml?faces-redirect=true&error=registrationFailed";
        }
    }

    // Getters and setters
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
}
