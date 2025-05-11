package com.example.e_commerce.technology.mapper;

import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.Entity.UserEntity;
import com.example.e_commerce.technology.model.request.ProductRequest;
import com.example.e_commerce.technology.model.request.UserUpdateRequest;
import com.example.e_commerce.technology.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)// Bỏ qua id vì được tự động sinh
    @Mapping(target = "createdAt", ignore = true) // Bỏ qua createdAt vì được set trong @PrePersist
    @Mapping(target = "category", expression = "java(new CategoryEntity(request.getCategoryId(), null))") // Ánh xạ categoryId
    ProductEntity toProduct(ProductRequest request);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "category", target = "category")
    @Mapping(target = "imageUrls", expression = "java(entity.getImages() != null ? entity.getImages().stream().map(ProductImageEntity::getImageUrl).collect(java.util.stream.Collectors.toList()) : new java.util.ArrayList<>())")
    ProductResponse toProductResponse(ProductEntity entity);

    void updateProduct(@MappingTarget ProductEntity entity, ProductRequest request);
}