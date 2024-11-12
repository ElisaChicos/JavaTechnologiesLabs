package org.laborator.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@RequestScoped
public class ProductRepository {

    EntityManager em;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab5PU");
        em = emf.createEntityManager();
    }
    public List<Product> findAll() {
        return em.createNamedQuery("Product.findAll", Product.class).getResultList();
    }

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    @Transactional
    public void save(Product product) {
        if (product.getId() == null) {
            em.persist(product); // New product
        } else {
            em.merge(product); // Update existing product
        }
    }

    @Transactional
    public void delete(Product product) {
        Product toRemove = em.find(Product.class, product.getId());
        if (toRemove != null) {
            em.remove(toRemove);
        }
    }
}
