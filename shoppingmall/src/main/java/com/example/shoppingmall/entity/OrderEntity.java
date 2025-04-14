package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 주문 아이템들
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    private int totalPrice;
    
    @Column(nullable = false)
    private String orderStatus; // 예: "결제완료", "배송중", "배송완료"

    private LocalDateTime orderDate;
    
    @Column(nullable = false)
    private boolean cancelled; // 주문 취소 여부 (true: 취소됨, false: 정상주문)
}
