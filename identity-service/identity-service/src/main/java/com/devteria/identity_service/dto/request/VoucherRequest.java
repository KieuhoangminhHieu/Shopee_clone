package com.devteria.identity_service.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VoucherRequest {
    private String code;
    private String description;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private int usageLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}