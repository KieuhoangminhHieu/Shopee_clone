package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.response.CartResponse;
import com.devteria.identity_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ApiResponse<CartResponse> getCart(@PathVariable String userId) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCartByUser(userId))
                .build();
    }

    @DeleteMapping("/{userId}/clear")
    public ApiResponse<Void> clearCart(@PathVariable String userId) {
        cartService.clearCartByUserId(userId);
        return ApiResponse.<Void>builder().build();
    }
}
