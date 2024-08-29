package com.mococo.delivery.application.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mococo.delivery.application.dto.PageInfoDto;
import com.mococo.delivery.application.dto.product.ProductListResponseDto;
import com.mococo.delivery.application.dto.product.ProductRequestDto;
import com.mococo.delivery.application.dto.product.ProductResponseDto;
import com.mococo.delivery.application.dto.product.ProductSimpleResponseDto;
import com.mococo.delivery.application.dto.product.ProductUpdateRequestDto;
import com.mococo.delivery.domain.model.Product;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.repository.ProductRepository;
import com.mococo.delivery.domain.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;

	public ProductResponseDto addProduct(ProductRequestDto request) {
		Store store = storeRepository.findById(request.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 ID입니다."));

		Product product = Product.builder()
			.name(request.getName())
			.price(request.getPrice())
			.description(request.getDescription())
			.stock(0)
			.isPublic(request.getIsPublic())
			.store(store)
			.build();

		Product savedProduct = productRepository.save(product);

		return ProductResponseDto.builder()
			.productId(savedProduct.getId())
			.name(savedProduct.getName())
			.price(savedProduct.getPrice())
			.description(savedProduct.getDescription())
			.stock(savedProduct.getStock())
			.isPublic(savedProduct.getIsPublic())
			.createdAt(savedProduct.getCreatedAt())
			.createdBy(savedProduct.getCreatedBy())
			.build();
	}

	@Transactional(readOnly = true)
	public ProductListResponseDto getProductList(String sortBy, String direction, boolean filter,
		int page, int size, String searchQuery) {

		Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
			sortBy != null ? sortBy : "createdAt");

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Product> productPage;

		if (searchQuery != null && !searchQuery.isEmpty()) {
			if (filter) {
				productPage = productRepository.findByNameContainingIgnoreCaseAndIsPublicTrue(searchQuery, pageable);
			} else {
				productPage = productRepository.findByNameContainingIgnoreCase(searchQuery, pageable);
			}
		} else {
			if (filter) {
				productPage = productRepository.findByIsPublicTrue(pageable);
			} else {
				productPage = productRepository.findAll(pageable);
			}
		}

		List<ProductSimpleResponseDto> productList = productPage.getContent().stream()
			.map(product -> ProductSimpleResponseDto.builder()
				.productId(product.getId())
				.name(product.getName())
				.price(product.getPrice())
				.description(product.getDescription())
				.stock(product.getStock())
				.build())
			.collect(Collectors.toList());

		PageInfoDto pageInfo = PageInfoDto.builder()
			.totalItems(productPage.getTotalElements())
			.totalPages(productPage.getTotalPages())
			.currentPage(productPage.getNumber())
			.pageSize(productPage.getSize())
			.hasNextPage(productPage.hasNext())
			.build();

		return ProductListResponseDto.builder()
			.productList(productList)
			.pageInfo(pageInfo)
			.build();
	}

	@Transactional(readOnly = true)
	public ProductSimpleResponseDto getProductById(UUID productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품 ID입니다."));

		return ProductSimpleResponseDto.builder()
			.productId(product.getId())
			.name(product.getName())
			.description(product.getDescription())
			.stock(product.getStock())
			.build();
	}

	@Transactional
	public ProductResponseDto updateProduct(UUID productId, ProductUpdateRequestDto request) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품 ID입니다."));

		Product updatedProduct = product.toBuilder()
			.name(request.getName() != null ? request.getName() : product.getName())
			.price(request.getPrice() != null ? request.getPrice() : product.getPrice())
			.description(request.getDescription() != null ? request.getDescription() : product.getDescription())
			.createdAt(product.getCreatedAt())
			.createdBy(product.getCreatedBy())
			.build();

		updatedProduct = productRepository.save(updatedProduct);

		return ProductResponseDto.builder()
			.storeId(updatedProduct.getStore().getId())
			.productId(updatedProduct.getId())
			.name(updatedProduct.getName())
			.price(updatedProduct.getPrice())
			.description(updatedProduct.getDescription())
			.stock(updatedProduct.getStock())
			.isPublic(updatedProduct.getIsPublic())
			.createdAt(updatedProduct.getCreatedAt())
			.createdBy(updatedProduct.getCreatedBy())
			.build();
	}
}
