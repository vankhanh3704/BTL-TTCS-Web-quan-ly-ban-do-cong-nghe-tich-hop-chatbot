package com.example.e_commerce.technology.controller;


import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.model.request.CartItemRequest;
import com.example.e_commerce.technology.model.request.CheckoutRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.CartResponse;
import com.example.e_commerce.technology.model.response.OrderResponse;
import com.example.e_commerce.technology.service.ICartService;
import com.example.e_commerce.technology.utils.SecurityUtils;
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

    @PostMapping
    public ApiResponse<CartResponse> addToCart(@RequestBody CartItemRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addToCart(SecurityUtils.getCurrentUsername(), request))
                .build();
    }


    @GetMapping
    ApiResponse<CartResponse> getCart() {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCart(SecurityUtils.getCurrentUsername()))
                .build();
    }

    @DeleteMapping("/items/{productId}")
    ApiResponse<String> removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(SecurityUtils.getCurrentUsername(), productId);
        return ApiResponse.<String>builder()
                .result("Item removed from cart.")
                .build();
    }

    @DeleteMapping
    ApiResponse<String> clearCart() {
        cartService.clearCart(SecurityUtils.getCurrentUsername());
        return ApiResponse.<String>builder()
                .result("Cart cleared")
                .build();
    }

    @PostMapping("/checkout")
    public ApiResponse<?> checkout(
            @RequestBody CheckoutRequest request) {
        OrderResponse response = cartService.checkout(SecurityUtils.getCurrentUsername(), request.getShippingAddress(), request.getPaymentMethod());
        return ApiResponse.builder()
                .result(response)
                .build();
    }
}
