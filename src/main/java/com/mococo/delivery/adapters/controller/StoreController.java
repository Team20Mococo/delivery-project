package com.mococo.delivery.adapters.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.store.AddStoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreListResponseDto;
import com.mococo.delivery.application.dto.store.StoreRequestDto;
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
}
