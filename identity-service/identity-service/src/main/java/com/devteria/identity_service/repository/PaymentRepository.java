package com.devteria.identity_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByOrderId(String orderId);
}
