package com.devteria.identity_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.devteria.identity_service.dto.response.CartItemResponse;
import com.devteria.identity_service.dto.response.CartResponse;
import com.devteria.identity_service.entity.Cart;
import com.devteria.identity_service.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items", qualifiedByName = "itemsToResponses")
    CartResponse toCartResponse(Cart cart);

    @Named("itemsToResponses")
    default List<CartItemResponse> itemsToResponses(List<CartItem> items) {
        return items.stream().map(this::toCartItemResponse).toList();
    }

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "id", target = "id")
    CartItemResponse toCartItemResponse(CartItem item);
}
