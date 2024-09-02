package com.mococo.delivery.application.dto.payment;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mococo.delivery.domain.model.enumeration.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

	private UUID paymentId;
	private UUID orderId;
	private Double amount;
	private String paymentMethod;
	private PaymentStatus status;
	private LocalDateTime paidAt;
}