package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.Entity.UserEntity;
import com.example.e_commerce.technology.mapper.ProductMapper;
import com.example.e_commerce.technology.model.request.ProductCreationRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.ProductResponse;
import com.example.e_commerce.technology.repository.ProductRepository;
import com.example.e_commerce.technology.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class ProductService implements IProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {
        ProductEntity product = productMapper.toProduct(request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

}
