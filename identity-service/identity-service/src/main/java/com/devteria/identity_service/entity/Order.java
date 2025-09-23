package com.devteria.identity_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import com.devteria.identity_service.enums.OrderStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items;

    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    private String voucherCode;
    private BigDecimal discountAmount; // số tiền được giảm

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
