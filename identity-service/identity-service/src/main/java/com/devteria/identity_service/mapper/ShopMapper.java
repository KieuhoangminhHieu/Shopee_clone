package com.devteria.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.entity.Shop;

@Mapper(componentModel = "spring")
public interface ShopMapper {
    @Mapping(source = "address", target = "address")
    Shop toEntity(ShopCreationRequest request);

    ShopResponse toResponse(Shop shop);

    void updateEntityFromDto(ShopUpdateRequest request, @MappingTarget Shop shop);
}
