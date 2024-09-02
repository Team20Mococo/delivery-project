package com.mococo.delivery.application.dto.delivery;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mococo.delivery.domain.model.enumeration.DeliveryStatus;

import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRequestDto {

	@NotNull(message = "주문 ID는 필수입니다.")
	private UUID orderId;

	@NotEmpty(message = "배달 주소는 필수입니다.")
	private String address;  // 필드명을 address로 변경

	@NotNull(message = "배달 상태는 필수입니다.")
	private DeliveryStatus status;

	private LocalDateTime shippedAt;
	private LocalDateTime deliveredAt;
}
