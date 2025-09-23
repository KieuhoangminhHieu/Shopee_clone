package com.devteria.identity_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devteria.identity_service.dto.request.VoucherRequest;
import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.response.VoucherResponse;
import com.devteria.identity_service.service.VoucherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(@RequestBody VoucherRequest request) {
        VoucherResponse response = voucherService.createVoucher(request);
        return ResponseEntity.ok(ApiResponse.<VoucherResponse>builder()
                .result(response)
                .message("Tạo voucher thành công")
                .build());
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<VoucherResponse>> getVoucher(@PathVariable String code) {
        VoucherResponse response = voucherService.getVoucherByCode(code);
        return ResponseEntity.ok(ApiResponse.<VoucherResponse>builder()
                .result(response)
                .message("Thông tin voucher")
                .build());
    }
}
