package com.devteria.identity_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCart_IdAndProduct_Id(String cartId, String productId);

    List<CartItem> findByCart_Id(String cartId);

    void deleteAllByCart_Id(String cartId);
}
