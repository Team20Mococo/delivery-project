package com.mococo.delivery.application.service;

import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public Store getStore(UUID storeId) {
        return storeRepository.findById(storeId).orElse(null);
    }
}
