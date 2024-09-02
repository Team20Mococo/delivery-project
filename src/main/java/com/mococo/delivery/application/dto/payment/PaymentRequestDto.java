package com.mococo.delivery.application.dto.payment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

	private UUID orderId;
	private Double amount;
	private String paymentMethod;
}