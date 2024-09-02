package com.mococo.delivery.adapters.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.delivery.DeliveryRequestDto;
import com.mococo.delivery.application.dto.delivery.DeliveryResponseDto;
import com.mococo.delivery.application.service.DeliveryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PostMapping("/deliveries")
	public SuccessResponseDto<DeliveryResponseDto> createDelivery(@Valid @RequestBody DeliveryRequestDto request) {
		DeliveryResponseDto response = deliveryService.createDelivery(request);
		return new SuccessResponseDto<>("배달이 성공적으로 생성되었습니다.", response);
	}

	@GetMapping("/deliveries/{deliveryId}")
	public SuccessResponseDto<DeliveryResponseDto> getDeliveryById(@PathVariable UUID deliveryId) {
		DeliveryResponseDto response = deliveryService.getDeliveryById(deliveryId);
		return new SuccessResponseDto<>("배달 조회에 성공했습니다.", response);
	}

	@PutMapping("/deliveries/{deliveryId}")
	public SuccessResponseDto<DeliveryResponseDto> updateDelivery(@PathVariable UUID deliveryId, @Valid @RequestBody DeliveryRequestDto request) {
		DeliveryResponseDto response = deliveryService.updateDelivery(deliveryId, request);
		return new SuccessResponseDto<>("배달 정보가 성공적으로 수정되었습니다.", response);
	}

	@DeleteMapping("/deliveries/{deliveryId}")
	public SuccessResponseDto<Void> deleteDelivery(@PathVariable UUID deliveryId) {
		deliveryService.deleteDelivery(deliveryId);
		return new SuccessResponseDto<>("배달 정보가 성공적으로 삭제되었습니다.", null);
	}
}
