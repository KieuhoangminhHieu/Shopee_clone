package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, String> {
    Optional<Voucher> findByCode(String code);
}