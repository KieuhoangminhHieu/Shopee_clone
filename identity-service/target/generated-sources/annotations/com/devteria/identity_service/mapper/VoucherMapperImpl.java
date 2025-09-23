package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.VoucherResponse;
import com.devteria.identity_service.entity.Voucher;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-22T12:56:02+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class VoucherMapperImpl implements VoucherMapper {

    @Override
    public VoucherResponse toResponse(Voucher voucher) {
        if ( voucher == null ) {
            return null;
        }

        VoucherResponse.VoucherResponseBuilder voucherResponse = VoucherResponse.builder();

        voucherResponse.code( voucher.getCode() );
        voucherResponse.description( voucher.getDescription() );
        voucherResponse.discountValue( voucher.getDiscountValue() );
        voucherResponse.minOrderAmount( voucher.getMinOrderAmount() );
        voucherResponse.usageLimit( voucher.getUsageLimit() );
        voucherResponse.usedCount( voucher.getUsedCount() );
        voucherResponse.startDate( voucher.getStartDate() );
        voucherResponse.endDate( voucher.getEndDate() );
        voucherResponse.active( voucher.isActive() );

        return voucherResponse.build();
    }
}
