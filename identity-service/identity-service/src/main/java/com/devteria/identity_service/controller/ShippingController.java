package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.ShippingRequest;
import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.response.ShippingResponse;
import com.devteria.identity_service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shippings")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShippingResponse>> createShipping(@RequestBody ShippingRequest request) {
        ShippingResponse response = shippingService.createShipping(request);
        return ResponseEntity.ok(ApiResponse.<ShippingResponse>builder()
                .result(response)
                .message("Tạo thông tin vận chuyển thành công")
                .build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<ShippingResponse>>> getShippingByOrder(@PathVariable String orderId) {
        List<ShippingResponse> responses = shippingService.getShippingByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.<List<ShippingResponse>>builder()
                .result(responses)
                .message("Danh sách vận chuyển theo đơn hàng")
                .build());
    }
}