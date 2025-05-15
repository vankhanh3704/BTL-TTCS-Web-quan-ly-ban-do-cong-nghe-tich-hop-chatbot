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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    IProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(
            @Valid @RequestPart("product") ProductRequest productRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        productRequest.setImages(images); // Gán images vào ProductRequest
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productRequest))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Long id, // MongoDB dùng String cho ID
            @Valid @RequestPart("product") ProductRequest productRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        productRequest.setImages(images);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, productRequest))
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
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.searchProducts(params))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<ProductResponse>> searchProducts(@RequestBody ProductSearchRequest request, Pageable pageable) {
        Page<ProductResponse> result = productService.searchProducts(request, pageable);
        return ApiResponse.<Page<ProductResponse>>builder().result(result).build();
    }


    @DeleteMapping("/{productId}/images/{imageId}")
    public ApiResponse<String> deleteProductImage(@PathVariable Long productId, @PathVariable Long imageId) {
        productService.deleteProductImage(productId, imageId);
        return ApiResponse.<String>builder()
                .result("Image deleted successfully")
                .build();
    }

    @GetMapping("/by-category")
    public ApiResponse<Page<ProductResponse>> getProductsByCategory(
            @RequestParam String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<ProductResponse> result = productService.getProductsByCategory(categoryName, PageRequest.of(page, size));
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(result)
                .build();
    }
}
