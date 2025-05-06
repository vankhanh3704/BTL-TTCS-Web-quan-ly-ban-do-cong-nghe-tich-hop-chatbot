package com.example.e_commerce.technology.repository;

import com.example.e_commerce.technology.Entity.OrderEntity;
import com.example.e_commerce.technology.Enum.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByUserId(Long userId, Pageable pageable);
    Page<OrderEntity> findByStatus(OrderStatus status, Pageable pageable);
}
