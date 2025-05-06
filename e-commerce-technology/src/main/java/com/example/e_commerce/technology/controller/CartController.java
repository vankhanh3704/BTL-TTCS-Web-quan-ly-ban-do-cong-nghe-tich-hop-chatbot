package com.example.e_commerce.technology.controller;


import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.model.request.CartItemRequest;
import com.example.e_commerce.technology.model.request.CheckoutRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.CartResponse;
import com.example.e_commerce.technology.model.response.OrderResponse;
import com.example.e_commerce.technology.service.ICartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    ICartService cartService;

    private String getCurrentUserId() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return authentication.getName();
    }

    @PostMapping
    public ApiResponse<CartResponse> addToCart(@RequestBody CartItemRequest request) {
        String userId = getCurrentUserId();
        log.info("Thêm sản phẩm {} vào giỏ hàng cho người dùng {}", request.getProductId(), userId);
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addToCart(userId, request))
                .build();
    }


    @GetMapping
    ApiResponse<CartResponse> getCart() {
        String userId = getCurrentUserId();
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCart(userId))
                .build();
    }

    @DeleteMapping("/items/{productId}")
    ApiResponse<String> removeFromCart(@PathVariable Long productId) {
        String userId = getCurrentUserId();
        cartService.removeFromCart(userId, productId);
        return ApiResponse.<String>builder()
                .result("Item removed from cart.")
                .build();
    }

    @DeleteMapping
    ApiResponse<String> clearCart() {
        String userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ApiResponse.<String>builder()
                .result("Cart cleared")
                .build();
    }

    @PostMapping("/checkout")
    public ApiResponse<?> checkout(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CheckoutRequest request) {
        String userId = getCurrentUserId();
        OrderResponse response = cartService.checkout(userId, request.getShippingAddress(), request.getPaymentMethod());
        return ApiResponse.builder()
                .result(response)
                .build();
    }
}
