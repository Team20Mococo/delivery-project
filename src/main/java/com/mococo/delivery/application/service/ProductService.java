package com.mococo.delivery.application.service;

import org.springframework.stereotype.Service;

import com.mococo.delivery.application.dto.product.ProductRequestDto;
import com.mococo.delivery.application.dto.product.ProductResponseDto;
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
}
