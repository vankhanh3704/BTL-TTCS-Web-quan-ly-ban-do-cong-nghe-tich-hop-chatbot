package com.example.e_commerce.technology.controller;

import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.model.request.OrderRequest;
import com.example.e_commerce.technology.model.request.UpdateOrderStatusRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.OrderResponse;
import com.example.e_commerce.technology.service.IOrderService;
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

    @GetMapping("/info")
    ApiResponse<?> getUserOrders(@PageableDefault(page = 0, size = 10) Pageable pageable){
        String userId = getCurrentUserId();
        log.info("Lấy danh sách đơn hàng của người dùng: {}, pageable: {}", userId, pageable);
        return ApiResponse.builder()
                .result(orderService.getUserOrders(userId, pageable))
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

        log.info("Cập nhật trạng thái đơn hàng ID: {} thành {}", id, request.getStatus());
        OrderResponse response = orderService.updateOrderStatus(id, request.getStatus());
        return ApiResponse.builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> cancelOrder(@PathVariable Long id) {
        String userId = getCurrentUserId();
        log.info("Hủy đơn hàng ID: {} của người dùng: {}", id, userId);
        orderService.cancelOrder(id, userId);
        return ApiResponse.builder()
                .message("Đơn hàng đã được hủy")
                .build();
    }
}
