package com.example.e_commerce.technology.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponse {
    String id;
    String name;
    Long price;
    String description;
    Long stock;
    String color;

    CategoryResponse category;
    List<String> imageUrls;
}
