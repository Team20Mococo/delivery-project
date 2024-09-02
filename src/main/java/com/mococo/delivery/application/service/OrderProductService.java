package com.mococo.delivery.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mococo.delivery.domain.model.OrderProduct;
import com.mococo.delivery.domain.repository.OrderProductRepository;

@Service
public class OrderProductService {

	@Autowired
	private OrderProductRepository orderProductRepository;

	public List<OrderProduct> getAllOrderProducts() {
		return orderProductRepository.findAll();
	}

	public OrderProduct getOrderProductById(UUID id) {
		return orderProductRepository.findById(id).orElse(null);
	}

	public OrderProduct createOrderProduct(OrderProduct orderProduct) {
		return orderProductRepository.save(orderProduct);
	}

	public void deleteOrderProduct(UUID id) {
		orderProductRepository.deleteById(id);
	}

	// 필요에 따라 추가적인 비즈니스 로직 메서드를 구현할 수 있습니다.
}
