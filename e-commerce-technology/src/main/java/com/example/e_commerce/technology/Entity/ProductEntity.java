package com.example.e_commerce.technology.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    Long price;
    @Column(columnDefinition = "TEXT",nullable = false)
    String description;
    @Column(nullable = false)
    String color;
    @Column(nullable = false)
    Long stock;
    @Column(nullable = false)
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductImageEntity> images = new ArrayList<>();;


    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
