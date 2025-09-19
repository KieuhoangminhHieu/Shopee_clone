package com.devteria.identity_service.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Double price;
    private String shopName;
    private String categoryName;
}
