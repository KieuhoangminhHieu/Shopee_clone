package com.devteria.identity_service.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String orderId;
    private String paymentMethod;
    private BigDecimal amount;
}