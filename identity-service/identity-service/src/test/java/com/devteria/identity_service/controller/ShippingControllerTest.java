package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.ShippingRequest;
import com.devteria.identity_service.dto.response.ShippingResponse;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.service.ShippingService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShippingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShippingService shippingService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShippingRequest shippingRequest;
    private ShippingResponse shippingResponse;

    @BeforeEach
    void setUp() {
        shippingRequest = new ShippingRequest();
        shippingRequest.setOrderId("order789");
        shippingRequest.setAddress("123 Đường ABC, Hà Nội");
        shippingRequest.setReceiverName("Nguyễn Văn A");
        shippingRequest.setReceiverPhone("0987654321");
        shippingRequest.setShippingMethod("STANDARD");

        shippingResponse = ShippingResponse.builder()
                .shippingId("ship001")
                .orderId("order789")
                .address("123 Đường ABC, Hà Nội")
                .receiverName("Nguyễn Văn A")
                .receiverPhone("0987654321")
                .shippingMethod("STANDARD")
                .trackingNumber("TRK123456")
                .shippedAt(LocalDateTime.now())
                .delivered(false)
                .build();
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void createShipping_success() throws Exception {
        Mockito.when(shippingService.createShipping(Mockito.any())).thenReturn(shippingResponse);

        mockMvc.perform(post("/shippings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shippingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Tạo thông tin vận chuyển thành công"))
                .andExpect(jsonPath("$.result.shippingId").value("ship001"))
                .andExpect(jsonPath("$.result.orderId").value("order789"))
                .andExpect(jsonPath("$.result.delivered").value(false));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void getShippingByOrder_success() throws Exception {
        Mockito.when(shippingService.getShippingByOrderId("order789")).thenReturn(List.of(shippingResponse));

        mockMvc.perform(get("/shippings/order/order789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Danh sách vận chuyển theo đơn hàng"))
                .andExpect(jsonPath("$.result[0].shippingId").value("ship001"))
                .andExpect(jsonPath("$.result[0].orderId").value("order789"));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void getShippingByOrder_notFound() throws Exception {
        Mockito.when(shippingService.getShippingByOrderId("notfound"))
                .thenThrow(new AppException(ErrorCode.SHIPPING_NOT_FOUND));

        mockMvc.perform(get("/shippings/order/notfound"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.SHIPPING_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.SHIPPING_NOT_FOUND.getMessage()));
    }
}