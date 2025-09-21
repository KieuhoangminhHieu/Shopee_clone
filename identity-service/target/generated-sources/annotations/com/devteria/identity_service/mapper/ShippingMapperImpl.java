package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.response.ShippingResponse;
import com.devteria.identity_service.entity.Shipping;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-21T17:45:47+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ShippingMapperImpl implements ShippingMapper {

    @Override
    public ShippingResponse toResponse(Shipping shipping) {
        if ( shipping == null ) {
            return null;
        }

        ShippingResponse.ShippingResponseBuilder shippingResponse = ShippingResponse.builder();

        shippingResponse.orderId( shipping.getOrderId() );
        shippingResponse.address( shipping.getAddress() );
        shippingResponse.receiverName( shipping.getReceiverName() );
        shippingResponse.receiverPhone( shipping.getReceiverPhone() );
        shippingResponse.shippingMethod( shipping.getShippingMethod() );
        shippingResponse.trackingNumber( shipping.getTrackingNumber() );
        shippingResponse.shippedAt( shipping.getShippedAt() );
        shippingResponse.delivered( shipping.isDelivered() );

        return shippingResponse.build();
    }
}
