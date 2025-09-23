package com.devteria.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {}
