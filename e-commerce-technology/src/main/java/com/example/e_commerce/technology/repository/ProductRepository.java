package com.example.e_commerce.technology.repository;

import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    boolean existsByName(String name);
    Page<ProductEntity> findByCategoryName(String categoryName, Pageable pageable);
    Optional<ProductEntity> findByName(String name);
}
