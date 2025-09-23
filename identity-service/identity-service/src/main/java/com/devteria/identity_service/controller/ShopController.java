package com.devteria.identity_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.service.ShopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    public ApiResponse<ShopResponse> createShop(@RequestBody @Valid ShopCreationRequest request) {
        return ApiResponse.<ShopResponse>builder()
                .result(shopService.createShop(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ShopResponse>> getShops() {
        return ApiResponse.<List<ShopResponse>>builder()
                .result(shopService.getShops())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ShopResponse> getShopById(@PathVariable String id) {
        return ApiResponse.<ShopResponse>builder()
                .result(shopService.getShopById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ShopResponse> updateShop(
            @PathVariable String id, @RequestBody @Valid ShopUpdateRequest request) {
        return ApiResponse.<ShopResponse>builder()
                .result(shopService.updateShop(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteShop(@PathVariable String id) {
        shopService.deleteShop(id);
        return ApiResponse.<Void>builder().build();
    }
}
