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

import com.mococo.delivery.application.dto.product.ProductListResponseDto;
import com.mococo.delivery.application.dto.product.ProductRequestDto;
import com.mococo.delivery.application.dto.product.ProductResponseDto;
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
			.name("Product 1")
			.price(100)
			.description("Description 1")
			.stock(10)
			.isPublic(true)
			.build();

		product2 = Product.builder()
			.id(UUID.randomUUID())
			.name("Product 2")
			.price(200)
			.description("Description 2")
			.stock(5)
			.isPublic(false)
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

		ProductResponseDto firstProduct = response.getProductList().get(0);
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

		ProductResponseDto firstProduct = response.getProductList().get(0);
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

		ProductResponseDto firstProduct = response.getProductList().get(0);
		assertEquals(product1.getId(), firstProduct.getProductId());
		assertEquals(product1.getName(), firstProduct.getName());
		assertEquals(product1.getPrice(), firstProduct.getPrice());
		assertEquals(product1.getDescription(), firstProduct.getDescription());

		verify(productRepository, times(1)).findByIsPublicTrue(pageable);
	}
}
