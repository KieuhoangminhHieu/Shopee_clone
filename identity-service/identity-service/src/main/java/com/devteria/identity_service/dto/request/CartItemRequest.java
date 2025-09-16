package com.devteria.identity_service.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private String productId;
    private int quantity;
}
