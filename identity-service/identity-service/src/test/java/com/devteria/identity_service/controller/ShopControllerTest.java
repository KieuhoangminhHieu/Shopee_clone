package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.service.ShopService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShopService shopService;

    private ShopCreationRequest shopCreationRequest;
    private ShopUpdateRequest shopUpdateRequest;
    private ShopResponse shopResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void initData() {
        shopCreationRequest = new ShopCreationRequest();
        shopCreationRequest.setShopName("Cửa hàng của Hiếu");
        shopCreationRequest.setDescription("Phụ kiện điện thoại");
        shopCreationRequest.setAddress("Hà Nội");

        shopUpdateRequest = new ShopUpdateRequest();
        shopUpdateRequest.setShopName("Cửa hàng Hiếu TPHCM");
        shopUpdateRequest.setDescription("Phụ kiện cao cấp");
        shopUpdateRequest.setAddress("Thành Phố Hồ Chí Minh");

        shopResponse = new ShopResponse();
        shopResponse.setId("123456789");
        shopResponse.setShopName("Cửa hàng của Hiếu");
        shopResponse.setDescription("Phụ kiện điện thoại");
        shopResponse.setAddress("Hà Nội");

        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createShop_success() throws Exception {
        Mockito.when(shopService.createShop(any())).thenReturn(shopResponse);

        mockMvc.perform(post("/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopCreationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.shopName").value("Cửa hàng của Hiếu"))
                .andExpect(jsonPath("$.result.address").value("Hà Nội"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getShops_success() throws Exception {
        Mockito.when(shopService.getShops()).thenReturn(List.of(shopResponse));

        mockMvc.perform(get("/shops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].id").value("123456789"))
                .andExpect(jsonPath("$.result[0].shopName").value("Cửa hàng của Hiếu"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getShopById_success() throws Exception {
        Mockito.when(shopService.getShopById("123456789")).thenReturn(shopResponse);

        mockMvc.perform(get("/shops/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value("123456789"))
                .andExpect(jsonPath("$.result.shopName").value("Cửa hàng của Hiếu"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateShop_success() throws Exception {
        ShopResponse updated = new ShopResponse();
        updated.setId("123456789");
        updated.setShopName("Hiếu Store TPHCM");
        updated.setDescription("Cơ sở đầu tiên");
        updated.setAddress("Thành Phố Hồ Chí Minh");

        Mockito.when(shopService.updateShop(eq("123456789"), any())).thenReturn(updated);

        mockMvc.perform(put("/shops/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.shopName").value("Hiếu Store TPHCM"))
                .andExpect(jsonPath("$.result.address").value("Thành Phố Hồ Chí Minh"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteShop_success() throws Exception {
        mockMvc.perform(delete("/shops/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    // ---------- ERROR CASES ----------

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getShopById_notFound_fail() throws Exception {
        Mockito.when(shopService.getShopById("notfound"))
                .thenThrow(new AppException(ErrorCode.INVALID_KEY));

        mockMvc.perform(get("/shops/notfound"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_KEY.getCode()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateShop_notFound_fail() throws Exception {
        Mockito.when(shopService.updateShop(eq("notfound"), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_KEY));

        mockMvc.perform(put("/shops/notfound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_KEY.getCode()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteShop_notFound_fail() throws Exception {
        Mockito.doThrow(new AppException(ErrorCode.INVALID_KEY))
                .when(shopService).deleteShop("notfound");

        mockMvc.perform(delete("/shops/notfound"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_KEY.getCode()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getShopById_unauthorized_fail() throws Exception {
        Mockito.when(shopService.getShopById("123456789"))
                .thenThrow(new AppException(ErrorCode.UNAUTHORIZED));

        mockMvc.perform(get("/shops/123456789"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createShop_missingField_fail() throws Exception {
        ShopCreationRequest invalidRequest = new ShopCreationRequest();
        invalidRequest.setDescription("Thiếu tên");

        mockMvc.perform(post("/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}