package com.devteria.identity_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String paymentId;
    private String orderId;
    private String paymentMethod;
    private BigDecimal amount;
    private boolean success;
    private LocalDateTime paidAt;
}
