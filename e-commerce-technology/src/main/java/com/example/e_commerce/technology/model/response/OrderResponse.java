package com.example.e_commerce.technology.model.response;

import com.example.e_commerce.technology.Enum.OrderStatus;
import com.example.e_commerce.technology.Enum.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private String shippingAddress;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private Long totalAmount;
    private List<OrderItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponse {
        Long id;
        Long productId;
        String productName;
        Long unitPrice;
        Integer quantity;
        Long subTotal;
    }
}