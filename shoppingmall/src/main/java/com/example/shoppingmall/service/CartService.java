package com.example.shoppingmall.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.dto.CartItemRequest;
import com.example.shoppingmall.dto.CartItemResponse;
import com.example.shoppingmall.dto.CartQuantityUpdateRequest;
import com.example.shoppingmall.entity.CartItem;
import com.example.shoppingmall.repository.CartItemRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 장바구니 담기
    public void addToCart(String username, CartItemRequest request) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        var existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(newItem);
        }
    }
    
    // 장바구니 조회
    public List<CartItemResponse> getCartItems(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        return cartItemRepository.findByUser(user).stream()
                .map(item -> {
                    int price = item.getProduct().getPrice();
                    int quantity = item.getQuantity();
                    return CartItemResponse.builder()
                            .productId(item.getProduct().getId())
                            .productName(item.getProduct().getName())
                            .quantity(quantity)
                            .price(price)
                            .totalPrice(price * quantity) // 총합 계산!
                            .build();
                })
                .toList();
    }
    
    // 장바구니 아이템 삭제
    public void removeFromCart(String username, Long productId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        var item = cartItemRepository.findByUserAndProduct(user, product);
        item.ifPresent(cartItemRepository::delete);
    }
    
    // 장바구니 아이템 수량 업데이트
    public void updateCartItemQuantity(String username, CartQuantityUpdateRequest request) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        var cartItemOpt = cartItemRepository.findByUserAndProduct(user, product);

        if (cartItemOpt.isPresent()) {
            CartItem item = cartItemOpt.get();
            if (request.getQuantity() <= 0) {
                cartItemRepository.delete(item); // 수량이 0 이하이면 삭제
            } else {
                item.setQuantity(request.getQuantity());
                cartItemRepository.save(item); // 수량만 업데이트
            }
        } else {
            throw new IllegalStateException("장바구니에 해당 상품이 없습니다.");
        }
    }

    
}

