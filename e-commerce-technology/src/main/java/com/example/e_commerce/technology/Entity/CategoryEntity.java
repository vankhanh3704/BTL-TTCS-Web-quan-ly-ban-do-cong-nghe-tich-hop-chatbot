package com.example.e_commerce.technology.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Category")
public class CategoryEntity {
    @Id
    @Column(name = "name", nullable = false)
    String name;
    String description;
}
