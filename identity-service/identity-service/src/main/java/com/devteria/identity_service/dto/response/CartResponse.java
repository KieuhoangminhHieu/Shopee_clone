package com.devteria.identity_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private String cartId;
    private String userId;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;
}
