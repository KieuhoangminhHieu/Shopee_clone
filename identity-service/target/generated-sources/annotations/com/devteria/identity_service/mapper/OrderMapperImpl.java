package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.OrderItemResponse;
import com.devteria.identity_service.dto.response.OrderResponse;
import com.devteria.identity_service.entity.Order;
import com.devteria.identity_service.entity.OrderItem;
import com.devteria.identity_service.entity.Product;
import com.devteria.identity_service.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-22T12:56:02+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toOrderResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.orderId( order.getId() );
        orderResponse.userId( orderUserId( order ) );
        orderResponse.totalPrice( order.getTotalPrice() );
        orderResponse.status( order.getStatus() );
        orderResponse.createdAt( order.getCreatedAt() );
        orderResponse.items( toOrderItemResponses( order.getItems() ) );
        orderResponse.voucherCode( order.getVoucherCode() );
        orderResponse.discountAmount( order.getDiscountAmount() );

        return orderResponse.build();
    }

    @Override
    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        OrderItemResponse.OrderItemResponseBuilder orderItemResponse = OrderItemResponse.builder();

        orderItemResponse.productId( itemProductId( item ) );
        orderItemResponse.productName( itemProductName( item ) );
        orderItemResponse.quantity( item.getQuantity() );
        orderItemResponse.price( item.getPrice() );
        orderItemResponse.subTotal( item.getSubTotal() );

        return orderItemResponse.build();
    }

    @Override
    public List<OrderItemResponse> toOrderItemResponses(List<OrderItem> items) {
        if ( items == null ) {
            return null;
        }

        List<OrderItemResponse> list = new ArrayList<OrderItemResponse>( items.size() );
        for ( OrderItem orderItem : items ) {
            list.add( toOrderItemResponse( orderItem ) );
        }

        return list;
    }

    private String orderUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        String id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductName(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
