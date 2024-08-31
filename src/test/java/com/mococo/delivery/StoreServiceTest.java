package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mococo.delivery.application.dto.store.AddStoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreListResponseDto;
import com.mococo.delivery.application.dto.store.StoreRequestDto;
import com.mococo.delivery.application.dto.store.StoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreSimpleResponseDto;
import com.mococo.delivery.application.service.AuditorAwareImpl;
import com.mococo.delivery.application.service.StoreService;
import com.mococo.delivery.domain.exception.entity.StoreAlreadyDeletedException;
import com.mococo.delivery.domain.exception.entity.StoreNotFoundException;
import com.mococo.delivery.domain.exception.entity.UnauthorizedStoreAccessException;
import com.mococo.delivery.domain.model.Category;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.repository.CategoryRepository;
import com.mococo.delivery.domain.repository.StoreRepository;
import com.mococo.delivery.domain.repository.UserRepository;

public class StoreServiceTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AuditorAwareImpl auditorAware;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private StoreService storeService;

	private StoreRequestDto storeRequestDto;
	private User owner;
	private Category category;
	private Store store1;
	private Store store2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		storeRequestDto = StoreRequestDto.builder()
			.username("testuser")
			.name("Test Store")
			.category("KOREAN")
			.notice("Store Notice")
			.description("Store Description")
			.build();

		owner = User.builder().username(storeRequestDto.getUsername()).build();
		category = Category.builder().name(storeRequestDto.getCategory()).build();

		store1 = Store.builder()
			.id(UUID.randomUUID())
			.name("김밥천국")
			.category(category)
			.owner(owner)
			.description("맛있는 식당이에요")
			.build();

		store2 = Store.builder()
			.id(UUID.randomUUID())
			.name("든킨드나쓰")
			.category(category)
			.owner(owner)
			.description("눈물나도록 맛있는 맛.")
			.build();

		when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("testuser"));
	}

	@Test
	void getStore_existingStore_returnStore() {
		UUID storeId = UUID.randomUUID();
		Store store = Store.builder()
			.id(storeId)
			.name("Test Store")
			.category(category)
			.owner(owner)
			.notice("Store Notice")
			.description("Store Description")
			.operationStatus(true)
			.build();

		when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

		// when
		Store result = storeService.getStore(storeId);

		// then
		assertNotNull(result);
		assertEquals("Test Store", result.getName());
		verify(storeRepository, times(1)).findById(storeId);
	}

	@Test
	void getStore_nonExistingStore_returnsNull() {
		// given
		UUID storeId = UUID.randomUUID();

		when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

		// when
		Store result = storeService.getStore(storeId);

		// then
		assertNull(result);
		verify(storeRepository, times(1)).findById(storeId);
	}

	@Test
	void addStore_success() {
		// given
		when(userRepository.findByUsername(storeRequestDto.getUsername())).thenReturn(Optional.of(owner));
		when(categoryRepository.findByName(storeRequestDto.getCategory())).thenReturn(Optional.of(category));
		when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> {
			Store store = invocation.getArgument(0);
			return Store.builder()
				.id(UUID.randomUUID())
				.name(store.getName())
				.category(store.getCategory())
				.notice(store.getNotice())
				.description(store.getDescription())
				.operationStatus(store.getOperationStatus())
				.owner(store.getOwner())
				.build();
		});

		// when
		AddStoreResponseDto response = storeService.addStore(storeRequestDto);

		// then
		assertNotNull(response);
		assertNotNull(response.getStore().getStoreId());
		assertEquals(storeRequestDto.getName(), response.getStore().getName());
		verify(storeRepository, times(1)).save(any(Store.class));
	}

	@Test
	void addStore_invalidUser_throwsException() {
		// given
		when(userRepository.findByUsername(storeRequestDto.getUsername())).thenReturn(Optional.empty());

		// when / then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			storeService.addStore(storeRequestDto);
		});

		assertEquals("유효하지 않은 사용자 ID입니다.", exception.getMessage());
		verify(storeRepository, never()).save(any(Store.class));
	}

	@Test
	void addStore_invalidCategory_throwsException() {
		// given
		when(userRepository.findByUsername(storeRequestDto.getUsername())).thenReturn(Optional.of(owner));
		when(categoryRepository.findByName(storeRequestDto.getCategory())).thenReturn(Optional.empty());

		// when / then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			storeService.addStore(storeRequestDto);
		});

		assertEquals("유효하지 않은 카테고리입니다.", exception.getMessage());
		verify(storeRepository, never()).save(any(Store.class));
	}

	@Test
	void getAllStores_withDefaultSort_success() {
		// Given
		List<Store> stores = Arrays.asList(store2, store1); // 기본 정렬 (createdAt, updatedAt)
		Page<Store> storePage = new PageImpl<>(stores);

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("createdAt"), Sort.Order.asc("updatedAt")));
		when(storeRepository.findAll(pageable)).thenReturn(storePage);

		// When
		StoreListResponseDto response = storeService.getAllStores(null, "asc", false, 0, 10, null);

		// Then
		assertNotNull(response);
		assertEquals(2, response.getStoreList().size());

		StoreSimpleResponseDto firstStore = response.getStoreList().get(0);
		StoreSimpleResponseDto secondStore = response.getStoreList().get(1);

		assertEquals("든킨드나쓰", firstStore.getName());
		assertEquals("김밥천국", secondStore.getName());

		verify(storeRepository, times(1)).findAll(pageable);
	}

	@Test
	void getAllStores_withCustomSort_success() {
		// Given
		List<Store> stores = Arrays.asList(store1, store2); // 사용자 지정 정렬 (name)
		Page<Store> storePage = new PageImpl<>(stores);

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
		when(storeRepository.findAll(pageable)).thenReturn(storePage);

		// When
		StoreListResponseDto response = storeService.getAllStores("name", "asc", false, 0, 10, null);

		// Then
		assertNotNull(response);
		assertEquals(2, response.getStoreList().size());

		StoreSimpleResponseDto firstStore = response.getStoreList().get(0);
		StoreSimpleResponseDto secondStore = response.getStoreList().get(1);

		assertEquals("김밥천국", firstStore.getName());
		assertEquals("든킨드나쓰", secondStore.getName());

		verify(storeRepository, times(1)).findAll(pageable);
	}

	@Test
	void getAllStores_withFilter_success() {
		// Given
		List<Store> stores = Arrays.asList(store1); // 필터링 적용 (operationStatus = true)
		Page<Store> storePage = new PageImpl<>(stores);

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("createdAt"), Sort.Order.asc("updatedAt")));
		when(storeRepository.findByOperationStatusTrue(pageable)).thenReturn(storePage);

		// When
		StoreListResponseDto response = storeService.getAllStores(null, "asc", true, 0, 10, null);

		// Then
		assertNotNull(response);
		assertEquals(1, response.getStoreList().size());

		StoreSimpleResponseDto firstStore = response.getStoreList().get(0);
		assertEquals("김밥천국", firstStore.getName());

		verify(storeRepository, times(1)).findByOperationStatusTrue(pageable);
		verify(storeRepository, never()).findAll(pageable);
	}

	@Test
	void getAllStores_withSearchQuery_success() {
		// Given
		List<Store> stores = Arrays.asList(store1); // 검색어에 맞는 스토어만 반환
		Page<Store> storePage = new PageImpl<>(stores);

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
		when(storeRepository.findByNameContainingIgnoreCase("김밥", pageable)).thenReturn(storePage);

		// When
		StoreListResponseDto response = storeService.getAllStores("name", "asc", false, 0, 10, "김밥");

		// Then
		assertNotNull(response);
		assertEquals(1, response.getStoreList().size());

		StoreSimpleResponseDto firstStore = response.getStoreList().get(0);
		assertEquals("김밥천국", firstStore.getName());

		verify(storeRepository, times(1)).findByNameContainingIgnoreCase("김밥", pageable);
	}

	@Test
	void getAllStores_withSearchQueryAndFilter_success() {
		// Given
		List<Store> stores = Arrays.asList(store1); // 검색어와 필터에 맞는 스토어만 반환
		Page<Store> storePage = new PageImpl<>(stores);

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
		when(storeRepository.findByNameContainingIgnoreCaseAndOperationStatusTrue("김밥", pageable)).thenReturn(
			storePage);

		// When
		StoreListResponseDto response = storeService.getAllStores("name", "asc", true, 0, 10, "김밥");

		// Then
		assertNotNull(response);
		assertEquals(1, response.getStoreList().size());

		StoreSimpleResponseDto firstStore = response.getStoreList().get(0);
		assertEquals("김밥천국", firstStore.getName());

		verify(storeRepository, times(1)).findByNameContainingIgnoreCaseAndOperationStatusTrue("김밥", pageable);
	}

	@Test
	void deleteStore_success() {
		// Given
		when(storeRepository.findById(store1.getId())).thenReturn(Optional.of(store1));
		when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		StoreResponseDto response = storeService.deleteStore(store1.getId());

		// Then
		assertNotNull(response);
		assertNotNull(response.getDeletedAt());
		assertNotNull(response.getDeletedBy());
		assertEquals(store1.getOwner().getUsername(), response.getDeletedBy());
		verify(storeRepository, times(1)).findById(store1.getId());
		verify(storeRepository, times(1)).save(store1);
	}

	@Test
	void deleteStore_alreadyDeleted_throwsException() {
		// Given
		store1.softDelete("testuser");
		when(storeRepository.findById(store1.getId())).thenReturn(Optional.of(store1));

		// When & Then
		StoreAlreadyDeletedException exception = assertThrows(StoreAlreadyDeletedException.class,
			() -> storeService.deleteStore(store1.getId()));

		assertEquals("이미 삭제된 스토어입니다.", exception.getMessage());
		verify(storeRepository, times(1)).findById(store1.getId());
		verify(storeRepository, never()).save(any(Store.class));
	}

	@Test
	void deleteStore_invalidId_throwsException() {
		// Given
		UUID invalidId = UUID.randomUUID();
		when(storeRepository.findById(invalidId)).thenReturn(Optional.empty());

		// When & Then
		StoreNotFoundException exception = assertThrows(StoreNotFoundException.class, () -> {
			storeService.deleteStore(invalidId);
		});

		assertEquals("유효하지 않은 스토어 ID입니다.", exception.getMessage());
		verify(storeRepository, times(1)).findById(invalidId);
		verify(storeRepository, never()).save(any(Store.class));
	}

	@Test
	void deleteStore_notOwner_throwsException() {
		// Given
		when(storeRepository.findById(store1.getId())).thenReturn(Optional.of(store1));
		when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("anotherUser"));

		// When & Then
		UnauthorizedStoreAccessException exception = assertThrows(UnauthorizedStoreAccessException.class,
			() -> storeService.deleteStore(store1.getId()));

		assertEquals("스토어의 소유자만 삭제할 수 있습니다.", exception.getMessage());
		verify(storeRepository, times(1)).findById(store1.getId());
		verify(storeRepository, never()).save(any(Store.class));
	}
}
