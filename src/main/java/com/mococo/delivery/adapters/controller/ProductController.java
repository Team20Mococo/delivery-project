package com.mococo.delivery.adapters.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.product.ProductListResponseDto;
import com.mococo.delivery.application.dto.product.ProductRequestDto;
import com.mococo.delivery.application.dto.product.ProductResponseDto;
import com.mococo.delivery.application.dto.product.ProductSimpleResponseDto;
import com.mococo.delivery.application.dto.product.ProductUpdateRequestDto;
import com.mococo.delivery.application.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	public SuccessResponseDto<ProductResponseDto> addProduct(@RequestBody ProductRequestDto request) {
		ProductResponseDto response = productService.addProduct(request);
		return new SuccessResponseDto<>("성공했습니다.", response);
	}

	@GetMapping("/owner/products")
	public SuccessResponseDto<ProductListResponseDto> getProductListByOwner(
		@RequestParam(required = false) String sortBy,
		@RequestParam(defaultValue = "asc") String direction,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String searchQuery
	) {
		ProductListResponseDto response =
			productService.getProductList(sortBy, direction, Boolean.FALSE, page, size, searchQuery);
		return new SuccessResponseDto<>("성공했습니다.", response);
	}

	@GetMapping("/customer/products")
	public SuccessResponseDto<ProductListResponseDto> getProductListByCustomer(
		@RequestParam(required = false) String sortBy,
		@RequestParam(defaultValue = "asc") String direction,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String searchQuery
	) {
		ProductListResponseDto response =
			productService.getProductList(sortBy, direction, Boolean.TRUE, page, size, searchQuery);
		return new SuccessResponseDto<>("성공했습니다.", response);
	}

	@GetMapping("/products/{productId}")
	public SuccessResponseDto<ProductSimpleResponseDto> getOneProduct(@PathVariable UUID productId) {
		ProductSimpleResponseDto response = productService.getProductById(productId);
		return new SuccessResponseDto<>("성공했습니다.", response);
	}

	@PutMapping("/owner/products/{productId}")
	public SuccessResponseDto<ProductResponseDto> updateProduct(
		@PathVariable UUID productId,
		@RequestBody ProductUpdateRequestDto request) {
		ProductResponseDto response = productService.updateProduct(productId, request);
		return new SuccessResponseDto<>("성공했습니다.", response);
	}

}
