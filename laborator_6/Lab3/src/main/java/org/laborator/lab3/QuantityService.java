package org.laborator.lab3;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class QuantityService {

    @PersistenceContext(unitName = "Lab5PU")
    private EntityManager em;

    public int getProductquantity(Long productId) {
        Product product = em.find(Product.class, productId);
        return product != null ? product.getQuantity() : 0;
    }
}
