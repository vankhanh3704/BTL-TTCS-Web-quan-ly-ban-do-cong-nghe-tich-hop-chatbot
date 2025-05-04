package com.example.e_commerce.technology.service;


import com.example.e_commerce.technology.model.request.ProductCreationRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.ProductResponse;

public interface IProductService {
    ProductResponse createProduct(ProductCreationRequest request);
}
