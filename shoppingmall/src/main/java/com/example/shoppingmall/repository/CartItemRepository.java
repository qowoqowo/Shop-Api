package com.example.shoppingmall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shoppingmall.entity.CartItem;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.entity.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.user = :user")
    List<CartItem> findByUser(@Param("user")User user);
    
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    
}
