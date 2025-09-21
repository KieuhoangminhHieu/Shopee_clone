package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.request.VoucherRequest;
import com.devteria.identity_service.dto.response.VoucherResponse;
import com.devteria.identity_service.entity.Voucher;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.VoucherMapper;
import com.devteria.identity_service.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    public VoucherResponse createVoucher(VoucherRequest request) {
        Voucher voucher = Voucher.builder()
                .code(request.getCode())
                .description(request.getDescription())
                .discountValue(request.getDiscountValue())
                .minOrderAmount(request.getMinOrderAmount())
                .usageLimit(request.getUsageLimit())
                .usedCount(0)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(true)
                .build();

        voucherRepository.save(voucher);
        return voucherMapper.toResponse(voucher);
    }

    public VoucherResponse getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        return voucherMapper.toResponse(voucher);
    }

    public Voucher validateVoucher(String code, BigDecimal orderAmount) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        if (!voucher.isActive()
                || voucher.getUsedCount() >= voucher.getUsageLimit()
                || LocalDateTime.now().isBefore(voucher.getStartDate())
                || LocalDateTime.now().isAfter(voucher.getEndDate())
                || orderAmount.compareTo(voucher.getMinOrderAmount()) < 0) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return voucher;
    }
}