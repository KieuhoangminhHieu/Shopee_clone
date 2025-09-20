package com.devteria.identity_service.dto.request;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
public class CartItemRequest {
    @NotBlank
    private String productId;

    @Min(1)
    private int quantity;
}
