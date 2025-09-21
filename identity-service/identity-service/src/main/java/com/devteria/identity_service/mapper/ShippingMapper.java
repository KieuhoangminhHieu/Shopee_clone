package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.ShippingResponse;
import com.devteria.identity_service.entity.Shipping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface ShippingMapper {
    ShippingResponse toResponse(Shipping shipping);
}