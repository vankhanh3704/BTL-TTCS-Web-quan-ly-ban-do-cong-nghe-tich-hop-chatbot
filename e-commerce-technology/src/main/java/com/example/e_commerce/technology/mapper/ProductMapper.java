package com.example.e_commerce.technology.mapper;

import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.model.request.ProductCreationRequest;
import com.example.e_commerce.technology.model.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity toProduct(ProductCreationRequest request);
    ProductResponse toProductResponse(ProductEntity entity);
}
