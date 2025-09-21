package com.devteria.identity_service.dto.request;

import lombok.Data;

@Data
public class ShippingRequest {
    private String orderId;
    private String address;
    private String receiverName;
    private String receiverPhone;
    private String shippingMethod;
}