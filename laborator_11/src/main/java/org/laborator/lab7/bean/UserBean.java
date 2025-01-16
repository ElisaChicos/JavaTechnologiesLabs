package org.laborator.lab7.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.laborator.lab7.entity.User;
import org.laborator.lab7.repository.UserRepository;

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
