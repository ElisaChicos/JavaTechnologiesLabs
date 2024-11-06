package org.laborator.lab3;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Named("productBean")
@RequestScoped
public class ProductBean {
    private List<Product> products;
    private Product currentItem;
    @PostConstruct
    public void init() {
        products = new ArrayList<>();
        currentItem = new Product();
        loadProducts();
    }

    public boolean isProduct()
    {
        return true;
    }

    public boolean isClient()
    {
        return false;
    }

    private void loadProducts() {
        products.clear();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        if(this.currentItem == null) {
            this.currentItem = new Product();
        }
    }

    public void saveItem() {
        if (currentItem.getId() == null) {
            addProduct(currentItem);
        } else {
            updateProduct(currentItem);
        }
        loadProducts();
        currentItem = new Product();
    }
    private void addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setInt(3, product.getPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?  WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setInt(3, product.getPrice());
            pstmt.setLong(4, product.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}