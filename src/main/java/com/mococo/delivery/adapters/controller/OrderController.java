package com.mococo.delivery.adapters.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.service.OrderService;
import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.enumeration.OrderStatus;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<SuccessResponseDto<Order>> createOrder(@RequestBody Order order) {
		Order createdOrder = orderService.createOrder(order);
		return ResponseEntity.ok(new SuccessResponseDto<>("주문이 성공적으로 생성되었습니다.", createdOrder));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<SuccessResponseDto<Order>> getOrderById(@PathVariable UUID orderId) {
		Order order = orderService.getOrderById(orderId);
		return ResponseEntity.ok(new SuccessResponseDto<>("주문을 성공적으로 조회했습니다.", order));
	}

	@GetMapping
	public ResponseEntity<SuccessResponseDto<List<Order>>> getAllOrders() {
		List<Order> orders = orderService.getAllOrders();
		return ResponseEntity.ok(new SuccessResponseDto<>("모든 주문을 성공적으로 조회했습니다.", orders));
	}

	@PatchMapping("/{orderId}/status")
	public ResponseEntity<SuccessResponseDto<Void>> updateOrderStatus(@PathVariable UUID orderId,
		@RequestParam OrderStatus status) {
		orderService.updateOrderStatus(orderId, status);
		return ResponseEntity.ok(new SuccessResponseDto<>("주문 상태가 성공적으로 업데이트되었습니다.", null));
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteOrder(@PathVariable UUID orderId) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.ok(new SuccessResponseDto<>("주문이 성공적으로 취소되었습니다.", null));

	}
}
