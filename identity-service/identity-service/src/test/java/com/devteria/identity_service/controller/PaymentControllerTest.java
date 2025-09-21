package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.PaymentRequest;
import com.devteria.identity_service.dto.response.PaymentResponse;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId("order789");
        paymentRequest.setPaymentMethod("VNPAY");
        paymentRequest.setAmount(BigDecimal.valueOf(300000));

        paymentResponse = PaymentResponse.builder()
                .paymentId("pay001")
                .orderId("order789")
                .paymentMethod("VNPAY")
                .amount(BigDecimal.valueOf(300000))
                .success(true)
                .paidAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void processPayment_success() throws Exception {
        Mockito.when(paymentService.processPayment(Mockito.any())).thenReturn(paymentResponse);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Thanh toán thành công"))
                .andExpect(jsonPath("$.result.paymentId").value("pay001"))
                .andExpect(jsonPath("$.result.orderId").value("order789"))
                .andExpect(jsonPath("$.result.success").value(true));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void getPaymentsByOrder_success() throws Exception {
        Mockito.when(paymentService.getPaymentsByOrderId("order789")).thenReturn(List.of(paymentResponse));

        mockMvc.perform(get("/payments/order/order789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Danh sách thanh toán theo đơn hàng"))
                .andExpect(jsonPath("$.result[0].paymentId").value("pay001"))
                .andExpect(jsonPath("$.result[0].orderId").value("order789"));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void getPaymentsByOrder_notFound() throws Exception {
        Mockito.when(paymentService.getPaymentsByOrderId("notfound"))
                .thenThrow(new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        mockMvc.perform(get("/payments/order/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.PAYMENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.PAYMENT_NOT_FOUND.getMessage()));
    }
}