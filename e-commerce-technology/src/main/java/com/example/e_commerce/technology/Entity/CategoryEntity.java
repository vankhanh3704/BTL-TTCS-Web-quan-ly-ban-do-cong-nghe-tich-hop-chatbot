package com.example.e_commerce.technology.Entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
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
