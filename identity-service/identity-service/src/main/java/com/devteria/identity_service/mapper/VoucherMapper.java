package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.VoucherResponse;
import com.devteria.identity_service.entity.Voucher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface VoucherMapper {
    VoucherResponse toResponse(Voucher voucher);
}