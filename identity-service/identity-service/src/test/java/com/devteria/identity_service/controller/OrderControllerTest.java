package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.OrderItemRequest;
import com.devteria.identity_service.dto.request.OrderRequest;
import com.devteria.identity_service.dto.response.OrderItemResponse;
import com.devteria.identity_service.dto.response.OrderResponse;
import com.devteria.identity_service.enums.OrderStatus;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.service.OrderService;
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
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId("product123");
        itemRequest.setQuantity(2);
        itemRequest.setPrice(BigDecimal.valueOf(150000));

        orderRequest = new OrderRequest();
        orderRequest.setUserId("user123");
        orderRequest.setItems(List.of(itemRequest));

        OrderItemResponse itemResponse = new OrderItemResponse();
        itemResponse.setProductId("product123");
        itemResponse.setProductName("Áo thun");
        itemResponse.setQuantity(2);
        itemResponse.setPrice(BigDecimal.valueOf(150000));
        itemResponse.setSubTotal(BigDecimal.valueOf(300000));

        orderResponse = new OrderResponse();
        orderResponse.setOrderId("order789");
        orderResponse.setUserId("user123");
        orderResponse.setTotalPrice(BigDecimal.valueOf(300000));
        orderResponse.setStatus(OrderStatus.PENDING);
        orderResponse.setCreatedAt(LocalDateTime.now());
        orderResponse.setItems(List.of(itemResponse));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void shouldCreateOrderSuccessfully() throws Exception {
        Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(orderResponse);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.orderId").value("order789"))
                .andExpect(jsonPath("$.result.totalPrice").value(300000))
                .andExpect(jsonPath("$.result.items[0].productId").value("product123"));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void shouldReturnOrderByIdSuccessfully() throws Exception {
        Mockito.when(orderService.getOrderById("order789")).thenReturn(orderResponse);

        mockMvc.perform(get("/orders/order789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.orderId").value("order789"))
                .andExpect(jsonPath("$.result.status").value("PENDING"));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void shouldReturnNotFoundForInvalidOrderId() throws Exception {
        Mockito.when(orderService.getOrderById("notfound"))
                .thenThrow(new AppException(ErrorCode.ORDER_NOT_FOUND));

        mockMvc.perform(get("/orders/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.ORDER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ORDER_NOT_FOUND.getMessage()));
    }

    @Test
    @WithMockUser(username = "user123", roles = "USER")
    void shouldReturnOrdersByUserSuccessfully() throws Exception {
        Mockito.when(orderService.getOrdersByUser("user123")).thenReturn(List.of(orderResponse));

        mockMvc.perform(get("/orders/user/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].orderId").value("order789"))
                .andExpect(jsonPath("$.result[0].userId").value("user123"));
    }
}