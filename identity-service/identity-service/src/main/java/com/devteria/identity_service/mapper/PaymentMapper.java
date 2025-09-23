package com.devteria.identity_service.mapper;

import org.mapstruct.Mapper;

import com.devteria.identity_service.dto.response.PaymentResponse;
import com.devteria.identity_service.entity.Payment;

@Mapper(componentModel = "Spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
