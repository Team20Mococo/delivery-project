package com.mococo.delivery.adapters.controller;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.store.AddStoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreRequestDto;
import com.mococo.delivery.application.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
