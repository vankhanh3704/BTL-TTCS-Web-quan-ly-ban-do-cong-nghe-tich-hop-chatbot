package com.example.e_commerce.technology.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "ProductImage")
@Builder
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String image_url;

    @Column
    String altText;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    ProductEntity product;

}
