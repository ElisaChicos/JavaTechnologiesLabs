package org.laborator.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@RequestScoped
public class ClientRepository {

    private EntityManager em;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab5PU");
        em = emf.createEntityManager();
    }

    public List<Client> findAll() {
        if (em == null) {
            System.out.println("EntityManager injection failed in ClientRepository.");
            throw new IllegalStateException("EntityManager is null.");
        }
        return em.createNamedQuery("Client.findAll", Client.class).getResultList();
    }


    public Client findById(Long id) {
        return em.find(Client.class, id);
    }

    @Transactional
    public void save(Client client) {
        if (client.getId() == null) {
            em.persist(client); // New client
        } else {
            em.merge(client); // Update existing client
        }
    }

    @Transactional
    public void delete(Client client) {
        Client toRemove = em.find(Client.class, client.getId());
        if (toRemove != null) {
            em.remove(toRemove);
        }
    }
}
