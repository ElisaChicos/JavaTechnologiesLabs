package org.laborator.lab7.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT c FROM User c")
@NamedQuery(name = "User.findUser", query = "SELECT c FROM User c WHERE c.username = :username")
@NamedQuery(name = "User.findAllTeachers", query = "SELECT c FROM User c WHERE c.role = 'teacher'")
@NamedQuery(name = "User.findAllStudents", query = "SELECT c FROM User c WHERE c.role = 'student'")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    private int counter;

    public User() {}
    public User(Long id, String username, String password, String role, int counter) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.counter = counter;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public int getCounter() {return counter;}
    public void setCounter(int counter) {this.counter = counter;}
}
