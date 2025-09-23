package com.devteria.identity_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devteria.identity_service.dto.response.OrderItemResponse;
import com.devteria.identity_service.dto.response.OrderResponse;
import com.devteria.identity_service.entity.Order;
import com.devteria.identity_service.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponse toOrderItemResponse(OrderItem item);

    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> items);
}
