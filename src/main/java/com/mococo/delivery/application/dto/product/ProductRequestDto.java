package com.mococo.delivery.application.dto.product;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
	private UUID storeId;
	private String name;
	private Integer price;
	private String description;
	private Boolean isPublic;
}
