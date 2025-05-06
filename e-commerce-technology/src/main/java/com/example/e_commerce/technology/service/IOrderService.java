package com.example.e_commerce.technology.service;

import com.example.e_commerce.technology.model.request.OrderRequest;
import com.example.e_commerce.technology.model.response.OrderResponse;

public interface IOrderService {
    OrderResponse createOrder(String userId, OrderRequest request);

}
