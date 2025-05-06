package com.example.e_commerce.technology.service;

import com.example.e_commerce.technology.Entity.CartEntity;
import com.example.e_commerce.technology.model.request.CartItemRequest;
import com.example.e_commerce.technology.model.response.CartResponse;

public interface ICartService {
    CartResponse addToCart(String userId, CartItemRequest request);
    CartResponse getCart(Long userId);
    void removeFromCart(Long userId, Long productId);
    void clearCart(Long userId);
}
