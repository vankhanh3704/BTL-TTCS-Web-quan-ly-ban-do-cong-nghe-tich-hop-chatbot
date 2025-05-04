package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.CategoryEntity;
import com.example.e_commerce.technology.mapper.CategoryMapper;
import com.example.e_commerce.technology.model.request.CategoryRequest;
import com.example.e_commerce.technology.model.response.CategoryResponse;
import com.example.e_commerce.technology.repository.CategoryRepository;
import com.example.e_commerce.technology.service.ICategoryService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponse> findAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toCategoryResponse);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryResponse = categoryMapper.toCategory(categoryRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.save(categoryResponse));
    }

    @Override
    public CategoryResponse updateCategory(String name, CategoryRequest categoryRequest) {
        CategoryEntity category = categoryRepository.findById(name)
                .orElseThrow( () -> new RuntimeException("Category not found."));
        categoryMapper.updateCategory(category, categoryRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }


}
