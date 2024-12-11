package org.laborator.lab7;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class UserBean {
    @Inject
    private UserRepository userRepository;

    public List<User> getAllTeachers() {
        return userRepository.getAllTeachers();
    }
}
