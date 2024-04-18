package com.mycompany.productapp;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.mycompany.productapp.entity.Product;
import com.mycompany.productapp.repository.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepoTest {
    @Autowired ProductRepository repo;

    // Create
    @Test
    public void testAddNew(){
        Product newProduct = Product.builder()
        .name("Nokia")
        .price(10000).build();

        repo.save(newProduct);

        Assertions.assertThat(newProduct).isNotNull();
        Assertions.assertThat(newProduct.getId()).isGreaterThan(0);
    }

    // Read
    @Test
    public void testFindAll() {
        Iterable<Product> products = repo.findAll();

        Assertions.assertThat(products).hasSizeGreaterThan(0);

        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    public void testFindById() {
        Optional<Product> productDB = repo.findById(1);

        Assertions.assertThat(productDB).isPresent();

        System.out.println(productDB);
    }

    // Update
    @Test
    public void testUpdate() {
        Optional<Product> optProduct = repo.findById(1);

        Product product = optProduct.get();

        product.setName("Nokia Immortal");

        repo.save(product);

        Product updateProduct = repo.findById(1).get();

        Assertions.assertThat(updateProduct.getName()).isEqualTo("Nokia Immortal");
    }

    // Delete
    @Test
    public void testDelete() {
        repo.deleteById(1);

        Optional<Product> optProduct = repo.findById(1);

        Assertions.assertThat(optProduct).isNotPresent();
    }
}
