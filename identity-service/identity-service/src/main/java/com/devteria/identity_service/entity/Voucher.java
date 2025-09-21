package com.devteria.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String code;

    String description;

    BigDecimal discountValue; // số tiền giảm

    BigDecimal minOrderAmount; // điều kiện áp dụng

    int usageLimit; // tổng số lần được dùng
    int usedCount;  // số lần đã dùng

    LocalDateTime startDate;
    LocalDateTime endDate;

    boolean active;
}