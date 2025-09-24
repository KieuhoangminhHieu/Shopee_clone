package com.devteria.identity_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

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

    BigDecimal discountValue;

    BigDecimal minOrderAmount;

    int usageLimit;
    int usedCount;

    LocalDateTime startDate;
    LocalDateTime endDate;

    boolean active;
}
