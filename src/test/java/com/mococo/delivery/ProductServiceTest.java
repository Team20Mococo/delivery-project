package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mococo.delivery.application.dto.product.*;
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

import com.mococo.delivery.application.service.ProductService;
import com.mococo.delivery.domain.model.Product;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.repository.ProductRepository;
import com.mococo.delivery.domain.repository.StoreRepository;

public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private ProductService productService;

	private ProductRequestDto productRequestDto;
	private Store store;
	private Product product1;
	private Product product2;
	private ProductUpdateRequestDto updateRequest;
	private UpdateStockRequestDto stockRequest;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		store = Store.builder()
			.id(UUID.randomUUID())
			.name("Test Store")
			.build();

		productRequestDto = ProductRequestDto.builder()
			.name("Test Product")
			.price(100)
			.description("Test Description")
			.isPublic(true)
			.storeId(store.getId())
			.build();

		product1 = Product.builder()
				.id(UUID.randomUUID())
				.name("Test Product")
				.price(100)
				.description("Test Description")
				.stock(10)
				.isPublic(true)
				.store(store)  // store 필드 초기화
				.build();

		product2 = Product.builder()
				.id(UUID.randomUUID())
				.name("Test Product2")
				.price(200)
				.description("Test Description2")
				.stock(20)
				.isPublic(true)
				.store(store)  // store 필드 초기화
				.build();


		updateRequest = ProductUpdateRequestDto.builder()
			.name("Updated Product")
			.price(150)
			.description("Updated Description")
			.build();

		stockRequest = UpdateStockRequestDto.builder()
				.stock(20)
				.build();
	}

	@Test
	void addProduct_success() {
		// Given
		when(storeRepository.findById(productRequestDto.getStoreId())).thenReturn(Optional.of(store));

		Product savedProduct = Product.builder()
			.id(UUID.randomUUID())
			.name(productRequestDto.getName())
			.price(productRequestDto.getPrice())
			.description(productRequestDto.getDescription())
			.stock(0)
			.isPublic(productRequestDto.getIsPublic())
			.store(store)
			.build();

		when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

		// When
		ProductResponseDto response = productService.addProduct(productRequestDto);

		// Then
		assertNotNull(response);
		assertEquals(savedProduct.getId(), response.getProductId());
		assertEquals(savedProduct.getName(), response.getName());
		assertEquals(savedProduct.getPrice(), response.getPrice());
		assertEquals(savedProduct.getDescription(), response.getDescription());
		assertEquals(savedProduct.getStock(), response.getStock());
		assertEquals(savedProduct.getIsPublic(), response.getIsPublic());

		verify(storeRepository, times(1)).findById(productRequestDto.getStoreId());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	void addProduct_storeNotFound_throwsException() {
		// Given
		when(storeRepository.findById(productRequestDto.getStoreId())).thenReturn(Optional.empty());

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			productService.addProduct(productRequestDto);
		});

		assertEquals("유효하지 않은 가게 ID입니다.", exception.getMessage());

		verify(storeRepository, times(1)).findById(productRequestDto.getStoreId());
		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	void getProductList_success() {
		// Given
		List<Product> products = Arrays.asList(product1, product2);
		Page<Product> productPage = new PageImpl<>(products);
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());

		when(productRepository.findAll(pageable)).thenReturn(productPage);

		// When
		ProductListResponseDto response =
			productService.getProductList(null, "asc", false, 0, 10, null);

		// Then
		assertNotNull(response);
		assertEquals(2, response.getProductList().size());

		ProductSimpleResponseDto firstProduct = response.getProductList().get(0);
		assertEquals(product1.getId(), firstProduct.getProductId());
		assertEquals(product1.getName(), firstProduct.getName());
		assertEquals(product1.getPrice(), firstProduct.getPrice());
		assertEquals(product1.getDescription(), firstProduct.getDescription());

		verify(productRepository, times(1)).findAll(pageable);
	}

	@Test
	void getProductList_withSearchQuery_success() {
		// Given
		List<Product> products = Arrays.asList(product1);
		Page<Product> productPage = new PageImpl<>(products);
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());

		when(productRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(
			productPage);

		// When
		ProductListResponseDto response =
			productService.getProductList(null, "asc", false, 0, 10, "Product 1");

		// Then
		assertNotNull(response);
		assertEquals(1, response.getProductList().size());

		ProductSimpleResponseDto firstProduct = response.getProductList().get(0);
		assertEquals(product1.getId(), firstProduct.getProductId());
		assertEquals(product1.getName(), firstProduct.getName());
		assertEquals(product1.getPrice(), firstProduct.getPrice());
		assertEquals(product1.getDescription(), firstProduct.getDescription());

		verify(productRepository, times(1)).findByNameContainingIgnoreCase("Product 1", pageable);
	}

	@Test
	void getProductList_withFilter_success() {
		// Given
		List<Product> products = Arrays.asList(product1);
		Page<Product> productPage = new PageImpl<>(products);
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());

		when(productRepository.findByIsPublicTrue(any(Pageable.class))).thenReturn(productPage);

		// When
		ProductListResponseDto response =
			productService.getProductList(null, "asc", true, 0, 10, null);

		// Then
		assertNotNull(response);
		assertEquals(1, response.getProductList().size());

		ProductSimpleResponseDto firstProduct = response.getProductList().get(0);
		assertEquals(product1.getId(), firstProduct.getProductId());
		assertEquals(product1.getName(), firstProduct.getName());
		assertEquals(product1.getPrice(), firstProduct.getPrice());
		assertEquals(product1.getDescription(), firstProduct.getDescription());

		verify(productRepository, times(1)).findByIsPublicTrue(pageable);
	}

	@Test
	void getProductById_success() {
		// Given
		UUID productId = UUID.randomUUID();
		Product product = Product.builder()
			.id(productId)
			.name("Product 1")
			.price(100)
			.description("Description 1")
			.stock(10)
			.isPublic(true)
			.build();

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// When
		ProductSimpleResponseDto response = productService.getProductById(productId);

		// Then
		assertNotNull(response);
		assertEquals(productId, response.getProductId());
		assertEquals("Product 1", response.getName());

		verify(productRepository, times(1)).findById(productId);
	}

	@Test
	void getProductById_productNotFound_throwsException() {
		// Given
		UUID productId = UUID.randomUUID();
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			productService.getProductById(productId);
		});

		assertEquals("유효하지 않은 상품 ID입니다.", exception.getMessage());
		verify(productRepository, times(1)).findById(productId);
	}

	@Test
	void updateProduct_success() {
		// Given
		when(productRepository.findById(product1.getId())).thenReturn(Optional.of(product1));
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			return savedProduct.toBuilder()
				.id(product1.getId())
				.store(savedProduct.getStore())
				.stock(product1.getStock())
				.isPublic(product1.getIsPublic())
				.build();
		});

		// When
		ProductResponseDto response = productService.updateProduct(product1.getId(), updateRequest);

		// Then
		assertNotNull(response);
		assertEquals(product1.getId(), response.getProductId());
		assertEquals(updateRequest.getName(), response.getName());
		assertEquals(updateRequest.getPrice(), response.getPrice());
		assertEquals(updateRequest.getDescription(), response.getDescription());
		assertEquals(product1.getStock(), response.getStock());
		assertEquals(product1.getIsPublic(), response.getIsPublic());

		assertEquals(product1.getCreatedAt(), response.getCreatedAt());

		verify(productRepository, times(1)).findById(product1.getId());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	void updateProduct_invalidId_throwsException() {
		// Given
		UUID invalidId = UUID.randomUUID();
		when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			productService.updateProduct(invalidId, updateRequest);
		});

		assertEquals("유효하지 않은 상품 ID입니다.", exception.getMessage());
		verify(productRepository, times(1)).findById(invalidId);
		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	void updateProductStock_success() {
		// Given
		when(productRepository.findById(product1.getId())).thenReturn(Optional.of(product1));
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			return savedProduct.toBuilder()
					.id(product1.getId()) // 유지해야 할 필드
					.name(product1.getName())
					.price(product1.getPrice())
					.description(product1.getDescription())
					.stock(stockRequest.getStock()) // 변경된 필드
					.isPublic(product1.getIsPublic())
					.store(product1.getStore())
					.build();
		});

		// When
		ProductResponseDto response = productService.updateProductStock(product1.getId(), stockRequest);

		// Then
		assertNotNull(response);
		assertEquals(product1.getId(), response.getProductId());
		assertEquals(stockRequest.getStock(), response.getStock());


		verify(productRepository, times(1)).findById(product1.getId());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	void updateProductStock_invalidId_throwsException() {
		// Given
		UUID invalidId = UUID.randomUUID();
		when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			productService.updateProductStock(invalidId, stockRequest);
		});

		assertEquals("유효하지 않은 상품 ID입니다.", exception.getMessage());
		verify(productRepository, times(1)).findById(invalidId);
		verify(productRepository, never()).save(any(Product.class));
	}
}
