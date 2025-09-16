package com.devteria.identity_service.dto.request;

import lombok.Data;

@Data
public class ProductCreationRequest {
    private String productName;
    private String productDescription;
    private Double price;
    private Integer stock;
    private String image;

    private String shopId;
    private String categoryId;
}