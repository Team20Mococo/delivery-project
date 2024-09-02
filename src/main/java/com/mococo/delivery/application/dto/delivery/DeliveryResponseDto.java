package com.mococo.delivery.application.dto.delivery;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mococo.delivery.domain.model.enumeration.DeliveryStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryResponseDto {
	private UUID deliveryId;
	private UUID orderId;
	private String address;
	private DeliveryStatus status;
	private LocalDateTime shippedAt;
	private LocalDateTime deliveredAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
