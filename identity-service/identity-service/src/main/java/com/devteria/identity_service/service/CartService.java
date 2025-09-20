package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.response.CartResponse;
import com.devteria.identity_service.entity.Cart;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.CartMapper;
import com.devteria.identity_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartResponse getCartByUser(String userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        return cartMapper.toCartResponse(cart);
    }

    public void clearCartByUserId(String userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    // helper if you need find/create cart by user
    public Cart findOrCreateCartForUser(com.devteria.identity_service.entity.User user) {
        return cartRepository.findByUser_Id(user.getId()).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            c.setTotalPrice(BigDecimal.ZERO);
            return cartRepository.save(c);
        });
    }
}
