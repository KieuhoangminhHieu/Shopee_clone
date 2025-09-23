package com.devteria.identity_service.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String userId;
    String voucherCode;
    List<OrderItemRequest> items;
}
