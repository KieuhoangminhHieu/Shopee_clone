package com.devteria.identity_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class VoucherResponse {
    private String voucherId;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private int usageLimit;
    private int usedCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
}