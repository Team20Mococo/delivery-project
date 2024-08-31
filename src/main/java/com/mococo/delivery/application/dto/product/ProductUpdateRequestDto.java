package com.mococo.delivery.application.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductUpdateRequestDto {
	private String name;
	private Integer price;
	private String description;
}
