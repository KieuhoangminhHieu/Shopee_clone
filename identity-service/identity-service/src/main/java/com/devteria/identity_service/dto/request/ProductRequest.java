package com.devteria.identity_service.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private String shopId;
    private String categoryId;
}
