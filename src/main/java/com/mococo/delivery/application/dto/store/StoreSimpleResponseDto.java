package com.mococo.delivery.application.dto.store;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreSimpleResponseDto {
	private UUID storeId;
	private String name;
	private String category;
	private String description;
}
