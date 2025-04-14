package com.example.shoppingmall.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.dto.OrderItemResponse;
import com.example.shoppingmall.dto.OrderRequest;
import com.example.shoppingmall.dto.OrderResponse;
import com.example.shoppingmall.dto.PaymentRequest;
import com.example.shoppingmall.entity.CartItem;
import com.example.shoppingmall.entity.OrderEntity;
import com.example.shoppingmall.entity.OrderItemEntity;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.repository.CartItemRepository;
import com.example.shoppingmall.repository.OrderEntityRepository;
import com.example.shoppingmall.repository.OrderItemEntityRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderEntityRepository orderRepository;
    private final OrderItemEntityRepository orderItemRepository;
    private final ProductRepository productRepository;

    // 장바구니 상품 주문
    @Transactional
    public OrderResponse createOrder(String username, OrderRequest request) {
        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        // 선택된 장바구니 항목만 조회
        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("선택된 장바구니 항목이 없습니다.");
        }

        // 해당 유저의 장바구니인지 검증
        for (CartItem item : cartItems) {
            if (!item.getUser().getUsername().equals(username)) {
                throw new IllegalStateException("다른 사용자의 장바구니 항목이 포함되어 있습니다.");
            }
        }

        // 총 금액 계산
        int totalPrice = cartItems.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // 주문 생성
        OrderEntity order = OrderEntity.builder()
                .user(user)
                .totalPrice(totalPrice)
                .orderDate(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        // 주문 항목 생성 + 재고 차감
        List<OrderItemEntity> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            // 재고 차감
            int newStock = product.getStock() - cartItem.getQuantity();
            if (newStock < 0) {
                throw new IllegalStateException("상품 재고가 부족합니다: " + product.getName());
            }
            product.setStock(newStock);  // 재고 업데이트

            return OrderItemEntity.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();
        }).toList();

        // 주문 저장
        orderItemRepository.saveAll(orderItems);

        // 재고 변경 반영
        productRepository.saveAll(
            orderItems.stream().map(OrderItemEntity::getProduct).toList()
        );
        
        // 장바구니 비우기
        cartItemRepository.deleteAll(cartItems);

        // 응답 DTO 반환
        List<OrderItemResponse> itemResponses = orderItems.stream().map(item ->
                OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.getPrice() * item.getQuantity())
                        .build()
        ).toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .items(itemResponses)
                .build();
    }

    // 주문 내역
    @Transactional
    public List<OrderResponse> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        List<OrderEntity> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> {
            List<OrderItemResponse> itemResponses = order.getItems().stream().map(item ->
                    OrderItemResponse.builder()
                            .productId(item.getProduct().getId())
                            .productName(item.getProduct().getName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .totalPrice(item.getQuantity() * item.getPrice())
                            .build()
            ).toList();

            return OrderResponse.builder()
                    .orderId(order.getId())
                    .orderDate(order.getOrderDate())
                    .totalPrice(order.getTotalPrice())
                    .items(itemResponses)
                    .build();
        }).toList();
    }
    
    // 주문 상세 조회
    public OrderResponse getOrderDetail(Long orderId, String username) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 자신의 주문인지 확인
        if (!order.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("해당 주문에 접근할 수 없습니다.");
        }

        List<OrderItemResponse> itemResponses = order.getItems().stream().map(item ->
                OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.getQuantity() * item.getPrice())
                        .build()
        ).toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .items(itemResponses)
                .build();
    }
    
    // 주문 전체 조회 (관리자)
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            List<OrderItemResponse> itemResponses = order.getItems().stream().map(item ->
                    OrderItemResponse.builder()
                            .productId(item.getProduct().getId())
                            .productName(item.getProduct().getName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .totalPrice(item.getQuantity() * item.getPrice())
                            .build()
            ).toList();

            return OrderResponse.builder()
                    .orderId(order.getId())
                    .orderDate(order.getOrderDate())
                    .totalPrice(order.getTotalPrice())
                    .items(itemResponses)
                    .build();
        }).toList();
    }

    // 주문 상태 변경
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        order.setOrderStatus(newStatus);
    }
    
    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId, String username) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        if (!order.getUser().getUsername().equals(username) && !user.getRole().equals("ADMIN")) {
            throw new IllegalStateException("해당 주문을 취소할 수 없습니다.");
        }

        if (order.isCancelled()) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        // 주문 취소
        order.setCancelled(true);

        // 재고 복구
        for (OrderItemEntity item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        // 저장
        productRepository.saveAll(order.getItems().stream().map(OrderItemEntity::getProduct).toList());
        orderRepository.save(order);
    }
    
    // 결제 처리
    @Transactional
    public void processPayment(PaymentRequest request) {
        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        // 사용자 확인
        if (!order.getUser().getUsername().equals(request.getUsername())) {
            throw new IllegalStateException("해당 주문의 결제를 처리할 수 없습니다.");
        }

        if (order.isCancelled()) {
            throw new IllegalStateException("취소된 주문은 결제할 수 없습니다.");
        }

        if (!order.getOrderStatus().equals("결제대기")) {
            throw new IllegalStateException("이미 결제가 완료된 주문입니다.");
        }

        // 결제 처리 완료
        order.setOrderStatus("결제완료");
        orderRepository.save(order);
    }

    

}
