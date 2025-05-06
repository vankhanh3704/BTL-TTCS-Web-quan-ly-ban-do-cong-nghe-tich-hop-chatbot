package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.CartEntity;
import com.example.e_commerce.technology.Entity.CartItemEntity;
import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.Entity.UserEntity;
import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.mapper.CartMapper;
import com.example.e_commerce.technology.model.request.CartItemRequest;
import com.example.e_commerce.technology.model.response.CartResponse;
import com.example.e_commerce.technology.repository.CartRepository;
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
    public void removeFromCart(Long userId, Long productId) {

    }

    @Override
    public void clearCart(Long userId) {

    }
}
