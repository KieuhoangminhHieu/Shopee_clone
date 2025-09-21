package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.VoucherRequest;
import com.devteria.identity_service.dto.response.VoucherResponse;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoucherController.class)
class VoucherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VoucherService voucherService;

    @Autowired
    private ObjectMapper objectMapper;

    private VoucherRequest voucherRequest;
    private VoucherResponse voucherResponse;

    @BeforeEach
    void setUp() {
        voucherRequest = new VoucherRequest();
        voucherRequest.setCode("SALE50");
        voucherRequest.setDescription("Giảm 50k cho đơn từ 300k");
        voucherRequest.setDiscountValue(BigDecimal.valueOf(50000));
        voucherRequest.setMinOrderAmount(BigDecimal.valueOf(300000));
        voucherRequest.setUsageLimit(100);
        voucherRequest.setStartDate(LocalDateTime.now().minusDays(1));
        voucherRequest.setEndDate(LocalDateTime.now().plusDays(7));

        voucherResponse = VoucherResponse.builder()
                .voucherId("voucher123")
                .code("SALE50")
                .description("Giảm 50k cho đơn từ 300k")
                .discountValue(BigDecimal.valueOf(50000))
                .minOrderAmount(BigDecimal.valueOf(300000))
                .usageLimit(100)
                .usedCount(0)
                .startDate(voucherRequest.getStartDate())
                .endDate(voucherRequest.getEndDate())
                .active(true)
                .build();
    }

    @Test
    void createVoucher_success() throws Exception {
        Mockito.when(voucherService.createVoucher(Mockito.any())).thenReturn(voucherResponse);

        mockMvc.perform(post("/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voucherRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.code").value("SALE50"))
                .andExpect(jsonPath("$.result.discountValue").value(50000));
    }

    @Test
    void getVoucher_success() throws Exception {
        Mockito.when(voucherService.getVoucherByCode("SALE50")).thenReturn(voucherResponse);

        mockMvc.perform(get("/vouchers/SALE50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.code").value("SALE50"))
                .andExpect(jsonPath("$.result.voucherId").value("voucher123"));
    }

    @Test
    void getVoucher_notFound() throws Exception {
        Mockito.when(voucherService.getVoucherByCode("INVALID"))
                .thenThrow(new AppException(ErrorCode.INVALID_KEY));

        mockMvc.perform(get("/vouchers/INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_KEY.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_KEY.getMessage()));
    }
}