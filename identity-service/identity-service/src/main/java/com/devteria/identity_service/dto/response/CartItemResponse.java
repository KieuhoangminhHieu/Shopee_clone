package com.devteria.identity_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private String id;
    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
