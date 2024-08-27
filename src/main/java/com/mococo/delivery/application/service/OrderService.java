package com.mococo.delivery.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.enumeration.OrderStatus;
import com.mococo.delivery.domain.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional
	public Order createOrder(Order order) {
		order.setCreatedAt(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.READY);
		return orderRepository.save(order);
	}

	public Order getOrderById(UUID orderId) {
		return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Transactional
	public void updateOrderStatus(UUID orderId, OrderStatus status) {
		Order order = getOrderById(orderId);
		order.setOrderStatus(status);
		order.setUpdatedAt(LocalDateTime.now());
		orderRepository.save(order);
	}

	@Transactional
	public void cancelOrder(UUID orderId) {
		Order order = getOrderById(orderId);
		order.cancelOrder();
		orderRepository.save(order);
	}
}
