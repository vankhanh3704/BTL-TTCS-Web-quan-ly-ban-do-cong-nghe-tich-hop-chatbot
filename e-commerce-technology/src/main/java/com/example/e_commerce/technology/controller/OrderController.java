package com.example.e_commerce.technology.controller;

import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.model.request.OrderRequest;
import com.example.e_commerce.technology.model.request.UpdateOrderStatusRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.OrderResponse;
import com.example.e_commerce.technology.service.IOrderService;
import com.example.e_commerce.technology.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    IOrderService orderService;


    @PostMapping
    public ApiResponse<?> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(SecurityUtils.getCurrentUsername(), request);
        return ApiResponse.builder()
                .result(response)
                .build();
    }

    @GetMapping("/info")
    ApiResponse<?> getUserOrders(@PageableDefault(page = 0, size = 10) Pageable pageable){
        return ApiResponse.builder()
                .result(orderService.getUserOrders(SecurityUtils.getCurrentUsername(), pageable))
                .build();
    }

    @GetMapping
    ApiResponse<?> getOrders(@PageableDefault(page = 0, size = 10) Pageable pageable){
        return ApiResponse.builder()
                .result(orderService.getOrders(pageable))
                .build();
    }

    @PatchMapping("/status/{id}")
    public ApiResponse<?> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(id, request.getStatus());
        return ApiResponse.builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id, SecurityUtils.getCurrentUsername());
        return ApiResponse.builder()
                .message("Đơn hàng đã được hủy")
                .build();
    }
}
