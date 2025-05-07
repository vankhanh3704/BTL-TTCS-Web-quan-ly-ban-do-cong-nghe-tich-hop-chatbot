package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.OrderEntity;
import com.example.e_commerce.technology.Entity.OrderItemEntity;
import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.Entity.UserEntity;
import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.mapper.OrderMapper;
import com.example.e_commerce.technology.model.request.OrderItemRequest;
import com.example.e_commerce.technology.model.request.OrderRequest;
import com.example.e_commerce.technology.model.response.OrderResponse;
import com.example.e_commerce.technology.repository.OrderRepository;
import com.example.e_commerce.technology.repository.ProductRepository;
import com.example.e_commerce.technology.repository.UserRepository;
import com.example.e_commerce.technology.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    UserRepository userRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(String userId, OrderRequest request) {
        UserEntity user = userRepository.findByUsername(userId)
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra tồn kho
        // Lưu sản phẩm vào map để tránh truy vấn lặp
        Map<Long, ProductEntity> productMap = new HashMap<>();
        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            productMap.put(itemRequest.getProductId(), product);
        }

        Long totalAmount = request.getItems().stream()
                .mapToLong(item -> item.getQuantity() * productMap.get(item.getProductId()).getPrice())
                .sum();

        // Tạo đơn hàng
        OrderEntity order = OrderEntity.builder()
                .user(user)
                .shippingAddress(request.getShippingAddress())
                .totalAmount(totalAmount)
                .paymentMethod(request.getPaymentMethod())
                .build();

        // Tạo các mục đơn hàng
        List<OrderItemEntity> orderItems = request.getItems().stream().map(itemRequest -> {
            ProductEntity product = productMap.get(itemRequest.getProductId());
            return OrderItemEntity.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
        }).collect(Collectors.toList());
        order.setItems(orderItems);

        // Lưu đơn hàng
        orderRepository.save(order);
        log.info("Đã tạo đơn hàng ID: {} cho người dùng: {}", order.getId(), userId);

        // Cập nhật tồn kho
        for (OrderItemEntity item : orderItems) {
            ProductEntity product = productMap.get(item.getProduct().getId());
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        return mapToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getUserOrders(String userId, Pageable pageable) {
        log.info("Truy vấn đơn hàng của người dùng: {}, pageable: {}", userId, pageable);
        Page<OrderEntity> ordersPage = orderRepository.findByUserUsername(userId, pageable);
        log.info("Tìm thấy {} đơn hàng, trang {}, kích thước {}", ordersPage.getTotalElements(), pageable.getPageNumber(), pageable.getPageSize());
        return ordersPage.map(this::mapToOrderResponse);
    }

    @Override
    public Page<OrderResponse> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::mapToOrderResponse);
    }


    private OrderResponse mapToOrderResponse(OrderEntity order) {
        OrderResponse response = orderMapper.toOrderResponse(order);
        response.setItems(orderMapper.toOrderItemResponses(order.getItems()));
        return response;
    }
}
