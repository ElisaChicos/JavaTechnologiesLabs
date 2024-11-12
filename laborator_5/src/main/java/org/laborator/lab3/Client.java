package org.laborator.lab3;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
@NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String date_of_birth;
    private String email;
    private String phone_number;

    public Client() {}

    public Client(Long id, String name, String date_of_birth, String email, String phone_number) {
        this.id = id;
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.phone_number = phone_number;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate_of_birth() { return date_of_birth; }
    public void setDate_of_birth(String date_of_birth) { this.date_of_birth = date_of_birth; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone_number() { return phone_number; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }
}
