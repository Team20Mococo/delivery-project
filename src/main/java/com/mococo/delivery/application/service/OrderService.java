package com.mococo.delivery.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mococo.delivery.application.dto.order.OrderRequestDto;
import com.mococo.delivery.application.dto.order.OrderResponseDto;
import com.mococo.delivery.application.dto.order.OrderSearchCriteriaDto;
import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.model.enumeration.OrderStatus;
import com.mococo.delivery.domain.model.enumeration.OrderType;
import com.mococo.delivery.domain.repository.OrderRepository;
import com.mococo.delivery.domain.repository.StoreRepository;
import com.mococo.delivery.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	@Transactional
	public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, String username) {
		Store store = storeRepository.findById(orderRequestDto.getStoreId())
			.orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		OrderType orderType = OrderType.valueOf(orderRequestDto.getType().toUpperCase());

		Order order = Order.builder()
			.store(store)
			.user(user)
			.type(orderType)  // OrderType 사용
			.totalPrice(orderRequestDto.getTotalPrice())
			.address(orderRequestDto.getAddress())
			.request(orderRequestDto.getRequest())
			.orderStatus(OrderStatus.READY)
			.build();

		order.setCreatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(order);

		return convertToDto(savedOrder);
	}

	@Transactional(readOnly = true)
	public OrderResponseDto getOrderById(UUID orderId, String username) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

		checkAccess(order, username);

		return convertToDto(order);
	}

	@Transactional(readOnly = true)
	public Page<OrderResponseDto> searchOrdersForOwner(String searchQuery, PageRequest pageRequest) {
		OrderSearchCriteriaDto searchCriteria = new OrderSearchCriteriaDto();
		// 추가적인 검색 조건 로직
		Page<Order> orders = orderRepository.searchOrders(searchCriteria, pageRequest);
		return orders.map(this::convertToDto);
	}

	@Transactional(readOnly = true)
	public Page<OrderResponseDto> searchOrdersForConsumer(String searchQuery, PageRequest pageRequest) {
		OrderSearchCriteriaDto searchCriteria = new OrderSearchCriteriaDto();
		// 추가적인 검색 조건 로직
		Page<Order> orders = orderRepository.searchOrders(searchCriteria, pageRequest);
		return orders.map(this::convertToDto);
	}

	@Transactional
	public OrderResponseDto updateOrderStatus(UUID orderId, String status, String username) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

		checkOwnerAccess(order, username);

		order.setOrderStatus(OrderStatus.valueOf(status));
		Order updatedOrder = orderRepository.save(order);

		return convertToDto(updatedOrder);
	}

	@Transactional
	public void cancelOrder(UUID orderId, String username) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

		checkAccess(order, username);

		if (order.getOrderStatus() == OrderStatus.READY &&
			LocalDateTime.now().isBefore(order.getCreatedAt().plusMinutes(5))) {
			order.setOrderStatus(OrderStatus.CANCELLED);
			order.setDeletedAt(LocalDateTime.now());
			order.setDeletedBy(username);
			orderRepository.save(order);
		} else {
			throw new IllegalStateException("주문은 5분 이내에만 취소할 수 있습니다.");
		}
	}

	private OrderResponseDto convertToDto(Order order) {
		return OrderResponseDto.builder()
			.orderId(order.getOrderId())
			.username(order.getUser().getUsername())
			.storeId(order.getStore().getId())
			.storeName(order.getStore().getName())
			.type(order.getType().name())
			.totalPrice(order.getTotalPrice())
			.address(order.getAddress())
			.request(order.getRequest())
			.status(order.getOrderStatus().name())
			.createdAt(order.getCreatedAt() != null ? order.getCreatedAt().toString() : null)
			.build();
	}

	private void checkAccess(Order order, String username) {
		if (!order.getUser().getUsername().equals(username) &&
			!order.getStore().getOwner().getUsername().equals(username)) {
			throw new RuntimeException("접근 권한이 없습니다.");
		}
	}

	private void checkOwnerAccess(Order order, String username) {
		if (!order.getStore().getOwner().getUsername().equals(username)) {
			throw new RuntimeException("사장님만 접근 가능합니다.");
		}
	}
}
