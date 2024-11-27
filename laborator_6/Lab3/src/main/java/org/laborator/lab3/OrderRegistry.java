package org.laborator.lab3;

import jakarta.ejb.Singleton;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class OrderRegistry {

    private Map<Long, Order> orderMap;

    @PostConstruct
    public void init() {
        orderMap = new HashMap<>();
    }

    public void registerOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    public Map<Long, Order> getOrders() {
        return orderMap;
    }
}
