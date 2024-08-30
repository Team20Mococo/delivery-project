package com.mococo.delivery.adapters.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.mococo.delivery.application.dto.order.OrderRequestDto;
import com.mococo.delivery.application.dto.order.OrderResponseDto;
import com.mococo.delivery.application.dto.order.OrderStatusUpdateDto;
import com.mococo.delivery.application.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	// 주문 생성
	@PostMapping("/orders")
	public SuccessResponseDto<OrderResponseDto> createOrder(@RequestBody OrderRequestDto request) {
		OrderResponseDto response = orderService.createOrder(request, request.getUsername());
		return new SuccessResponseDto<>("주문이 생성되었습니다.", response);
	}

	// 주문 단건 조회
	@GetMapping("/orders/{orderId}")
	public SuccessResponseDto<OrderResponseDto> getOrderById(@PathVariable UUID orderId,
		@RequestParam String username) {
		OrderResponseDto response = orderService.getOrderById(orderId, username);
		return new SuccessResponseDto<>("주문 조회에 성공했습니다.", response);
	}

	// 주문 목록 조회 (점주)
	@GetMapping("/owner/orders")
	public SuccessResponseDto<Page<OrderResponseDto>> getOrderListByOwner(
		@RequestParam(required = false) String sortBy,
		@RequestParam(defaultValue = "asc") String direction,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String searchQuery
	) {
		PageRequest pageRequest = PageRequest.of(page, Math.min(size, 50),
			Sort.by(Sort.Direction.fromString(direction), sortBy != null ? sortBy : "createdAt"));
		Page<OrderResponseDto> response = orderService.searchOrdersForOwner(searchQuery, pageRequest);
		return new SuccessResponseDto<>("주문 목록 조회 (점주)에 성공했습니다.", response);
	}

	// 주문 목록 조회 (소비자)
	@GetMapping("/consumer/orders")
	public SuccessResponseDto<Page<OrderResponseDto>> getOrderListByConsumer(
		@RequestParam(required = false) String sortBy,
		@RequestParam(defaultValue = "asc") String direction,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String searchQuery
	) {
		PageRequest pageRequest = PageRequest.of(page, Math.min(size, 50),
			Sort.by(Sort.Direction.fromString(direction), sortBy != null ? sortBy : "createdAt"));
		Page<OrderResponseDto> response = orderService.searchOrdersForConsumer(searchQuery, pageRequest);
		return new SuccessResponseDto<>("주문 목록 조회 (소비자)에 성공했습니다.", response);
	}

	// 주문 상태 변경 (주문 상태는 사장님만 변경 가능)
	@PatchMapping("/orders/{orderId}")
	public SuccessResponseDto<OrderResponseDto> updateOrderStatus(@PathVariable UUID orderId,
		@RequestBody OrderStatusUpdateDto request, @RequestParam String username) {
		OrderResponseDto response = orderService.updateOrderStatus(orderId, request.getStatus(), username);
		return new SuccessResponseDto<>("주문 상태가 변경되었습니다.", response);
	}

	// 주문 취소 (5분 이내에만 가능)
	@DeleteMapping("/orders/{orderId}")
	public SuccessResponseDto<Boolean> cancelOrder(@PathVariable UUID orderId, @RequestParam String username) {
		orderService.cancelOrder(orderId, username);
		return new SuccessResponseDto<>("주문이 취소되었습니다.", true);
	}
}
