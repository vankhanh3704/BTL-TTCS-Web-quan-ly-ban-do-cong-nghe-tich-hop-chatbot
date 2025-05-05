package com.example.e_commerce.technology.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    @JsonProperty("id")
    String id;
    String productId;
    String productName;
    Integer quantity;
    Long unitPrice;
}
