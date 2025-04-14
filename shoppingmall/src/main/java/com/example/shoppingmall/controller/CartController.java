package com.example.shoppingmall.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.dto.CartItemRequest;
import com.example.shoppingmall.dto.CartItemResponse;
import com.example.shoppingmall.dto.CartQuantityUpdateRequest;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@Tag(name = "Cart API", description = "장바구니 API")
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @PostMapping("/add")
    @Operation(summary = "장바구니 담기", description = "상품 ID와 수량을 넘겨받아 장바구니에 추가합니다.")
    public ResponseEntity<String> addToCart(@RequestBody CartItemRequest request,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        cartService.addToCart(username, request);
        return ResponseEntity.ok("장바구니에 담겼습니다.");
    }
    
    @GetMapping
    @Operation(summary = "장바구니 조회", description = "사용자의 장바구니에 담긴 상품 목록을 조회합니다.")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        List<CartItemResponse> cartItems = cartService.getCartItems(username);
        return ResponseEntity.ok(cartItems);
    }
    
    @DeleteMapping("/{productId}")
    @Operation(summary = "장바구니에서 상품 삭제", description = "장바구니에서 특정 상품을 삭제합니다.")
    public ResponseEntity<String> removeItem(@PathVariable Long productId,
                                             @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        cartService.removeFromCart(username, productId);
        return ResponseEntity.ok("장바구니에서 삭제되었습니다.");
    }
    
    @PatchMapping("/update")
    @Operation(summary = "장바구니 수량 수정", description = "장바구니 상품의 수량을 변경합니다. 0 이하일 경우 삭제됩니다.")
    public ResponseEntity<String> updateQuantity(@RequestBody CartQuantityUpdateRequest request,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        cartService.updateCartItemQuantity(username, request);
        return ResponseEntity.ok("장바구니 수량이 수정되었습니다.");
    }

}

