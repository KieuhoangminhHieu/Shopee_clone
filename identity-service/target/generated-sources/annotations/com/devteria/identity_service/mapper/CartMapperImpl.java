package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.CartItemResponse;
import com.devteria.identity_service.dto.response.CartResponse;
import com.devteria.identity_service.entity.Cart;
import com.devteria.identity_service.entity.CartItem;
import com.devteria.identity_service.entity.Product;
import com.devteria.identity_service.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-24T13:35:15+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponse toCartResponse(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartResponse cartResponse = new CartResponse();

        cartResponse.setUserId( cartUserId( cart ) );
        cartResponse.setItems( itemsToResponses( cart.getItems() ) );
        cartResponse.setTotalPrice( cart.getTotalPrice() );

        return cartResponse;
    }

    @Override
    public CartItemResponse toCartItemResponse(CartItem item) {
        if ( item == null ) {
            return null;
        }

        CartItemResponse cartItemResponse = new CartItemResponse();

        cartItemResponse.setProductId( itemProductId( item ) );
        cartItemResponse.setProductName( itemProductName( item ) );
        cartItemResponse.setId( item.getId() );
        cartItemResponse.setQuantity( item.getQuantity() );
        cartItemResponse.setPrice( item.getPrice() );
        cartItemResponse.setSubTotal( item.getSubTotal() );

        return cartItemResponse;
    }

    private String cartUserId(Cart cart) {
        if ( cart == null ) {
            return null;
        }
        User user = cart.getUser();
        if ( user == null ) {
            return null;
        }
        String id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductId(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductName(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product product = cartItem.getProduct();
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
