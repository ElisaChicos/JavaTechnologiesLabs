package org.laborator.lab7;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class EvaluationRepository {
    @PersistenceContext(unitName = "Lab7PU")
    private EntityManager em;

    private static final Logger logger = Logger.getLogger(EvaluationRepository.class.getName());

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab7PU");
        em = emf.createEntityManager();
    }
    @Transactional
    @LogExecution
    public void saveEvaluation(Evaluation evaluation) {
        try {
            logger.log(Level.INFO, "Reg Num: {0}", evaluation.getRegistrationNumber());
            em.getTransaction().begin();
            em.persist(evaluation);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public List<Evaluation> getEvaluationsForTeacher(Long teacherId) {
        return em.createNamedQuery("Evaluation.findByTeacherId", Evaluation.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    public List<Evaluation> getEvaluationsByStudentId(Long studentId) {
        return em.createNamedQuery("Evaluation.findByStudentId", Evaluation.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    public List<Evaluation> getAllEvaluations() {
        return em.createNamedQuery("Evaluation.findAllEvaluations", Evaluation.class)
                .getResultList();
    }
}
