package com.devteria.identity_service.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private String paymentMethod;
    private BigDecimal amount;
}
