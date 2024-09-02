package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*; // JUnit의 단언 메서드를 사용하기 위해 import
import static org.mockito.Mockito.*; // Mockito의 메서드를 사용하기 위해 import

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.mococo.delivery.application.dto.order.OrderRequestDto; // OrderRequestDto import
import com.mococo.delivery.application.dto.order.OrderResponseDto; // OrderResponseDto import
import com.mococo.delivery.application.service.OrderService; // OrderService import
import com.mococo.delivery.domain.model.Order; // Order import
import com.mococo.delivery.domain.model.Store; // Store import
import com.mococo.delivery.domain.model.User; // User import
import com.mococo.delivery.domain.model.enumeration.OrderStatus; // OrderStatus enum import
import com.mococo.delivery.domain.model.enumeration.OrderType; // OrderType enum import
import com.mococo.delivery.domain.repository.OrderRepository; // OrderRepository import
import com.mococo.delivery.domain.repository.StoreRepository; // StoreRepository import
import com.mococo.delivery.domain.repository.UserRepository; // UserRepository import

import org.junit.jupiter.api.BeforeEach; // JUnit의 BeforeEach 어노테이션 사용
import org.junit.jupiter.api.Test; // JUnit의 Test 어노테이션 사용
import org.mockito.InjectMocks; // Mockito의 InjectMocks 어노테이션 사용
import org.mockito.Mock; // Mockito의 Mock 어노테이션 사용
import org.mockito.MockitoAnnotations; // Mockito의 MockitoAnnotations 사용

public class OrderServiceTest {

	// OrderRepository, StoreRepository, UserRepository를 Mock으로 생성
	@Mock
	private OrderRepository orderRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private UserRepository userRepository;

	// Mock 객체들을 주입받아 OrderService를 테스트
	@InjectMocks
	private OrderService orderService;

	private OrderRequestDto orderRequestDto;
	private User user;
	private Store store;

	// 각 테스트 실행 전 실행되는 초기화 메서드
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Mock 객체 초기화

		// 테스트용 User 객체 생성
		user = User.builder()
			.username("testuser") // 사용자 이름 설정
			.build();

		// 테스트용 Store 객체 생성 및 사용자와 연결
		store = Store.builder()
			.id(UUID.randomUUID()) // 임의의 UUID 생성
			.name("Test Store") // 가게 이름 설정
			.owner(user)  // 가게 주인을 위에서 생성한 사용자로 설정
			.build();

		// 테스트용 OrderRequestDto 객체 생성
		orderRequestDto = OrderRequestDto.builder()
			.storeId(store.getId()) // 가게 ID 설정
			.username(user.getUsername()) // 사용자 이름 설정
			.type(OrderType.ONLINE.name()) // 주문 타입 설정
			.totalPrice(10000) // 총 가격 설정
			.address("Test Address") // 주소 설정
			.request("Please be quick") // 요청 사항 설정
			.build();
	}

	// 주문 생성 성공 케이스 테스트
	@Test
	void addOrder_success() {
		// Given: Mock 객체의 동작 설정
		when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.of(store));
		when(userRepository.findByUsername(orderRequestDto.getUsername())).thenReturn(Optional.of(user));

		// 저장할 Order 객체 설정
		Order order = Order.builder()
			.orderId(UUID.randomUUID()) // 임의의 UUID 생성
			.store(store) // 가게 설정
			.user(user) // 사용자 설정
			.type(OrderType.ONLINE) // 주문 타입 설정
			.totalPrice(orderRequestDto.getTotalPrice()) // 총 가격 설정
			.address(orderRequestDto.getAddress()) // 주소 설정
			.request(orderRequestDto.getRequest()) // 요청 사항 설정
			.orderStatus(OrderStatus.READY) // 주문 상태 설정
			.createdAt(LocalDateTime.now()) // 생성 시간 설정
			.build();

		when(orderRepository.save(any(Order.class))).thenReturn(order);

		// When: 주문 생성 서비스 호출
		OrderResponseDto response = orderService.createOrder(orderRequestDto, orderRequestDto.getUsername());

		// Then: 생성된 주문의 정보를 검증
		assertNotNull(response); // 응답이 null이 아님을 확인
		assertEquals(orderRequestDto.getUsername(), response.getUsername()); // 요청한 사용자 이름과 응답된 사용자 이름이 일치하는지 확인
		assertEquals(orderRequestDto.getStoreId(), response.getStoreId()); // 요청한 가게 ID와 응답된 가게 ID가 일치하는지 확인
		assertEquals(OrderStatus.READY.name(), response.getStatus()); // 주문 상태가 READY인지 확인

		// Mock 객체의 메서드 호출이 예상대로 이루어졌는지 검증
		verify(storeRepository, times(1)).findById(orderRequestDto.getStoreId());
		verify(userRepository, times(1)).findByUsername(orderRequestDto.getUsername());
		verify(orderRepository, times(1)).save(any(Order.class));
	}

	// 가게를 찾지 못한 경우의 예외 처리 테스트
	@Test
	void addOrder_storeNotFound_throwsException() {
		// Given: 가게를 찾지 못한 경우 설정
		when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.empty());

		// When & Then: 주문 생성 시 예외 발생 여부 테스트
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.createOrder(orderRequestDto, orderRequestDto.getUsername());
		});

		assertEquals("가게를 찾을 수 없습니다!.", exception.getMessage()); // 예외 메시지가 올바른지 확인
		verify(storeRepository, times(1)).findById(orderRequestDto.getStoreId()); // 가게 조회 메서드가 한 번 호출되었는지 확인
		verify(userRepository, never()).findByUsername(anyString()); // 사용자 조회 메서드가 호출되지 않았음을 확인
		verify(orderRepository, never()).save(any(Order.class)); // 주문 저장 메서드가 호출되지 않았음을 확인
	}

	// 사용자를 찾지 못한 경우의 예외 처리 테스트
	@Test
	void addOrder_userNotFound_throwsException() {
		// Given: 가게는 찾았지만 사용자를 찾지 못한 경우 설정
		when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.of(store));
		when(userRepository.findByUsername(orderRequestDto.getUsername())).thenReturn(Optional.empty());

		// When & Then: 주문 생성 시 예외 발생 여부 테스트
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.createOrder(orderRequestDto, orderRequestDto.getUsername());
		});

		assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage()); // 예외 메시지가 올바른지 확인
		verify(storeRepository, times(1)).findById(orderRequestDto.getStoreId()); // 가게 조회 메서드가 한 번 호출되었는지 확인
		verify(userRepository, times(1)).findByUsername(orderRequestDto.getUsername()); // 사용자 조회 메서드가 한 번 호출되었는지 확인
		verify(orderRepository, never()).save(any(Order.class)); // 주문 저장 메서드가 호출되지 않았음을 확인
	}

	// 주문 조회 성공 테스트
	@Test
	void getOrderById_success() {
		// Given: 주문 조회 성공 설정
		UUID orderId = UUID.randomUUID();
		Order order = Order.builder()
			.orderId(orderId) // 임의의 주문 ID 설정
			.store(store) // 가게 설정
			.user(user) // 사용자 설정
			.type(OrderType.ONLINE) // 주문 타입 설정
			.totalPrice(10000) // 총 가격 설정
			.address("Test Address") // 주소 설정
			.request("Please be quick") // 요청 사항 설정
			.orderStatus(OrderStatus.READY) // 주문 상태 설정
			.createdAt(LocalDateTime.now()) // 생성 시간 설정
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		// When: 주문 조회 서비스 호출
		OrderResponseDto response = orderService.getOrderById(orderId, user.getUsername());

		// Then: 조회된 주문 정보 검증
		assertNotNull(response); // 응답이 null이 아님을 확인
		assertEquals(orderId, response.getOrderId()); // 요청한 주문 ID와 응답된 주문 ID가 일치하는지 확인
		assertEquals(user.getUsername(), response.getUsername()); // 요청한 사용자 이름과 응답된 사용자 이름이 일치하는지 확인

		// 주문 조회 메서드가 한 번 호출되었는지 확인
		verify(orderRepository, times(1)).findById(orderId);
	}

	// 주문을 찾지 못한 경우의 예외 처리 테스트
	@Test
	void getOrderById_orderNotFound_throwsException() {
		// Given: 주문을 찾지 못한 경우 설정
		UUID orderId = UUID.randomUUID();
		when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

		// When & Then: 주문 조회 시 예외 발생 여부 테스트
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.getOrderById(orderId, user.getUsername());
		});

		assertEquals("주문을 찾을 수 없습니다.", exception.getMessage()); // 예외 메시지가 올바른지 확인
		verify(orderRepository, times(1)).findById(orderId); // 주문 조회 메서드가 한 번 호출되었는지 확인
	}

	// 주문 상태 업데이트 성공 테스트
	@Test
	void updateOrderStatus_success() {
		// Given: 주문 상태 업데이트 성공 설정
		UUID orderId = UUID.randomUUID();
		Order order = Order.builder()
			.orderId(orderId) // 임의의 주문 ID 설정
			.store(store) // 가게 설정
			.user(user) // 사용자 설정
			.type(OrderType.ONLINE) // 주문 타입 설정
			.totalPrice(10000) // 총 가격 설정
			.address("Test Address") // 주소 설정
			.request("Please be quick") // 요청 사항 설정
			.orderStatus(OrderStatus.READY) // 주문 상태 설정
			.createdAt(LocalDateTime.now()) // 생성 시간 설정
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		// When: 주문 상태 업데이트 서비스 호출
		OrderResponseDto response = orderService.updateOrderStatus(orderId, OrderStatus.ACCEPTED.name(), user.getUsername());

		// Then: 업데이트된 주문 상태 정보 검증
		assertNotNull(response); // 응답이 null이 아님을 확인
		assertEquals(OrderStatus.ACCEPTED.name(), response.getStatus()); // 업데이트된 주문 상태가 ACCEPTED인지 확인
		verify(orderRepository, times(1)).findById(orderId); // 주문 조회 메서드가 한 번 호출되었는지 확인
		verify(orderRepository, times(1)).save(order); // 주문 저장 메서드가 한 번 호출되었는지 확인
	}

	// 잘못된 접근으로 인한 예외 처리 테스트
	@Test
	void updateOrderStatus_invalidAccess_throwsException() {
		// Given: 잘못된 사용자로 인한 예외 발생 설정
		UUID orderId = UUID.randomUUID();
		Order order = Order.builder()
			.orderId(orderId) // 임의의 주문 ID 설정
			.store(store) // 가게 설정
			.user(user) // 사용자 설정
			.type(OrderType.ONLINE) // 주문 타입 설정
			.totalPrice(10000) // 총 가격 설정
			.address("Test Address") // 주소 설정
			.request("Please be quick") // 요청 사항 설정
			.orderStatus(OrderStatus.READY) // 주문 상태 설정
			.createdAt(LocalDateTime.now()) // 생성 시간 설정
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		// When & Then: 잘못된 사용자가 주문 상태를 업데이트할 때 예외 발생 여부 테스트
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.updateOrderStatus(orderId, OrderStatus.ACCEPTED.name(), "invalidUser");
		});

		assertEquals("사장님만 접근 가능합니다.", exception.getMessage()); // 예외 메시지가 올바른지 확인
		verify(orderRepository, times(1)).findById(orderId); // 주문 조회 메서드가 한 번 호출되었는지 확인
		verify(orderRepository, never()).save(any(Order.class)); // 주문 저장 메서드가 호출되지 않았음을 확인
	}

	// 추가적인 테스트 케이스를 여기서 작성할 수 있습니다.
}
