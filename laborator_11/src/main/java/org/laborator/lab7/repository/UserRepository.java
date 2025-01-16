package org.laborator.lab7.repository;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.laborator.lab7.entity.User;

import java.io.Serializable;
import java.util.List;

@Stateless
public class UserRepository implements Serializable {

    @PersistenceContext(unitName = "Lab7PU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab7PU");
        em = emf.createEntityManager();
    }

    public User findUserByUsername(String username) {
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findUser", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public User findUserById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> getAllTeachers() {
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findAllTeachers", User.class);
            return query.getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<User> getAllStudents() {
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findAllStudents", User.class);
            return query.getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }


    public boolean isUsernameTaken(String username) {
        return findUserByUsername(username) != null;
    }

    @Transactional
    public void saveUser(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
    }
}
