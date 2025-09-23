package com.devteria.identity_service.mapper;

import org.mapstruct.Mapper;

import com.devteria.identity_service.dto.response.VoucherResponse;
import com.devteria.identity_service.entity.Voucher;

@Mapper(componentModel = "Spring")
public interface VoucherMapper {
    VoucherResponse toResponse(Voucher voucher);
}
