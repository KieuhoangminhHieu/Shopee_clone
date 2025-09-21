package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.PaymentResponse;
import com.devteria.identity_service.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}