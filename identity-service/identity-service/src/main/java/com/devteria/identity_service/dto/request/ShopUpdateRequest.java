
package com.devteria.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShopUpdateRequest {
    @NotBlank
    private String shopName;
    private String description;
}
