package com.mococo.delivery.application.service;

import com.mococo.delivery.application.dto.store.AddStoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreRequestDto;
import com.mococo.delivery.application.dto.store.StoreResponseDto;
import com.mococo.delivery.domain.model.Category;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.repository.CategoryRepository;
import com.mococo.delivery.domain.repository.StoreRepository;
import com.mococo.delivery.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Store getStore(UUID storeId) {
        return storeRepository.findById(storeId).orElse(null);
    }

    public AddStoreResponseDto addStore(StoreRequestDto request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }

        User owner = user.get();

        Optional<Category> categoryOpt = categoryRepository.findByName(request.getCategory());

        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
        }

        Category category = categoryOpt.get();

        Store store = Store.builder()
                .name(request.getName())
                .category(category)
                .notice(request.getNotice())
                .description(request.getDescription())
                .operationStatus(true)
                .owner(owner)
                .build();

        Store savedStore = storeRepository.save(store);

        StoreResponseDto storeResponseDto = StoreResponseDto.builder()
                .storeId(savedStore.getId())
                .username(request.getUsername())
                .name(savedStore.getName())
                .category(savedStore.getCategory().getName())
                .notice(savedStore.getNotice())
                .description(savedStore.getDescription())
                .createdAt(LocalDateTime.now())
                .createdBy("system")
                .build();

        return AddStoreResponseDto.builder().store(storeResponseDto).build();
    }
}
