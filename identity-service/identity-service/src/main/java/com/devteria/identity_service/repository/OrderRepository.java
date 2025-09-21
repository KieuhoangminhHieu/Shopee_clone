package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_Id(String userId);
}