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
    @JsonProperty("id")
    String id;
    String name;
    Long price;
    String description;
    Long stock;
    CategoryResponse category;
    List<String> imageUrls;
}
