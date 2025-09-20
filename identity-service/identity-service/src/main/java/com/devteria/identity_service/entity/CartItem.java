package com.devteria.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    // snapshot price tại thời điểm thêm (BigDecimal)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    // price * quantity
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subTotal;
}
