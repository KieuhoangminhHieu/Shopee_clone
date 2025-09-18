package com.devteria.identity_service.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {

    SHOP_NOT_FOUND(1005, "Shop not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(1006, "Category not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),

    INVALID_KEY(1000, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXISTED(999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_EXISTED(1001, "User already exists", HttpStatus.CONFLICT),
    USERNAME_INVALID(1002, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),

    USER_NOT_EXISTED(1007, "User does not exist", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1011, "Invalid date of bỉrth", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1008, "Unauthenticated error", HttpStatus.UNAUTHORIZED);



    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}