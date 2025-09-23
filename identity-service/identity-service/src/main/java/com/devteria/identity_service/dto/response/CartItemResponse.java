package com.devteria.identity_service.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemResponse {
    private String id;
    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
