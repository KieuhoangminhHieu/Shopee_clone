package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCart_IdAndProduct_Id(String cartId, String productId);
    List<CartItem> findByCart_Id(String cartId);
    void deleteAllByCart_Id(String cartId);
}
