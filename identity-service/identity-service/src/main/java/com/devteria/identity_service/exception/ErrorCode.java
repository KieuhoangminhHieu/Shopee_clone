package com.devteria.identity_service.exception;

public enum ErrorCode {
    SHOP_NOT_FOUND(1004, "Shop not found"),
    CATEGORY_NOT_FOUND(1004, "Category not found"),
    USER_NOT_FOUND(1004, "User not found"),
    INVALID_KEY(1000, "Invalid message key"),
    UNCATEGORIZED_EXISTED(999, "Uncategorized error"),
    USER_EXISTED(1001, "User already exists"),
    USERNAME_INVALID(1002, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1003, "Password must be at least 8 characters"),;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
