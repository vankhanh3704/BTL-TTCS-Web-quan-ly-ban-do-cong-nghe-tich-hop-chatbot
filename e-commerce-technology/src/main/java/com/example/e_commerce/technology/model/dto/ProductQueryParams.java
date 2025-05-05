package com.example.e_commerce.technology.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductQueryParams {
    @Builder.Default
    int page = 0;
    @Builder.Default
    int size = 10;
    @Builder.Default
    String sort = "id";
    String name;
    String categoryId;
    Long priceFrom;
    Long priceTo;
}