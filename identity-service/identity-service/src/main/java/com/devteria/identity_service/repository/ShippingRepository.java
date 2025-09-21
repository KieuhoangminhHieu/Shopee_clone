package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, String> {
    List<Shipping> findByOrderId(String orderId);
}