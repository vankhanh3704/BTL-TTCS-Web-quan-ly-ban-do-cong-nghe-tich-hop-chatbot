package com.example.e_commerce.technology.controller;

import com.example.e_commerce.technology.model.dto.ProductQueryParams;
import com.example.e_commerce.technology.model.request.ProductRequest;
import com.example.e_commerce.technology.model.request.ProductSearchRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.ProductResponse;
import com.example.e_commerce.technology.service.IProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    IProductService productService;

    @PostMapping
    ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .result("Product has been deleted")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<ProductResponse>> getAllProducts(@ModelAttribute ProductQueryParams params) {
        log.info("Getting products with params: {}", params);
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.searchProducts(params))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<ProductResponse>> searchProducts(@RequestBody ProductSearchRequest request, Pageable pageable) {
        log.info("Received search request: {}", request);
        Page<ProductResponse> result = productService.searchProducts(request, pageable);
        return ApiResponse.<Page<ProductResponse>>builder().result(result).build();
    }
}
