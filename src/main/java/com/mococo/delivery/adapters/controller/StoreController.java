package com.mococo.delivery.adapters.controller;

import java.util.UUID;

import com.mococo.delivery.domain.model.enumeration.UserRole;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.store.AddStoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreListResponseDto;
import com.mococo.delivery.application.dto.store.StoreRequestDto;
import com.mococo.delivery.application.dto.store.StoreResponseDto;
import com.mococo.delivery.application.dto.store.UpdateStoreStatusRequestDto;
import com.mococo.delivery.application.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping("/admin/stores")
	public SuccessResponseDto<AddStoreResponseDto> addStore(@RequestBody StoreRequestDto request) {
		AddStoreResponseDto response = storeService.addStore(request);
		return new SuccessResponseDto<>("성공했습니다.", response);
	}

	@GetMapping("/admin/stores")
	public SuccessResponseDto<StoreListResponseDto> getAllStoresByAdmin(
		@RequestParam(required = false) String sortBy,
		@RequestParam(defaultValue = "asc") String direction,
		@RequestParam(required = false) boolean filter,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String searchQuery
	) {
		StoreListResponseDto response = storeService.getAllStores(sortBy, direction, filter, page, size, searchQuery);
		return new SuccessResponseDto<StoreListResponseDto>("스토어 목록 조회에 성공했습니다.", response);
	}

	@DeleteMapping("/owner/stores/{storeId}")
	public SuccessResponseDto<StoreResponseDto> deleteStore(@PathVariable UUID storeId) {
		StoreResponseDto response = storeService.deleteStore(storeId);
		return new SuccessResponseDto<>("폐업 신청에 성공했습니다.", response);
	}

	@PatchMapping("/owner/stores/{storeId}/operation-status")
	public SuccessResponseDto<StoreResponseDto> updateStoreStatus(
		@PathVariable UUID storeId,
		@RequestBody UpdateStoreStatusRequestDto requestDto
	) {
		StoreResponseDto response = storeService.updateStoreStatus(storeId, requestDto);
		return new SuccessResponseDto<>("가게 운영 상태 변경에 성공했습니다.", response);
	}

	@GetMapping("/admin/stores/{storeId}")
	public SuccessResponseDto<StoreResponseDto> getStore(@PathVariable UUID storeId) {
		StoreResponseDto response = storeService.getOneStore(storeId);
		return new SuccessResponseDto<>("스토어 단건 조회에 성공했습니다.", response);
	}

	@GetMapping("/customer/stores")
	public SuccessResponseDto<StoreListResponseDto> getStoreListByCustomer(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		StoreListResponseDto response = storeService.getStores(UserRole.ROLE_CUSTOMER, page, size);
		return new SuccessResponseDto<>("가게 목록 조회에 성공했습니다.", response);
	}

	@GetMapping("/owner/stores")
	public SuccessResponseDto<StoreListResponseDto> getStoreListByOwner(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		StoreListResponseDto response = storeService.getStores(UserRole.ROLE_OWNER, page, size);
		return new SuccessResponseDto<>("내 가게 조회에 성공했습니다.", response);
	}
}
