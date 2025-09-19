package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.entity.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    Shop toEntity(ShopCreationRequest request);

    ShopResponse toResponse(Shop shop);

    void updateEntityFromDto(ShopUpdateRequest request, @MappingTarget Shop shop);
}
