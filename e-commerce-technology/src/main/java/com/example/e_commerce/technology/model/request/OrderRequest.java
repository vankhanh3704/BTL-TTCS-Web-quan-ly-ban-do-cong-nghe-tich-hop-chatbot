package com.example.e_commerce.technology.model.request;

import com.example.e_commerce.technology.Enum.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotBlank(message = "Địa chỉ giao hàng là bắt buộc")
    String shippingAddress;
    @NotNull(message = "Phương thức thanh toán là bắt buộc")
    PaymentMethod paymentMethod;

    @NotNull(message = "Danh sách sản phẩm không được null")
    @NotEmpty(message = "Danh sách sản phẩm không được rỗng")
    private List<OrderItemRequest> items;
}