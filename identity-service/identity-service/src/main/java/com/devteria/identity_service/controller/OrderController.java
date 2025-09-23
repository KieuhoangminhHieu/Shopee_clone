package com.devteria.identity_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.devteria.identity_service.dto.request.OrderRequest;
import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.response.OrderResponse;
import com.devteria.identity_service.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("Tạo đơn hàng thành công")
                .result(response)
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse response = orderService.getOrderById(orderId);
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("Thông tin đơn hàng")
                .result(response)
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<OrderResponse>> getOrdersByUser(@PathVariable String userId) {
        List<OrderResponse> responses = orderService.getOrdersByUser(userId);
        return ApiResponse.<List<OrderResponse>>builder()
                .code(200)
                .message("Danh sách đơn hàng của người dùng")
                .result(responses)
                .build();
    }
}
