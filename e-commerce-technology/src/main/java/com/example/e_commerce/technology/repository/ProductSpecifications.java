package com.example.e_commerce.technology.repository;

import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.model.request.ProductSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {

    public static Specification<ProductEntity> withSearchRequest(ProductSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm kiếm theo tên (LIKE)
            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }

            // Tìm kiếm theo mô tả (LIKE)
            if (request.getDescription() != null && !request.getDescription().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + request.getDescription().toLowerCase() + "%"));
            }

            // Tìm kiếm theo khoảng giá
            if (request.getPriceFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), request.getPriceFrom()));
            }
            if (request.getPriceTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), request.getPriceTo()));
            }

            // Tìm kiếm theo khoảng tồn kho
            if (request.getStockFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stock"), request.getStockFrom()));
            }
            if (request.getStockTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("stock"), request.getStockTo()));
            }

            // Tìm kiếm theo danh mục
            if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("name"), request.getCategoryId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}