package com.example.e_commerce.technology.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductRequest {
    @NotNull
    @Size(min = 1, max = 100)
    String name;
    @NotNull
    @Positive
    Long price;
    @NotNull
    @Size(min = 1, max = 500)
    String description;
    @NotNull
    @Positive
    Long stock;
    @NotNull
    String categoryId;

    List<String> imageUrls;
}
