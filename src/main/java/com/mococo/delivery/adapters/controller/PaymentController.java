package com.mococo.delivery.adapters.controller;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.payment.PaymentRequestDto;
import com.mococo.delivery.application.dto.payment.PaymentResponseDto;
import com.mococo.delivery.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/payments")
	public SuccessResponseDto<PaymentResponseDto> createPayment(@Valid @RequestBody PaymentRequestDto request) {
		PaymentResponseDto response = paymentService.createPayment(request);
		return new SuccessResponseDto<>("Payment successfully created", response);
	}

	@GetMapping("/payments/{paymentId}")
	public SuccessResponseDto<PaymentResponseDto> getPaymentById(@PathVariable UUID paymentId) {
		PaymentResponseDto response = paymentService.getPaymentById(paymentId);
		return new SuccessResponseDto<>("Payment retrieved successfully", response);
	}

	@PutMapping("/payments/{paymentId}/status")
	public SuccessResponseDto<PaymentResponseDto> updatePaymentStatus(@PathVariable UUID paymentId, @RequestParam String status) {
		PaymentResponseDto response = paymentService.updatePaymentStatus(paymentId, status);
		return new SuccessResponseDto<>("Payment status updated successfully", response);
	}

	@DeleteMapping("/payments/{paymentId}")
	public SuccessResponseDto<Void> deletePayment(@PathVariable UUID paymentId) {
		paymentService.deletePayment(paymentId);
		return new SuccessResponseDto<>("Payment successfully deleted", null);
	}
}
