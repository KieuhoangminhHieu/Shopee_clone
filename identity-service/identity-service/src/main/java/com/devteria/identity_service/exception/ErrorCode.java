package com.devteria.identity_service.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {

    CART_NOT_FOUND(1100, "Cart not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1101, "Cart item not found", HttpStatus.NOT_FOUND),
    CART_ITEM_QUANTITY_INVALID(1102, "Cart item quantity must be greater than 0", HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_FOUND(1200, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1201, "Product already exists", HttpStatus.CONFLICT),
    PRODUCT_PRICE_INVALID(1202, "Product price must be greater than 0", HttpStatus.BAD_REQUEST),

    SHOP_NOT_FOUND(1005, "Shop not found", HttpStatus.NOT_FOUND),
    SHOP_EXISTED(1006, "Shop name is not available", HttpStatus.CONFLICT),

    CATEGORY_NOT_FOUND(1007, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(1008, "Category name is not available", HttpStatus.CONFLICT),

    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(1001, "User already exists", HttpStatus.CONFLICT),
    USERNAME_INVALID(1002, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1011, "Your age must be at least 18", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1012, "INVALID_EMAIL", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER(1013, "INVALID_PHONE_NUMBER", HttpStatus.BAD_REQUEST),

    UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1008, "Unauthenticated error", HttpStatus.UNAUTHORIZED),



    INVALID_KEY(1000, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXISTED(999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);



    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}