package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.CartItemRequest;
import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.response.CartItemResponse;
import com.devteria.identity_service.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    // add/update by userId (auto create cart)
    @PostMapping("/carts/{userId}/items")
    public ApiResponse<CartItemResponse> addOrUpdateByUser(
            @PathVariable String userId,
            @RequestBody @Valid CartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .result(cartItemService.addOrUpdateCartItemByUser(userId, request))
                .build();
    }

    // add/update by cartId (if you prefer to provide cartId)
    @PostMapping("/cart-items")
    public ApiResponse<CartItemResponse> addOrUpdateByCartId(
            @RequestParam String cartId,
            @RequestBody @Valid CartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .result(cartItemService.addOrUpdateCartItemByCartId(cartId, request))
                .build();
    }

    @DeleteMapping("/carts/{userId}/items/{productId}")
    public ApiResponse<Void> removeByUser(@PathVariable String userId, @PathVariable String productId) {
        cartItemService.removeCartItemByUser(userId, productId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/cart-items")
    public ApiResponse<Void> removeByCartId(@RequestParam String cartId, @RequestParam String productId) {
        cartItemService.removeCartItemByCartId(cartId, productId);
        return ApiResponse.<Void>builder().build();
    }
}
