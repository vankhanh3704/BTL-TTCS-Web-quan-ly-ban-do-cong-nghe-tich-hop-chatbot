package com.example.e_commerce.technology.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart_item")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    ProductEntity product;

    @Column(nullable = false)
    Integer quantity;
}
