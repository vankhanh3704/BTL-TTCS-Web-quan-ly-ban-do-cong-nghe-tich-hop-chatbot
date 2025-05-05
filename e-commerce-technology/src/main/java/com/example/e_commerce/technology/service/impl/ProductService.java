package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.CategoryEntity;
import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.mapper.ProductMapper;
import com.example.e_commerce.technology.model.dto.ProductQueryParams;
import com.example.e_commerce.technology.model.request.ProductRequest;
import com.example.e_commerce.technology.model.request.ProductSearchRequest;
import com.example.e_commerce.technology.model.response.ProductResponse;
import com.example.e_commerce.technology.repository.CategoryRepository;
import com.example.e_commerce.technology.repository.ProductRepository;
import com.example.e_commerce.technology.repository.ProductSpecifications;
import com.example.e_commerce.technology.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<ProductResponse> searchProducts(ProductSearchRequest request, Pageable pageable) {
        log.info("Searching products with search request: {}", request);
        request.validate();
        Page<ProductEntity> productPage = productRepository.findAll(ProductSpecifications.withSearchRequest(request), pageable);
        log.info("Found {} products", productPage.getTotalElements());
        return productPage.map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(ProductQueryParams params) {
        log.info("Searching products with query params: {}", params);

        // Xử lý sort
        String[] sortParts = params.getSort().split(",");
        String sortField = sortParts[0];
        Sort sortObj = Sort.by(sortField);
        if (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")) {
            sortObj = sortObj.descending();
        }

        // Tạo Pageable
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), sortObj);

        // Xây dựng ProductSearchRequest
        ProductSearchRequest request = ProductSearchRequest.builder()
                .name(params.getName())
                .categoryId(params.getCategoryId())
                .priceFrom(params.getPriceFrom())
                .priceTo(params.getPriceTo())
                .build();

        // Validate và truy vấn
        request.validate();
        Page<ProductEntity> productPage = productRepository.findAll(ProductSpecifications.withSearchRequest(request), pageable);
        log.info("Found {} products", productPage.getTotalElements());
        return productPage.map(productMapper::toProductResponse);
    }
    }
