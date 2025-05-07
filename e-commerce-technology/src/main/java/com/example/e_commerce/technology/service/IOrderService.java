package com.example.e_commerce.technology.service;

import com.example.e_commerce.technology.Enum.OrderStatus;
import com.example.e_commerce.technology.model.request.OrderRequest;
import com.example.e_commerce.technology.model.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    OrderResponse createOrder(String userId, OrderRequest request);
    Page<OrderResponse> getUserOrders(String userId, Pageable pageable);

    Page<OrderResponse> getOrders(Pageable pageable);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);
    void cancelOrder(Long orderId, String userId);
}
