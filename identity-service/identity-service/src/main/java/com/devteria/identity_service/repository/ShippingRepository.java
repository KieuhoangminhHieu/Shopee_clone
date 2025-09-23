package com.devteria.identity_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entity.Shipping;

public interface ShippingRepository extends JpaRepository<Shipping, String> {
    List<Shipping> findByOrderId(String orderId);
}
