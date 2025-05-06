package com.example.e_commerce.technology.service;

import com.example.e_commerce.technology.Entity.CartEntity;
import com.example.e_commerce.technology.model.request.CartItemRequest;
import com.example.e_commerce.technology.model.response.CartResponse;
import com.example.e_commerce.technology.model.response.OrderResponse;

public interface ICartService {
    CartResponse addToCart(String userId, CartItemRequest request);
    CartResponse getCart(String userId);
    void removeFromCart(String userId, Long productId);
    void clearCart(String userId);
    OrderResponse checkout(String userId, String shippingAddress, String paymentMethod);
}
