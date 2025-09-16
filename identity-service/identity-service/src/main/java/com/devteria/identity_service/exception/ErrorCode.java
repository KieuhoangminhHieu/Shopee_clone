package com.devteria.identity_service.exception;

import lombok.Data;
import lombok.Getter;


@Getter
public enum ErrorCode {
    SHOP_NOT_FOUND(1005, "Shop not found"),
    CATEGORY_NOT_FOUND(1006, "Category not found"),
    USER_NOT_FOUND(1004, "User not found"),
    INVALID_KEY(1000, "Invalid message key"),
    UNCATEGORIZED_EXISTED(999, "Uncategorized error"),
    USER_EXISTED(1001, "User already exists"),
    USERNAME_INVALID(1002, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1003, "Password must be at least 8 characters"),
    USER_NOT_EXISTED(1007, "User not exists"),
    UNAUTHENTICATED(1008, "Unauthenticated error"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

}
