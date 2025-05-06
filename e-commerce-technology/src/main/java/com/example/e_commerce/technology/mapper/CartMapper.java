package com.example.e_commerce.technology.mapper;

import com.example.e_commerce.technology.Entity.CartItemEntity;
import com.example.e_commerce.technology.model.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "subTotal", expression = "java(entity.getQuantity() * entity.getProduct().getPrice())")

    CartResponse.CartItemResponse toCartItemResponse(CartItemEntity entity);
}