package com.example.e_commerce.technology.model.request;


import com.example.e_commerce.technology.Enum.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateOrderStatusRequest {
    @NotNull(message = "Trạng thái đơn hàng là bắt buộc")
    private OrderStatus status;
}
