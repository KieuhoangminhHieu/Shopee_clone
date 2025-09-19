package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.entity.Shop;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T13:47:47+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ShopMapperImpl implements ShopMapper {

    @Override
    public Shop toEntity(ShopCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setShopName( request.getShopName() );
        shop.setDescription( request.getDescription() );

        return shop;
    }

    @Override
    public ShopResponse toResponse(Shop shop) {
        if ( shop == null ) {
            return null;
        }

        ShopResponse.ShopResponseBuilder shopResponse = ShopResponse.builder();

        shopResponse.id( shop.getId() );
        shopResponse.shopName( shop.getShopName() );
        shopResponse.description( shop.getDescription() );
        shopResponse.userId( shop.getUserId() );

        return shopResponse.build();
    }

    @Override
    public void updateEntityFromDto(ShopUpdateRequest request, Shop shop) {
        if ( request == null ) {
            return;
        }

        shop.setShopName( request.getShopName() );
        shop.setDescription( request.getDescription() );
    }
}
