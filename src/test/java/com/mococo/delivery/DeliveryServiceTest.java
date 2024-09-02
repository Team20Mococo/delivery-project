package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mococo.delivery.application.dto.delivery.DeliveryRequestDto;
import com.mococo.delivery.application.dto.delivery.DeliveryResponseDto;
import com.mococo.delivery.application.service.DeliveryService;
import com.mococo.delivery.domain.model.Delivery;
import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.model.enumeration.DeliveryStatus;
import com.mococo.delivery.domain.repository.DeliveryRepository;
import com.mococo.delivery.domain.repository.OrderRepository;

public class DeliveryServiceTest {

	// Mock 객체로 DeliveryRepository와 OrderRepository를 생성합니다.
	@Mock
	private DeliveryRepository deliveryRepository;

	@Mock
	private OrderRepository orderRepository;

	// 위에서 생성한 Mock 객체들을 DeliveryService에 주입합니다.
	@InjectMocks
	private DeliveryService deliveryService;

	private User user;
	private Order order;
	private Delivery delivery;
	private DeliveryRequestDto deliveryRequestDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Mock 객체를 초기화합니다.

		// 테스트에 사용할 User 객체를 생성합니다.
		user = User.builder()
			.username("testuser")
			.build();

		// 테스트에 사용할 Order 객체를 생성하고, 위에서 만든 User 객체와 연결합니다.
		order = Order.builder()
			.orderId(UUID.randomUUID())
			.user(user)
			.build();

		// 테스트에 사용할 DeliveryRequestDto 객체를 생성합니다.
		deliveryRequestDto = DeliveryRequestDto.builder()
			.orderId(order.getOrderId())
			.address("Test Address")
			.status(DeliveryStatus.PENDING)
			.build();

		// 테스트에 사용할 Delivery 객체를 생성하고, 위에서 만든 Order 객체와 연결합니다.
		delivery = Delivery.builder()
			.deliveryId(UUID.randomUUID())
			.order(order)
			.address("Test Address")
			.status(DeliveryStatus.PENDING)
			.build();
	}

	@Test
	void createDelivery_success() {
		// Given: OrderRepository가 order를 찾았을 때 반환하도록 설정
		when(orderRepository.findById(deliveryRequestDto.getOrderId())).thenReturn(Optional.of(order));
		// DeliveryRepository가 Delivery 객체를 저장하고 반환하도록 설정
		when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

		// When: deliveryService의 createDelivery 메서드를 호출
		DeliveryResponseDto response = deliveryService.createDelivery(deliveryRequestDto);

		// Then: 반환된 결과가 올바른지 검증
		assertNotNull(response);
		assertEquals(delivery.getDeliveryId(), response.getDeliveryId());
		assertEquals(delivery.getOrder().getOrderId(), response.getOrderId());
		assertEquals(delivery.getAddress(), response.getAddress());
		assertEquals(delivery.getStatus(), response.getStatus());

		// OrderRepository와 DeliveryRepository가 올바르게 호출되었는지 확인
		verify(orderRepository, times(1)).findById(deliveryRequestDto.getOrderId());
		verify(deliveryRepository, times(1)).save(any(Delivery.class));
	}

	@Test
	void getDeliveryById_success() {
		// Given: DeliveryRepository가 특정 ID로 Delivery 객체를 찾았을 때 반환하도록 설정
		UUID deliveryId = delivery.getDeliveryId();
		when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));

		// When: deliveryService의 getDeliveryById 메서드를 호출
		DeliveryResponseDto response = deliveryService.getDeliveryById(deliveryId);

		// Then: 반환된 결과가 올바른지 검증
		assertNotNull(response);
		assertEquals(delivery.getDeliveryId(), response.getDeliveryId());
		assertEquals(delivery.getOrder().getOrderId(), response.getOrderId());

		// DeliveryRepository가 올바르게 호출되었는지 확인
		verify(deliveryRepository, times(1)).findById(deliveryId);
	}

	@Test
	void updateDelivery_success() {
		// Given: DeliveryRepository가 특정 ID로 Delivery 객체를 찾았을 때 반환하도록 설정
		when(deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
		// DeliveryRepository가 업데이트된 Delivery 객체를 저장하고 반환하도록 설정
		when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

		// 업데이트 요청에 사용할 새로운 DeliveryRequestDto 객체 생성
		DeliveryRequestDto updateRequest = DeliveryRequestDto.builder()
			.address("Updated Address")
			.status(DeliveryStatus.SHIPPED)
			.build();

		// When: deliveryService의 updateDelivery 메서드를 호출
		DeliveryResponseDto response = deliveryService.updateDelivery(delivery.getDeliveryId(), updateRequest);

		// Then: 반환된 결과가 올바른지 검증
		assertNotNull(response);
		assertEquals("Updated Address", response.getAddress());
		assertEquals(DeliveryStatus.SHIPPED, response.getStatus());

		// DeliveryRepository가 올바르게 호출되었는지 확인
		verify(deliveryRepository, times(1)).findById(delivery.getDeliveryId());
		verify(deliveryRepository, times(1)).save(any(Delivery.class));
	}

	@Test
	void updateDeliveryStatus_success() {
		// Given: DeliveryRepository가 특정 ID로 Delivery 객체를 찾았을 때 반환하도록 설정
		when(deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
		// DeliveryRepository가 업데이트된 Delivery 객체를 저장하고 반환하도록 설정
		when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

		// When: deliveryService의 updateDeliveryStatus 메서드를 호출
		DeliveryResponseDto response = deliveryService.updateDeliveryStatus(delivery.getDeliveryId(), "COMPLETED");

		// Then: 반환된 결과가 올바른지 검증
		assertNotNull(response);
		assertEquals(DeliveryStatus.COMPLETED, response.getStatus());

		// DeliveryRepository가 올바르게 호출되었는지 확인
		verify(deliveryRepository, times(1)).findById(delivery.getDeliveryId());
		verify(deliveryRepository, times(1)).save(any(Delivery.class));
	}

	@Test
	void deleteDelivery_success() {
		// Given: DeliveryRepository가 특정 ID로 Delivery 객체를 찾았을 때 반환하도록 설정
		UUID deliveryId = delivery.getDeliveryId();
		when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));

		// When: deliveryService의 deleteDelivery 메서드를 호출
		deliveryService.deleteDelivery(deliveryId);

		// Then: DeliveryRepository가 올바르게 호출되었는지 확인
		verify(deliveryRepository, times(1)).findById(deliveryId);
		verify(deliveryRepository, times(1)).delete(delivery);
	}
}
