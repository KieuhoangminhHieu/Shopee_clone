package com.devteria.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // CART
    CART_NOT_FOUND(1100, "Không tìm thấy giỏ hàng", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1101, "Không tìm thấy sản phẩm trong giỏ hàng", HttpStatus.NOT_FOUND),
    CART_ITEM_QUANTITY_INVALID(1102, "Số lượng sản phẩm phải lớn hơn 0", HttpStatus.BAD_REQUEST),

    // PRODUCT
    PRODUCT_NOT_FOUND(1200, "Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1201, "Sản phẩm đã tồn tại", HttpStatus.CONFLICT),
    PRODUCT_PRICE_INVALID(1202, "Giá sản phẩm phải lớn hơn 0", HttpStatus.BAD_REQUEST),

    // SHOP
    SHOP_NOT_FOUND(1005, "Không tìm thấy cửa hàng", HttpStatus.NOT_FOUND),
    SHOP_EXISTED(1006, "Tên cửa hàng đã được sử dụng", HttpStatus.CONFLICT),
    SHOP_NAME_REQUIRED(1009, "Tên cửa hàng là bắt buộc", HttpStatus.BAD_REQUEST),
    SHOP_DESCRIPTION_REQUIRED(1014, "Mô tả cửa hàng là bắt buộc", HttpStatus.BAD_REQUEST),
    SHOP_ADDRESS_REQUIRED(1015, "Địa chỉ cửa hàng là bắt buộc", HttpStatus.BAD_REQUEST),

    // CATEGORY
    CATEGORY_NOT_FOUND(1007, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(1008, "Tên danh mục đã được sử dụng", HttpStatus.CONFLICT),

    // USER
    USER_NOT_FOUND(1004, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    USER_EXISTED(1001, "Người dùng đã tồn tại", HttpStatus.CONFLICT),
    USERNAME_INVALID(1002, "Tên người dùng phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1011, "Tuổi phải từ 18 trở lên", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1012, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER(1013, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),

    // AUTH
    UNAUTHORIZED(1010, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1019, "Bạn chưa đăng nhập", HttpStatus.UNAUTHORIZED),

    // ORDER / PAYMENT / SHIPPING
    ORDER_NOT_FOUND(1016, "Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_FOUND(1017, "Không tìm thấy thông tin thanh toán", HttpStatus.NOT_FOUND),
    SHIPPING_NOT_FOUND(1018, "Không tìm thấy thông tin vận chuyển", HttpStatus.NOT_FOUND),

    // SYSTEM
    INVALID_KEY(1000, "Khóa không hợp lệ", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXISTED(999, "Lỗi hệ thống chưa xác định", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}