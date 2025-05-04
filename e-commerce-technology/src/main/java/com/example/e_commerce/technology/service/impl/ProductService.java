package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.CategoryEntity;
import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.mapper.ProductMapper;
import com.example.e_commerce.technology.model.request.ProductRequest;
import com.example.e_commerce.technology.model.response.ProductResponse;
import com.example.e_commerce.technology.repository.CategoryRepository;
import com.example.e_commerce.technology.repository.ProductRepository;
import com.example.e_commerce.technology.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class ProductService implements IProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        if(productRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        ProductEntity productEntity = productMapper.toProduct(request);
        productEntity.setCategory(category); // Đặt danh mục đã kiểm tra
        return productMapper.toProductResponse(productRepository.save(productEntity));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productMapper.updateProduct(product, request);
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return  productRepository.findAll(pageable).map(productMapper::toProductResponse);
    }

}
