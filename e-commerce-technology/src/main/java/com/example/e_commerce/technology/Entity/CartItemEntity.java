package com.example.e_commerce.technology.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
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
    @JsonBackReference
    CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @JsonIgnore
    ProductEntity product;

    @Column(nullable = false)
    Integer quantity;
}
