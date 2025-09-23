package com.devteria.identity_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devteria.identity_service.dto.request.OrderItemRequest;
import com.devteria.identity_service.dto.request.OrderRequest;
import com.devteria.identity_service.dto.response.OrderResponse;
import com.devteria.identity_service.entity.*;
import com.devteria.identity_service.enums.OrderStatus;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.OrderMapper;
import com.devteria.identity_service.repository.OrderRepository;
import com.devteria.identity_service.repository.ProductRepository;
import com.devteria.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final VoucherService voucherService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            BigDecimal subTotal = itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(subTotal);

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(itemRequest.getPrice())
                    .subTotal(subTotal)
                    .build();

            orderItems.add(item);
        }

        BigDecimal discount = BigDecimal.ZERO;
        String voucherCode = null;

        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            Voucher voucher = voucherService.validateVoucher(request.getVoucherCode(), total);
            discount = voucher.getDiscountValue();
            voucherCode = voucher.getCode();

            total = total.subtract(discount);
            if (total.compareTo(BigDecimal.ZERO) < 0) {
                total = BigDecimal.ZERO;
            }
        }

        Order order = Order.builder()
                .user(user)
                .items(new ArrayList<>()) // tạm thời rỗng, sẽ set sau
                .totalPrice(total)
                .discountAmount(discount)
                .voucherCode(voucherCode)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }

        order.setItems(orderItems);
        orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(String userId) {
        userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Order> orders = orderRepository.findByUser_Id(userId);
        return orders.stream().map(orderMapper::toOrderResponse).toList();
    }
}
