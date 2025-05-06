package com.example.e_commerce.technology.controller;

import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.model.request.OrderRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.OrderResponse;
import com.example.e_commerce.technology.service.IOrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    IOrderService orderService;
    private String getCurrentUserId() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return authentication.getName();
    }

    @PostMapping
    public ApiResponse<?> createOrder(@Valid @RequestBody OrderRequest request) {
        String userId = getCurrentUserId();
        OrderResponse response = orderService.createOrder(userId, request);
        return ApiResponse.builder()
                .result(response)
                .build();
    }

}
