package com.mycompany.productapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.productapp.entity.Product;
import com.mycompany.productapp.exception.ProductNotFoundException;
import com.mycompany.productapp.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired ProductRepository repo;

    // Create
    @Override
    public Product createProduct(Product product) {

        return repo.save(product);
    }

    // Read
    @Override
    public Iterable<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product getProductById(Integer id) throws ProductNotFoundException{
        Optional<Product> optProduct =  repo.findById(id);

        if (optProduct.isPresent()) {
            return optProduct.get();
        }

        throw new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + id);
    }

    // Update
    @Override
    public Product updateProduct(Integer id, Product product) {
        Product productDB = getProductById(id);

        productDB.setName(product.getName());
        productDB.setPrice(product.getPrice());

        return repo.save(productDB);
    }

    // Delete
    @Override
    public void deleteProduct(Integer id) {
        Product product = getProductById(id);

        repo.delete(product);
    }

}
