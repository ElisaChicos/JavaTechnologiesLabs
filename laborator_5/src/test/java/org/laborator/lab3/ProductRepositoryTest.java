package org.laborator.lab3;

import org.junit.jupiter.api.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepositoryTest {

    private ProductRepository productRepository;
    private EntityManager em;

    @BeforeAll
    public void setup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab5PU-Test");
        em = emf.createEntityManager();

        productRepository = new ProductRepository();
        productRepository.em = em;
    }

    @BeforeEach
    public void initTransaction() {
        em.getTransaction().begin();
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product(null, "Sample Product", "Sample Description", 200);
        productRepository.save(product);
        assertNotNull(product.getId());
    }

    @Test
    public void testFindProductById() {
        Product product = new Product(null, "Test Product", "Test Description", 100);
        productRepository.save(product);

        Product foundProduct = productRepository.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product(null, "Original Product", "Original Description", 200);
        productRepository.save(product);

        product.setPrice(150);
        productRepository.save(product); // Use the save method to update

        Product updatedProduct = productRepository.findById(product.getId());
        assertEquals(150, updatedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product(null, "To Be Deleted", "Description", 100);
        productRepository.save(product);

        productRepository.delete(product);
        Product deletedProduct = productRepository.findById(product.getId());
        assertNull(deletedProduct);
    }

    @AfterEach
    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @AfterAll
    public void cleanup() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
