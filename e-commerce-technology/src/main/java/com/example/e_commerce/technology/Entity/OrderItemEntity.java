package com.example.e_commerce.technology.Entity;

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
@Table(name = "order_item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    ProductEntity product;

    @Column(nullable = false)
    Integer quantity;

    @Column(nullable = false)
    Long unitPrice;
}