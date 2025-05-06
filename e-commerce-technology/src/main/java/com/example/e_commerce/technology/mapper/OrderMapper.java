package com.example.e_commerce.technology.mapper;

import com.example.e_commerce.technology.Entity.OrderEntity;
import com.example.e_commerce.technology.Entity.OrderItemEntity;
import com.example.e_commerce.technology.model.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    OrderResponse toOrderResponse(OrderEntity order);

    @Mapping(target = "subTotal", expression = "java(orderItem.getQuantity() * orderItem.getUnitPrice())")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.id", target = "productId")
    OrderResponse.OrderItemResponse toOrderItemResponse(OrderItemEntity orderItem);

    List<OrderResponse.OrderItemResponse> toOrderItemResponses(List<OrderItemEntity> items);

    @Named("statusToString")
    default String mapStatusToString(Enum<?> status) {
        return status.name();
    }
}
