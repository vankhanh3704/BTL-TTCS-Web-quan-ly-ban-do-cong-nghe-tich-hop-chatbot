package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.*;
import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.Enum.PaymentMethod;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.mapper.CartMapper;
import com.example.e_commerce.technology.model.request.CartItemRequest;
import com.example.e_commerce.technology.model.response.CartResponse;
import com.example.e_commerce.technology.model.response.OrderResponse;
import com.example.e_commerce.technology.repository.CartRepository;
import com.example.e_commerce.technology.repository.OrderRepository;
import com.example.e_commerce.technology.repository.ProductRepository;
import com.example.e_commerce.technology.repository.UserRepository;
import com.example.e_commerce.technology.service.ICartService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CartService implements ICartService {

    UserRepository userRepository;
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;
    OrderRepository orderRepository;


    @Override
    public CartResponse addToCart(String userId, CartItemRequest request) {
        // Tìm user
        UserEntity user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy hoặc tạo mới giỏ hàng
        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> CartEntity.builder()
                        .user(user)
                        .items(new ArrayList<>()) // đảm bảo không null
                        .build());

        // Tìm sản phẩm
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        log.info("Product stock: {}, Requested quantity: {}", product.getStock(), request.getQuantity());

        // Kiểm tra tồn kho
        if (product.getStock() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Tìm item trong giỏ, nếu có
        List<CartItemEntity> cartItems = cart.getItems();
        CartItemEntity item = cartItems.stream()
                .filter(i -> i.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            // Tạo mới nếu chưa có
            item = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cartItems.add(item);
        } else {
            // Tăng số lượng nếu đã có
            item.setQuantity(item.getQuantity() + request.getQuantity());
        }

        // Lưu giỏ hàng
        cartRepository.save(cart);

        // Tạo response
        List<CartResponse.CartItemResponse> itemResponses = cartItems.stream()
                .map(cartMapper::toCartItemResponse)
                .collect(Collectors.toList());

        Long totalAmount = itemResponses.stream()
                .mapToLong(CartResponse.CartItemResponse::getSubTotal)
                .sum();
        return CartResponse.builder()
                .id(cart.getId())
                .items(itemResponses)
                .totalAmount(totalAmount)
                .build();
    }


    @Override
    public CartResponse getCart(String userId) {
        UserEntity user = userRepository.findByUsername(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        CartEntity cart =  cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        List<CartResponse.CartItemResponse> itemResponses = cart.getItems().stream()
                .map(cartMapper::toCartItemResponse)
                .collect(Collectors.toList());

        long totalAmount = itemResponses.stream()
                .mapToLong(CartResponse.CartItemResponse::getSubTotal)
                .sum();

        return CartResponse.builder()
                .id(cart.getId())
                .items(itemResponses)
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    public void removeFromCart(String userId, Long productId) {
        UserEntity user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow( () -> new AppException(ErrorCode.CART_NOT_FOUND));

        List<CartItemEntity> cartItems = cart.getItems();
        boolean removed = cartItems.removeIf(
                item -> item.getProduct().getId().equals(productId));
        if (!removed) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {
        UserEntity user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        List<CartItemEntity> cartItems = cart.getItems();
        cartItems.clear();
        cartRepository.save(cart);

    }

    @Override
    public OrderResponse checkout(String userId, String shippingAddress, String paymentMethod) {
        UserEntity user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        List<CartItemEntity> cartItems = cart.getItems();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }

        // Kiểm tra tồn kho
        for (CartItemEntity item : cartItems) {
            ProductEntity product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
        }

        // Tạo đơn hàng
        OrderEntity order = OrderEntity.builder()
                .user(user)
                .shippingAddress(shippingAddress)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod.toUpperCase()))
                .totalAmount(cartItems.stream()
                        .mapToLong(item -> item.getQuantity() * item.getProduct().getPrice())
                        .sum())
                .build();

        // Thêm các mục đơn hàng
        List<OrderItemEntity> orderItems = cartItems.stream().map(cartItem -> OrderItemEntity.builder()
                .order(order)
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getProduct().getPrice())
                .build()).collect(Collectors.toList());
        order.setItems(orderItems);

        // Lưu đơn hàng
        orderRepository.save(order);

        // Giảm tồn kho
        for (CartItemEntity item : cartItems) {
            ProductEntity product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        // Xóa giỏ hàng
        clearCart(userId);

        // Map sang OrderResponse
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(user.getId());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentMethod(order.getPaymentMethod().name());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalAmount(order.getTotalAmount());
        response.setItems(order.getItems().stream().map(orderItem -> {
            OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
            itemResponse.setId(orderItem.getId());
            itemResponse.setProductId(orderItem.getProduct().getId());
            itemResponse.setProductName(orderItem.getProduct().getName());
            itemResponse.setUnitPrice(orderItem.getUnitPrice());
            itemResponse.setQuantity(orderItem.getQuantity());
            itemResponse.setSubTotal(orderItem.getQuantity() * orderItem.getUnitPrice());
            return itemResponse;
        }).collect(Collectors.toList()));

        return response;
    }
}
