package com.mococo.delivery;

import com.mococo.delivery.application.dto.payment.PaymentRequestDto;
import com.mococo.delivery.application.dto.payment.PaymentResponseDto;
import com.mococo.delivery.application.service.PaymentService;
import com.mococo.delivery.domain.model.Order;
import com.mococo.delivery.domain.model.Payment;
import com.mococo.delivery.domain.model.enumeration.PaymentStatus;
import com.mococo.delivery.domain.repository.OrderRepository;
import com.mococo.delivery.domain.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private PaymentService paymentService;

	private Order order;
	private Payment payment;
	private PaymentRequestDto paymentRequestDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		order = Order.builder()
			.orderId(UUID.randomUUID())
			.build();

		paymentRequestDto = PaymentRequestDto.builder()
			.orderId(order.getOrderId())
			.amount(100.0)
			.paymentMethod("CREDIT_CARD")
			.build();

		payment = Payment.builder()
			.paymentId(UUID.randomUUID())
			.order(order)
			.amount(100.0)
			.paymentMethod("CREDIT_CARD")
			.status(PaymentStatus.PENDING)
			.build();
	}

	@Test
	void createPayment_success() {
		when(orderRepository.findById(paymentRequestDto.getOrderId())).thenReturn(Optional.of(order));
		when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

		PaymentResponseDto response = paymentService.createPayment(paymentRequestDto);

		assertNotNull(response);
		assertEquals(payment.getPaymentId(), response.getPaymentId());
		assertEquals(payment.getOrder().getOrderId(), response.getOrderId());
		assertEquals(payment.getAmount(), response.getAmount());
		assertEquals(payment.getPaymentMethod(), response.getPaymentMethod());

		verify(orderRepository, times(1)).findById(paymentRequestDto.getOrderId());
		verify(paymentRepository, times(1)).save(any(Payment.class));
	}

	@Test
	void getPaymentById_success() {
		UUID paymentId = payment.getPaymentId();
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

		PaymentResponseDto response = paymentService.getPaymentById(paymentId);

		assertNotNull(response);
		assertEquals(payment.getPaymentId(), response.getPaymentId());
		assertEquals(payment.getOrder().getOrderId(), response.getOrderId());

		verify(paymentRepository, times(1)).findById(paymentId);
	}

	@Test
	void updatePaymentStatus_success() {
		UUID paymentId = payment.getPaymentId();
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
		when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

		PaymentResponseDto response = paymentService.updatePaymentStatus(paymentId, "SUCCESS");

		assertNotNull(response);
		assertEquals(PaymentStatus.SUCCESS, response.getStatus());

		verify(paymentRepository, times(1)).findById(paymentId);
		verify(paymentRepository, times(1)).save(any(Payment.class));
	}

	@Test
	void deletePayment_success() {
		UUID paymentId = payment.getPaymentId();
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

		paymentService.deletePayment(paymentId);

		verify(paymentRepository, times(1)).findById(paymentId);
		verify(paymentRepository, times(1)).delete(payment);
	}
}
