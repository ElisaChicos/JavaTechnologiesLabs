package org.laborator.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateful
@Interceptors(ExecutionTimeInterceptor.class)
public class OrderService implements Serializable {

    private Order order;
    private List<OrderItem> items = new ArrayList<>();

    @PersistenceContext(unitName = "Lab5PU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab5PU");
        em = emf.createEntityManager();
    }

    public void createOrder(Client client) {
        this.order = new Order();
        this.order.setClient(client);
        this.items.clear();
        System.out.println("Order created for client: " + client.getName());
    }

    public void addItem(Product product, int quantity) {
        if (product != null && quantity > 0) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            items.add(item);
        }
    }

    @Transactional
    public void completeOrder() {
        if (order != null && !items.isEmpty()) {
            items.forEach(item -> {
                Product product = item.getProduct();
                if (product != null) {
                    product.setQuantity(product.getQuantity() - item.getQuantity());
                    em.persist(item);
                }
            });
            em.persist(order);
            System.out.println("Order completed with items count: " + items.size());
        } else {
            System.out.println("Order or items are null or empty.");
            throw new IllegalStateException("Order or items are null or empty.");
        }
    }

    public List<OrderItem> getOrderItems() {
        return items;
    }
}
