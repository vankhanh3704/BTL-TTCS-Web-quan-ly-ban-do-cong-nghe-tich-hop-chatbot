package com.example.e_commerce.technology.controller;

import com.example.e_commerce.technology.model.request.ProductRequest;
import com.example.e_commerce.technology.model.response.ApiResponse;
import com.example.e_commerce.technology.model.response.ProductResponse;
import com.example.e_commerce.technology.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    IProductService productService;

    @PostMapping
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request){
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
    ApiResponse<ProductResponse> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ApiResponse.<ProductResponse>builder().build();
    }

    @GetMapping
    ApiResponse<Page<ProductResponse>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ){
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.getAllProducts(PageRequest.of(page, size)))
                .build();
    }
}
