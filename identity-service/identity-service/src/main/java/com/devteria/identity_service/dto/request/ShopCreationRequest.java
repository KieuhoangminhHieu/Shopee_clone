package com.devteria.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopCreationRequest {
    @NotBlank(message = "SHOP_NAME_REQUIRED")
    private String shopName;

    @NotBlank(message = "SHOP_DESCRIPTION_REQUIRED")
    private String description;

    @NotBlank(message = "SHOP_ADDRESS_REQUIRED")
    private String address;
}

