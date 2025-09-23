package com.devteria.identity_service.mapper;

import org.mapstruct.Mapper;

import com.devteria.identity_service.dto.response.ShippingResponse;
import com.devteria.identity_service.entity.Shipping;

@Mapper(componentModel = "Spring")
public interface ShippingMapper {
    ShippingResponse toResponse(Shipping shipping);
}
