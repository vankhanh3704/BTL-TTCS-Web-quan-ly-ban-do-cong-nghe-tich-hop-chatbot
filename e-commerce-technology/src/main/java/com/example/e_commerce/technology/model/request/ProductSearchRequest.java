package com.example.e_commerce.technology.model.request;


import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductSearchRequest {
    String name;
    @Positive
    Long priceFrom;
    @Positive
    Long priceTo;
    String description;
    String color;
    @Positive
    Long stockFrom;
    @Positive
    Long stockTo;
    String categoryId;

    public void validate() {
        if (priceFrom != null && priceTo != null && priceFrom > priceTo) {
            throw new IllegalArgumentException("priceFrom must be less than or equal to priceTo");
        }
        if (stockFrom != null && stockTo != null && stockFrom > stockTo) {
            throw new IllegalArgumentException("stockFrom must be less than or equal to stockTo");
        }
    }
}
