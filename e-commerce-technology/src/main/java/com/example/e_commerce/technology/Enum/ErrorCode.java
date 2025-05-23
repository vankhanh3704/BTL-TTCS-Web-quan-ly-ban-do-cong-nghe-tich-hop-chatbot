package com.example.e_commerce.technology.Enum;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // Lỗi không xác định
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key.", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User already existed.", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters.", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not exists.", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    CATEGORY_NOT_FOUND(1008, "Category not found.", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1009, "Product already existed.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1010, "Product does not exist.", HttpStatus.NOT_FOUND),
    IMAGE_UPLOAD_FAILED(1011, "Image upload failed.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_NOT_FOUND(1012, "Image not found.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK(1013, "Insufficient stock.", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(1014, "Cart not found.", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1015, "Cart item not found.", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(1016, "Order not found.", HttpStatus.NOT_FOUND),
    INVALID_ORDER_STATUS(1017, "Orders can only be canceled in PENDING status.", HttpStatus.BAD_REQUEST),
    ;



    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
