package com.mococo.delivery.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mococo.delivery.application.dto.delivery.DeliveryRequestDto;
import com.mococo.delivery.application.dto.delivery.DeliveryResponseDto;
import com.mococo.delivery.domain.model.Delivery;
import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.enumeration.DeliveryStatus;
import com.mococo.delivery.domain.repository.DeliveryRepository;
import com.mococo.delivery.domain.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public DeliveryResponseDto createDelivery(DeliveryRequestDto requestDto) {
		// Validate the address
		if (requestDto.getAddress() == null || requestDto.getAddress().trim().isEmpty()) {
			throw new IllegalArgumentException("배달 주소는 null이거나 빈 값일 수 없습니다.");
		}

		Order order = orderRepository.findById(requestDto.getOrderId())
			.orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

		Delivery delivery = Delivery.builder()
			.order(order)
			.address(requestDto.getAddress())
			.status(requestDto.getStatus())
			.shippedAt(requestDto.getShippedAt())
			.deliveredAt(requestDto.getDeliveredAt())
			.build();

		Delivery savedDelivery = deliveryRepository.save(delivery);

		return convertToDto(savedDelivery);
	}

	@Transactional(readOnly = true)
	public DeliveryResponseDto getDeliveryById(UUID deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new RuntimeException("배달을 찾을 수 없습니다."));

		return convertToDto(delivery);
	}

	@Transactional
	public DeliveryResponseDto updateDelivery(UUID deliveryId, DeliveryRequestDto requestDto) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new RuntimeException("배달을 찾을 수 없습니다."));

		delivery.setAddress(requestDto.getAddress());
		delivery.setStatus(requestDto.getStatus());
		delivery.setShippedAt(requestDto.getShippedAt());
		delivery.setDeliveredAt(requestDto.getDeliveredAt());

		Delivery updatedDelivery = deliveryRepository.save(delivery);

		return convertToDto(updatedDelivery);
	}

	@Transactional
	public DeliveryResponseDto updateDeliveryStatus(UUID deliveryId, String status) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new RuntimeException("배달을 찾을 수 없습니다."));

		try {
			DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase());
			delivery.setStatus(deliveryStatus);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("잘못된 배달 상태 값입니다.");
		}

		Delivery updatedDelivery = deliveryRepository.save(delivery);
		return convertToDto(updatedDelivery);
	}

	@Transactional
	public void deleteDelivery(UUID deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new RuntimeException("배달을 찾을 수 없습니다."));

		deliveryRepository.delete(delivery);
	}

	private DeliveryResponseDto convertToDto(Delivery delivery) {
		return DeliveryResponseDto.builder()
			.deliveryId(delivery.getDeliveryId())
			.orderId(delivery.getOrder().getOrderId())
			.address(delivery.getAddress())  // 필드명을 address로 변경
			.status(delivery.getStatus())
			.shippedAt(delivery.getShippedAt())
			.deliveredAt(delivery.getDeliveredAt())
			.createdAt(delivery.getCreatedAt())
			.updatedAt(delivery.getUpdatedAt())
			.build();
	}
}
