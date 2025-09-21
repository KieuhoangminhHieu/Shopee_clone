package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.response.CartItemResponse;
import com.devteria.identity_service.dto.response.CartResponse;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    private CartResponse cartResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        CartItemResponse item1 = new CartItemResponse();
        item1.setProductId("product123");
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("150000"));
        item1.setSubTotal(new BigDecimal("300000"));

        CartItemResponse item2 = new CartItemResponse();
        item2.setProductId("product456");
        item2.setQuantity(1);
        item2.setPrice(new BigDecimal("200000"));
        item2.setSubTotal(new BigDecimal("200000"));

        cartResponse = new CartResponse();
        cartResponse.setCartId("cart789");
        cartResponse.setUserId("user123");
        cartResponse.setTotalPrice(new BigDecimal("500000"));
        cartResponse.setItems(List.of(item1, item2));

        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void getCart_success() throws Exception {
        Mockito.when(cartService.getCartByUser("user123")).thenReturn(cartResponse);

        mockMvc.perform(get("/carts/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.cartId").value("cart789"))
                .andExpect(jsonPath("$.result.userId").value("user123"))
                .andExpect(jsonPath("$.result.totalPrice").value(500000))
                .andExpect(jsonPath("$.result.items[0].productId").value("product123"))
                .andExpect(jsonPath("$.result.items[1].productId").value("product456"));
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void getCart_notFound_fail() throws Exception {
        Mockito.when(cartService.getCartByUser("notfound"))
                .thenThrow(new AppException(ErrorCode.CART_NOT_FOUND));

        mockMvc.perform(get("/carts/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CART_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.CART_NOT_FOUND.getMessage()));
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void clearCart_success() throws Exception {
        mockMvc.perform(delete("/carts/user123/clear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void clearCart_notFound_fail() throws Exception {
        Mockito.doThrow(new AppException(ErrorCode.CART_NOT_FOUND))
                .when(cartService).clearCartByUserId("notfound");

        mockMvc.perform(delete("/carts/notfound/clear"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CART_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.CART_NOT_FOUND.getMessage()));
    }
}