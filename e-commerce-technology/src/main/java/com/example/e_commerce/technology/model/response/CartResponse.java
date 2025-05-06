package com.example.e_commerce.technology.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CartResponse {
     Long id;
     List<CartItemResponse> items;
     Long totalAmount; // Tổng giá trị giỏ hàng

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    public static class CartItemResponse {
         Long id;
         Long productId;
         String productName;
         Long productPrice;
         Integer quantity;
         Long subTotal; // Giá trị của sản phẩm này (quantity * price)
    }
}
