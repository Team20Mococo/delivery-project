package com.mococo.delivery.application.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateDto {
	private String status;  // "READY", "ACCEPT", "CANCEL", "COMPLETE"
}
