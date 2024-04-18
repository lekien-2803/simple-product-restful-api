package com.mycompany.productapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.productapp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
