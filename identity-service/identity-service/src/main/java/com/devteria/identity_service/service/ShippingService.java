package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.request.ShippingRequest;
import com.devteria.identity_service.dto.response.ShippingResponse;
import com.devteria.identity_service.entity.Shipping;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.ShippingMapper;
import com.devteria.identity_service.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;

    public ShippingResponse createShipping(ShippingRequest request) {
        Shipping shipping = Shipping.builder()
                .orderId(request.getOrderId())
                .address(request.getAddress())
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .shippingMethod(request.getShippingMethod())
                .trackingNumber("TRK" + System.currentTimeMillis()) // giả lập mã vận đơn
                .shippedAt(LocalDateTime.now())
                .delivered(false)
                .build();

        shippingRepository.save(shipping);
        return shippingMapper.toResponse(shipping);
    }

    public List<ShippingResponse> getShippingByOrderId(String orderId) {
        List<Shipping> shippings = shippingRepository.findByOrderId(orderId);
        if (shippings.isEmpty()) {
            throw new AppException(ErrorCode.SHIPPING_NOT_FOUND);
        }
        return shippings.stream().map(shippingMapper::toResponse).toList();
    }
}