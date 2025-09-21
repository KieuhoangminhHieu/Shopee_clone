package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.CartItemRequest;
import com.devteria.identity_service.dto.response.CartItemResponse;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.service.CartItemService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartItemService cartItemService;

    private CartItemRequest cartItemRequest;
    private CartItemResponse cartItemResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId("product123");
        cartItemRequest.setQuantity(2);

        cartItemResponse = new CartItemResponse();
        cartItemResponse.setProductId("product123");
        cartItemResponse.setQuantity(2);
        cartItemResponse.setPrice(new BigDecimal("150000"));
        cartItemResponse.setSubTotal(new BigDecimal("300000"));

        objectMapper = new ObjectMapper();
    }

    // ---------- ADD/UPDATE BY USER ----------
    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void addOrUpdateByUser_success() throws Exception {
        Mockito.when(cartItemService.addOrUpdateCartItemByUser(eq("user123"), any()))
                .thenReturn(cartItemResponse);

        mockMvc.perform(post("/carts/user123/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.productId").value("product123"))
                .andExpect(jsonPath("$.result.quantity").value(2))
                .andExpect(jsonPath("$.result.subTotal").value(300000));
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void addOrUpdateByUser_productNotFound_fail() throws Exception {
        Mockito.when(cartItemService.addOrUpdateCartItemByUser(eq("user123"), any()))
                .thenThrow(new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        mockMvc.perform(post("/carts/user123/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.PRODUCT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }

    // ---------- ADD/UPDATE BY CART ID ----------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addOrUpdateByCartId_success() throws Exception {
        Mockito.when(cartItemService.addOrUpdateCartItemByCartId(eq("cart789"), any()))
                .thenReturn(cartItemResponse);

        mockMvc.perform(post("/cart-items")
                        .param("cartId", "cart789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.productId").value("product123"))
                .andExpect(jsonPath("$.result.subTotal").value(300000));
    }

    // ---------- REMOVE BY USER ----------
    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void removeByUser_success() throws Exception {
        mockMvc.perform(delete("/carts/user123/items/product123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void removeByUser_itemNotFound_fail() throws Exception {
        Mockito.doThrow(new AppException(ErrorCode.CART_ITEM_NOT_FOUND))
                .when(cartItemService).removeCartItemByUser("user123", "product404");

        mockMvc.perform(delete("/carts/user123/items/product404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CART_ITEM_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.CART_ITEM_NOT_FOUND.getMessage()));
    }

    // ---------- REMOVE BY CART ID ----------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removeByCartId_success() throws Exception {
        mockMvc.perform(delete("/cart-items")
                        .param("cartId", "cart789")
                        .param("productId", "product123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removeByCartId_itemNotFound_fail() throws Exception {
        Mockito.doThrow(new AppException(ErrorCode.CART_ITEM_NOT_FOUND))
                .when(cartItemService).removeCartItemByCartId("cart789", "product404");

        mockMvc.perform(delete("/cart-items")
                        .param("cartId", "cart789")
                        .param("productId", "product404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CART_ITEM_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.CART_ITEM_NOT_FOUND.getMessage()));
    }
}