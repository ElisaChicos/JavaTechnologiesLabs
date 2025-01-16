package org.laborator.lab7.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.laborator.lab7.entity.EvaluationConfig;

import java.io.Serializable;

@Stateless
public class EvaluationConfigRepository implements Serializable {
    @PersistenceContext(unitName = "Lab7PU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab7PU");
        em = emf.createEntityManager();
    }

    public EvaluationConfig getConfig() {
        return em.createNamedQuery("EvaluationConfig.findConfig", EvaluationConfig.class)
                .getSingleResult();
    }

    @Transactional
    @RolesAllowed("admin")
    public void updateConfig(EvaluationConfig config) {
        try {
            em.getTransaction().begin();
            em.persist(config);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
    }
}
