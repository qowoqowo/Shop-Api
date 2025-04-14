package com.example.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shoppingmall.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	
	
}
