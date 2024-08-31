package com.mococo.delivery.application.dto.order;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderResponseDto {
	private UUID orderId;
	private String username;
	private UUID storeId;
	private String storeName;
	private String type;
	private Integer totalPrice;
	private String address;
	private String request;
	private String status;
	private String createdAt;
}
