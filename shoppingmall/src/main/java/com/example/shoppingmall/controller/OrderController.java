package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.OrderRequest;
import com.example.shoppingmall.dto.OrderResponse;
import com.example.shoppingmall.dto.OrderStatusUpdateRequest;
import com.example.shoppingmall.dto.PaymentRequest;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.repository.UserRepository;
import com.example.shoppingmall.service.OrderService;
import com.example.shoppingmall.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "주문 API")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/create")
    @Operation(summary = "주문 생성", description = "장바구니 상품을 바탕으로 주문을 생성합니다.")
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("Authorization") String authHeader,
                                                     @RequestBody(required = false) OrderRequest request) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        OrderResponse response = orderService.createOrder(username, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "내 주문 목록 조회", description = "사용자의 주문 내역을 조회합니다.")
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        List<OrderResponse> orders = orderService.getUserOrders(username);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상세 조회", description = "특정 주문 ID로 상세 내역을 조회합니다.")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable Long orderId,
                                                        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        OrderResponse response = orderService.getOrderDetail(orderId, username);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin")
    @Operation(summary = "전체 주문 조회 (관리자)", description = "모든 사용자의 주문 목록을 조회합니다.")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        // 권한 체크 예시
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        if (!user.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403).build(); // 권한 없음
        }

        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/admin/{orderId}/status")
    @Operation(summary = "주문 상태 변경 (관리자)", description = "주문의 상태를 변경합니다.")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        if (!user.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/payment")
    @Operation(summary = "결제 처리", description = "주문 ID를 받아 결제를 완료 처리합니다.")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest request) {
        orderService.processPayment(request);
        return ResponseEntity.ok("결제가 완료되었습니다.");
    }
    
}
