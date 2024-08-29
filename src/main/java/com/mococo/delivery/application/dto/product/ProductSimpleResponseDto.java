package com.mococo.delivery.application.dto.product;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSimpleResponseDto {
	private UUID productId;
	private String name;
	private Double price;
	private String description;
}
