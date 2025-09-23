package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.request.ProductRequest;
import com.devteria.identity_service.dto.response.ProductResponse;
import com.devteria.identity_service.entity.Category;
import com.devteria.identity_service.entity.Product;
import com.devteria.identity_service.entity.Shop;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-22T12:56:02+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( request.getName() );
        product.description( request.getDescription() );
        if ( request.getPrice() != null ) {
            product.price( BigDecimal.valueOf( request.getPrice() ) );
        }

        return product.build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setShopName( productShopShopName( product ) );
        productResponse.setCategoryName( productCategoryName( product ) );
        productResponse.setId( product.getId() );
        productResponse.setName( product.getName() );
        productResponse.setDescription( product.getDescription() );
        if ( product.getPrice() != null ) {
            productResponse.setPrice( product.getPrice().doubleValue() );
        }

        return productResponse;
    }

    @Override
    public void updateEntityFromDto(ProductRequest request, Product product) {
        if ( request == null ) {
            return;
        }

        product.setName( request.getName() );
        product.setDescription( request.getDescription() );
        if ( request.getPrice() != null ) {
            product.setPrice( BigDecimal.valueOf( request.getPrice() ) );
        }
        else {
            product.setPrice( null );
        }
    }

    private String productShopShopName(Product product) {
        if ( product == null ) {
            return null;
        }
        Shop shop = product.getShop();
        if ( shop == null ) {
            return null;
        }
        String shopName = shop.getShopName();
        if ( shopName == null ) {
            return null;
        }
        return shopName;
    }

    private String productCategoryName(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
