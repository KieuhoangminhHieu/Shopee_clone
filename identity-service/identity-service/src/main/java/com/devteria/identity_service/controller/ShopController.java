package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.service.ShopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShopController {
    ShopService shopService;

    @PostMapping
    public ApiResponse<Shop> createShop(@RequestBody Shop shop) {
        ApiResponse<Shop> response = new ApiResponse<>();
        response.setResult(shopService.createShop(shop));
        return response;
    }

    @GetMapping
    public ApiResponse<List<Shop>> getShops() {
        ApiResponse<List<Shop>> response = new ApiResponse<>();
        response.setResult(shopService.getShops());
        return response;
    }

    @GetMapping("/{shopId}")
    public ApiResponse<Shop> getShop(@PathVariable String shopId) {
        ApiResponse<Shop> response = new ApiResponse<>();
        response.setResult(shopService.getShopById(shopId));
        return response;
    }

    @PutMapping("/{shopId}")
    public ApiResponse<Shop> updateShop(@PathVariable String shopId, @RequestBody Shop shop) {
        ApiResponse<Shop> response = new ApiResponse<>();
        response.setResult(shopService.updateShop(shopId, shop));
        return response;
    }

    @DeleteMapping("/{shopId}")
    public ApiResponse<String> deleteShop(@PathVariable String shopId) {
        shopService.deleteShop(shopId);
        ApiResponse<String> response = new ApiResponse<>();
        response.setResult("Shop deleted successfully");
        return response;
    }
}
