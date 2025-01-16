package org.laborator.lab7.event;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import org.laborator.lab7.entity.EventStore;

import java.util.List;

@Stateless
public class EventManager {
    @PersistenceContext(unitName = "Lab7PU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab7PU");
        em = emf.createEntityManager();
    }
    public void saveEvent(String eventType, String payload) {
        EventStore event = new EventStore(eventType, payload);
        em.getTransaction().begin();
        em.persist(event);
        em.getTransaction().commit();
    }

    public List<EventStore> getAllEvents() {
        return em.createQuery("SELECT e FROM EventStore e", EventStore.class).getResultList();
    }
}
