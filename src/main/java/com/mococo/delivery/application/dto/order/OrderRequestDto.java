package com.mococo.delivery.application.dto.order;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderRequestDto {
	private UUID storeId;
	private String username;
	private String type;
	private Integer totalPrice;
	private String address;
	private String request;
}
