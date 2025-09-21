package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.ShippingResponse;
import com.devteria.identity_service.entity.Shipping;
import org.springframework.stereotype.Component;

@Component
public class ShippingMapper {

    public ShippingResponse toResponse(Shipping shipping) {
        return ShippingResponse.builder()
                .shippingId(shipping.getId())
                .orderId(shipping.getOrderId())
                .address(shipping.getAddress())
                .receiverName(shipping.getReceiverName())
                .receiverPhone(shipping.getReceiverPhone())
                .shippingMethod(shipping.getShippingMethod())
                .trackingNumber(shipping.getTrackingNumber())
                .shippedAt(shipping.getShippedAt())
                .delivered(shipping.isDelivered())
                .build();
    }
}