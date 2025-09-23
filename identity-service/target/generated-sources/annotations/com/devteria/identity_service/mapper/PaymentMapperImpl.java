package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.PaymentResponse;
import com.devteria.identity_service.entity.Payment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T13:05:04+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toResponse(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentResponse.PaymentResponseBuilder paymentResponse = PaymentResponse.builder();

        paymentResponse.orderId( payment.getOrderId() );
        paymentResponse.paymentMethod( payment.getPaymentMethod() );
        paymentResponse.amount( payment.getAmount() );
        paymentResponse.success( payment.isSuccess() );
        paymentResponse.paidAt( payment.getPaidAt() );

        return paymentResponse.build();
    }
}
