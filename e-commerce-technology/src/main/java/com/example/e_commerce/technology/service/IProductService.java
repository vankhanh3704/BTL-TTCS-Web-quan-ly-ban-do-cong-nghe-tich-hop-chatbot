package com.example.e_commerce.technology.service;


import com.example.e_commerce.technology.model.dto.ProductQueryParams;
import com.example.e_commerce.technology.model.request.ProductRequest;
import com.example.e_commerce.technology.model.request.ProductSearchRequest;
import com.example.e_commerce.technology.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> searchProducts(ProductSearchRequest request, Pageable pageable);
    Page<ProductResponse> searchProducts(ProductQueryParams params);
    void deleteProductImage(Long productId, Long imageId);
    Page<ProductResponse> getProductsByCategory(String category, Pageable pageable);
}
