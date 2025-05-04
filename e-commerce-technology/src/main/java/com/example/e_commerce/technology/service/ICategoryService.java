package com.example.e_commerce.technology.service;


import com.example.e_commerce.technology.model.request.CategoryRequest;
import com.example.e_commerce.technology.model.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    Page<CategoryResponse> findAllCategory(Pageable pageable);
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(String name, CategoryRequest categoryRequest);
    void deleteCategory(String id);

}
