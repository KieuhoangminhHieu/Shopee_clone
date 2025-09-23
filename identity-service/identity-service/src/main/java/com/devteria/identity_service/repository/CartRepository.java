package com.devteria.identity_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser_Id(String userId);
}
