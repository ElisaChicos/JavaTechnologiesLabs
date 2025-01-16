package org.laborator.lab7.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.laborator.lab7.entity.Evaluation;
import org.laborator.lab7.interceptor.LogExecution;

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

    /// WRITE OPERATIONS

    @Transactional
    @LogExecution
    @RolesAllowed("student")
    public void saveEvaluation(Evaluation evaluation) {
        try {
            logger.log(Level.INFO, "Reg Num: {0}", evaluation.getRegistrationNumber());

            em.getTransaction().begin();
            em.persist(evaluation);
            em.getTransaction().commit();
            logger.log(Level.INFO, "Evaluation Saved!");
        }
        catch (Exception e) {
            em.getTransaction().rollback();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @RolesAllowed("admin")
    public void updateEvaluation(Evaluation evaluation) {
        try{
            em.getTransaction().begin();
            em.merge(evaluation);
            em.getTransaction().commit();
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    @RolesAllowed("admin")
    public void deleteEvaluation(Long id) {
        try {
            Evaluation evaluation = findById(id);
            em.getTransaction().begin();
            if (evaluation != null) {
                em.remove(evaluation);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }


    ///  READ OPERATIONS

    @RolesAllowed({"admin","teacher"})
    public List<Evaluation> getEvaluationsForTeacher(Long teacherId) {
        return em.createNamedQuery("Evaluation.findByTeacherId", Evaluation.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }
    @RolesAllowed({"admin","student"})
    public List<Evaluation> getEvaluationsByStudentId(Long studentId) {
        return em.createNamedQuery("Evaluation.findByStudentId", Evaluation.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }
    @RolesAllowed("admin")
    public List<Evaluation> getAllEvaluations() {
        return em.createNamedQuery("Evaluation.findAllEvaluations", Evaluation.class)
                .getResultList();
    }

    public Evaluation findById(Long id) {
        return em.find(Evaluation.class, id);
    }

    public int getEvaluationCountForUser(Long userId) {
        return ((Number) em.createQuery(
                        "SELECT COUNT(e) FROM Evaluation e WHERE e.student.id = :userId")
                .setParameter("userId", userId)
                .getSingleResult()).intValue();
    }
}
