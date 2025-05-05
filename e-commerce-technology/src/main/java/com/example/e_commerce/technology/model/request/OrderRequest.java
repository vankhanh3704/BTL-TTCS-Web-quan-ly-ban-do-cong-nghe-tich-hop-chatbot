package com.example.e_commerce.technology.model.request;

import com.example.e_commerce.technology.Enum.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotBlank
    String shippingAddress;
    @NotNull
    PaymentMethod paymentMethod;
}