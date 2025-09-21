package com.devteria.identity_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShippingResponse {
    private String shippingId;
    private String orderId;
    private String address;
    private String receiverName;
    private String receiverPhone;
    private String shippingMethod;
    private String trackingNumber;
    private LocalDateTime shippedAt;
    private boolean delivered;
}