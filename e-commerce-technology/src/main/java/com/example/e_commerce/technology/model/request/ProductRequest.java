package com.example.e_commerce.technology.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductRequest {
    @NotNull
    String name;
    @NotNull
    @Positive
    Long price;
    @NotNull
    String description;
    @NotNull
    @Positive
    Long stock;
    @NotNull
    String categoryId;
    List<MultipartFile> images; // Thêm trường images
}
