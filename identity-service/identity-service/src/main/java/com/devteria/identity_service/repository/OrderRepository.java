package com.devteria.identity_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_Id(String userId);
}
