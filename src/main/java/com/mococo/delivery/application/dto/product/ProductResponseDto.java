package com.mococo.delivery.application.dto.product;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponseDto {
	private UUID productId;
	private UUID storeId;
	private String name;
	private Integer price;
	private String description;
	private Integer stock;
	private Boolean isPublic;
	private LocalDateTime createdAt;
	private String createdBy;
}
