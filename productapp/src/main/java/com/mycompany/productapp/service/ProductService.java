package com.mycompany.productapp.service;

import com.mycompany.productapp.entity.Product;

public interface ProductService {

    // Create
    Product createProduct(Product product);

    // Read
    Iterable<Product> getAllProducts();
    Product getProductById(Integer id);

    // Update
    Product updateProduct(Integer id, Product product);

    // Delete
    void deleteProduct(Integer id);
}
