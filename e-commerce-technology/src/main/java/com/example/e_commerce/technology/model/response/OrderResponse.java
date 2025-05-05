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
    @JsonProperty("id")
    String id;
    String userId;
    OrderStatus status;
    String shippingAddress;
    PaymentMethod paymentMethod;
    Long totalAmount;
    LocalDateTime createdAt;
    List<OrderItemResponse> items;
}