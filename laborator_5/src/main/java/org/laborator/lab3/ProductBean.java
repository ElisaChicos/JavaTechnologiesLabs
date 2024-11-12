package org.laborator.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named("productBean")
@RequestScoped
public class ProductBean {

    @Inject
    private ProductRepository productRepository;

    private List<Product> products;
    private Product currentItem;

    @PostConstruct
    public void init() {
        currentItem = new Product();
        loadProducts();
    }

    public boolean isProduct() {
        return true;
    }

    public boolean isClient() {
        return false;
    }

    private void loadProducts() {
        products = productRepository.findAll();
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getCurrentItem() {
        return currentItem;
    }

    public void prepareEdit(Product product) {
        this.currentItem = product;
    }

    public void prepareNewItem() {
        if (this.currentItem == null) {
            this.currentItem = new Product();
        }
    }

    public void saveItem() {
        productRepository.save(currentItem);
        loadProducts();
        currentItem = new Product();
    }

    public void removeProduct(Product product) {
        productRepository.delete(product);
        loadProducts();
    }

    public Product findProduct(Long id) {
        return productRepository.findById(id);
    }
}
