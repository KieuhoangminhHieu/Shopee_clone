package com.devteria.identity_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devteria.identity_service.dto.request.PaymentRequest;
import com.devteria.identity_service.dto.response.PaymentResponse;
import com.devteria.identity_service.entity.Payment;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.PaymentMapper;
import com.devteria.identity_service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .success(true) // giả định thanh toán thành công
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByOrderId(String orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        if (payments.isEmpty()) {
            throw new AppException(ErrorCode.PAYMENT_NOT_FOUND);
        }
        return payments.stream().map(paymentMapper::toResponse).toList();
    }
}
