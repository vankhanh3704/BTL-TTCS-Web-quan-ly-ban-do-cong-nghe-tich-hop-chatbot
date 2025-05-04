package com.example.e_commerce.technology.mapper;

import com.example.e_commerce.technology.Entity.CategoryEntity;
import com.example.e_commerce.technology.Entity.UserEntity;
import com.example.e_commerce.technology.model.request.CategoryRequest;
import com.example.e_commerce.technology.model.request.UserUpdateRequest;
import com.example.e_commerce.technology.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryEntity toCategory(CategoryRequest request);
    CategoryResponse toCategoryResponse(CategoryEntity entity);
    void updateCategory(@MappingTarget CategoryEntity entity, CategoryRequest request);
}
