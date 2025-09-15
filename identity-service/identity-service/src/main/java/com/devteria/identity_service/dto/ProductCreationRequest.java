package com.devteria.identity_service.dto;

import lombok.Data;
import java.math.BigDecimal;

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