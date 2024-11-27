package org.laborator.lab3;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("orderBean")
@SessionScoped
public class OrderBean implements Serializable {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private OrderService orderService;

    private Map<Long, String> orderQuantities = new HashMap<>();

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Map<Long, String> getOrderQuantities() {
        return orderQuantities;
    }

    public void prepareOrder(Client client) {
        orderService.createOrder(client);
    }

    public List<OrderItem> getOrderItems() {
        return orderService.getOrderItems();
    }

    public String completeOrder() {
        orderService.completeOrder();
        return "/index.xhtml";
    }

    public String loadOrderItems() {
        orderQuantities.forEach((productId, quantityStr) -> {
            if (quantityStr != null && !quantityStr.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    Product product = productRepository.findById(productId);
                    if (quantity > 0 && product != null) {
                        orderService.addItem(product, quantity);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity for product ID " + productId + ": " + quantityStr);
                }
            }
        });
        if (orderService.getOrderItems().isEmpty()) {
            System.out.println("No items added to order.");
            throw new IllegalStateException("Order or items are null or empty.");
        }
        return "/orderConfirmation.xhtml";
    }

}
