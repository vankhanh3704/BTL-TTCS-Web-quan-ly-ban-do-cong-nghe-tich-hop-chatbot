package com.example.e_commerce.technology.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductRequest {
    String name;
    Long price;
    String description;
    Long stock;
    String categoryId;
}
