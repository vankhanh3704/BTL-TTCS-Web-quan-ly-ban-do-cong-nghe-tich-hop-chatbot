package com.example.e_commerce.technology.service;


import com.example.e_commerce.technology.model.request.CategoryRequest;
import com.example.e_commerce.technology.model.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    Page<CategoryResponse> findAllCategory(Pageable pageable);
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);
    void deleteCategory(Long id);

}
