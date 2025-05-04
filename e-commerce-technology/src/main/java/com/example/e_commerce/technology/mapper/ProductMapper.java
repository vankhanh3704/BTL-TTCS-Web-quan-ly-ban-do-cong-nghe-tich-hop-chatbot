package com.example.e_commerce.technology.mapper;

import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.model.request.ProductCreationRequest;
import com.example.e_commerce.technology.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)// Bỏ qua id vì được tự động sinh
    @Mapping(target = "createdAt", ignore = true) // Bỏ qua createdAt vì được set trong @PrePersist
    @Mapping(target = "category", expression = "java(new CategoryEntity(request.getCategoryId(), null))") // Ánh xạ categoryId
    ProductEntity toProduct(ProductCreationRequest request);
    @Mapping(source = "category", target = "category")
    ProductResponse toProductResponse(ProductEntity entity);
}