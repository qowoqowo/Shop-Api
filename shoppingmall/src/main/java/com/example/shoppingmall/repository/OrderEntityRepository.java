package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.OrderEntity;
import com.example.shoppingmall.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

    // 사용자 이름으로 주문 목록 조회
    List<OrderEntity> findByUser(User user);
    
}
