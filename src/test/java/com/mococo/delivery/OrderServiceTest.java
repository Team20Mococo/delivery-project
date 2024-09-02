package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.mococo.delivery.application.dto.order.OrderRequestDto;
import com.mococo.delivery.application.dto.order.OrderResponseDto;
import com.mococo.delivery.application.service.OrderService;
import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.model.enumeration.OrderStatus;
import com.mococo.delivery.domain.model.enumeration.OrderType;
import com.mococo.delivery.domain.repository.OrderRepository;
import com.mococo.delivery.domain.repository.StoreRepository;
import com.mococo.delivery.domain.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private OrderService orderService;

	private OrderRequestDto orderRequestDto;
	private User user;
	private Store store;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = User.builder()
			.username("testuser")
			.build();

		store = Store.builder()
			.id(UUID.randomUUID())
			.name("Test Store")
			.owner(user)  // Store 객체의 owner 필드 설정
			.build();

		orderRequestDto = OrderRequestDto.builder()
			.storeId(store.getId())
			.username(user.getUsername())
			.type(OrderType.ONLINE.name())
			.totalPrice(10000)
			.address("Test Address")
			.request("Please be quick")
			.build();
	}

	@Test
	void addOrder_success() {
		// Given
		when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.of(store));
		when(userRepository.findByUsername(orderRequestDto.getUsername())).thenReturn(Optional.of(user));

		Order order = Order.builder()
			.orderId(UUID.randomUUID())
			.store(store)
			.user(user)
			.type(OrderType.ONLINE)
			.totalPrice(orderRequestDto.getTotalPrice())
			.address(orderRequestDto.getAddress())
			.request(orderRequestDto.getRequest())
			.orderStatus(OrderStatus.READY)
			.createdAt(LocalDateTime.now())
			.build();

		when(orderRepository.save(any(Order.class))).thenReturn(order);

		// When
		OrderResponseDto response = orderService.createOrder(orderRequestDto, orderRequestDto.getUsername());

		// Then
		assertNotNull(response);
		assertEquals(orderRequestDto.getUsername(), response.getUsername());
		assertEquals(orderRequestDto.getStoreId(), response.getStoreId());
		assertEquals(OrderStatus.READY.name(), response.getStatus());

		verify(storeRepository, times(1)).findById(orderRequestDto.getStoreId());
		verify(userRepository, times(1)).findByUsername(orderRequestDto.getUsername());
		verify(orderRepository, times(1)).save(any(Order.class));
	}

	@Test
	void addOrder_storeNotFound_throwsException() {
		// Given
		when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.empty());

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.createOrder(orderRequestDto, orderRequestDto.getUsername());
		});

		assertEquals("가게를 찾을 수 없습니다.", exception.getMessage());
		verify(storeRepository, times(1)).findById(orderRequestDto.getStoreId());
		verify(userRepository, never()).findByUsername(anyString());
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void addOrder_userNotFound_throwsException() {
		// Given
		when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.of(store));
		when(userRepository.findByUsername(orderRequestDto.getUsername())).thenReturn(Optional.empty());

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.createOrder(orderRequestDto, orderRequestDto.getUsername());
		});

		assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
		verify(storeRepository, times(1)).findById(orderRequestDto.getStoreId());
		verify(userRepository, times(1)).findByUsername(orderRequestDto.getUsername());
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void getOrderById_success() {
		// Given
		UUID orderId = UUID.randomUUID();
		Order order = Order.builder()
			.orderId(orderId)
			.store(store)
			.user(user)
			.type(OrderType.ONLINE)
			.totalPrice(10000)
			.address("Test Address")
			.request("Please be quick")
			.orderStatus(OrderStatus.READY)
			.createdAt(LocalDateTime.now())
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		// When
		OrderResponseDto response = orderService.getOrderById(orderId, user.getUsername());

		// Then
		assertNotNull(response);
		assertEquals(orderId, response.getOrderId());
		assertEquals(user.getUsername(), response.getUsername());

		verify(orderRepository, times(1)).findById(orderId);
	}

	@Test
	void getOrderById_orderNotFound_throwsException() {
		// Given
		UUID orderId = UUID.randomUUID();
		when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.getOrderById(orderId, user.getUsername());
		});

		assertEquals("주문을 찾을 수 없습니다.", exception.getMessage());
		verify(orderRepository, times(1)).findById(orderId);
	}

	@Test
	void updateOrderStatus_success() {
		// Given
		UUID orderId = UUID.randomUUID();
		Order order = Order.builder()
			.orderId(orderId)
			.store(store)
			.user(user)
			.type(OrderType.ONLINE)
			.totalPrice(10000)
			.address("Test Address")
			.request("Please be quick")
			.orderStatus(OrderStatus.READY)
			.createdAt(LocalDateTime.now())
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		// When
		OrderResponseDto response = orderService.updateOrderStatus(orderId, OrderStatus.ACCEPTED.name(), user.getUsername());

		// Then
		assertNotNull(response);
		assertEquals(OrderStatus.ACCEPTED.name(), response.getStatus());
		verify(orderRepository, times(1)).findById(orderId);
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	void updateOrderStatus_invalidAccess_throwsException() {
		// Given
		UUID orderId = UUID.randomUUID();
		Order order = Order.builder()
			.orderId(orderId)
			.store(store)
			.user(user)
			.type(OrderType.ONLINE)
			.totalPrice(10000)
			.address("Test Address")
			.request("Please be quick")
			.orderStatus(OrderStatus.READY)
			.createdAt(LocalDateTime.now())
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.updateOrderStatus(orderId, OrderStatus.ACCEPTED.name(), "invalidUser");
		});

		assertEquals("사장님만 접근 가능합니다.", exception.getMessage());
		verify(orderRepository, times(1)).findById(orderId);
		verify(orderRepository, never()).save(any(Order.class));
	}

	// 추가적인 테스트 케이스를 여기서 작성할 수 있습니다.
}
