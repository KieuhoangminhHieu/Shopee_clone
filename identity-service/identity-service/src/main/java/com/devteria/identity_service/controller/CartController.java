package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.request.CartItemRequest;
import com.devteria.identity_service.entity.Cart;
import com.devteria.identity_service.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping("/{userId}/items")
    public ApiResponse<Cart> addItemToCart(@PathVariable String userId,
                                           @RequestBody CartItemRequest request) {
        ApiResponse<Cart> response = new ApiResponse<>();
        response.setResult(cartService.addItemToCart(userId, request));
        return response;
    }

    @GetMapping("/{userId}")
    public ApiResponse<Cart> getCart(@PathVariable String userId) {
        ApiResponse<Cart> response = new ApiResponse<>();
        response.setResult(cartService.getCartByUser(userId));
        return response;
    }
}
