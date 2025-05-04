package com.example.e_commerce.technology.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleEntity {
    @Id
    String name;
    String description;
}
