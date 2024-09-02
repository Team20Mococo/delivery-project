package com.mococo.delivery.application.service;

import com.mococo.delivery.application.dto.payment.PaymentRequestDto;
import com.mococo.delivery.application.dto.payment.PaymentResponseDto;
import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.Payment;
import com.mococo.delivery.domain.model.enumeration.PaymentStatus;
import com.mococo.delivery.domain.repository.OrderRepository;
import com.mococo.delivery.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {
		Order order = orderRepository.findById(requestDto.getOrderId())
			.orElseThrow(() -> new RuntimeException("Order not found"));

		Payment payment = Payment.builder()
			.order(order)
			.amount(requestDto.getAmount())
			.paymentMethod(requestDto.getPaymentMethod())
			.status(PaymentStatus.PENDING)
			.paidAt(LocalDateTime.now())
			.build();

		Payment savedPayment = paymentRepository.save(payment);

		return convertToDto(savedPayment);
	}

	@Transactional(readOnly = true)
	public PaymentResponseDto getPaymentById(UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new RuntimeException("Payment not found"));

		return convertToDto(payment);
	}

	@Transactional
	public PaymentResponseDto updatePaymentStatus(UUID paymentId, String status) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new RuntimeException("Payment not found"));

		try {
			PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
			payment.setStatus(paymentStatus);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid payment status value");
		}

		Payment updatedPayment = paymentRepository.save(payment);
		return convertToDto(updatedPayment);
	}

	@Transactional
	public void deletePayment(UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new RuntimeException("Payment not found"));

		paymentRepository.delete(payment);
	}

	private PaymentResponseDto convertToDto(Payment payment) {
		return PaymentResponseDto.builder()
			.paymentId(payment.getPaymentId())
			.orderId(payment.getOrder().getOrderId())
			.amount(payment.getAmount())
			.paymentMethod(payment.getPaymentMethod())
			.status(payment.getStatus())
			.paidAt(payment.getPaidAt())
			.build();
	}
}
