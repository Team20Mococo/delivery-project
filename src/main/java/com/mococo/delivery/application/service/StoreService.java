package com.mococo.delivery.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mococo.delivery.application.dto.PageInfoDto;
import com.mococo.delivery.application.dto.store.AddStoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreListResponseDto;
import com.mococo.delivery.application.dto.store.StoreRequestDto;
import com.mococo.delivery.application.dto.store.StoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreSimpleResponseDto;
import com.mococo.delivery.application.dto.store.UpdateStoreStatusRequestDto;
import com.mococo.delivery.domain.exception.entity.StoreAlreadyDeletedException;
import com.mococo.delivery.domain.exception.entity.StoreNotFoundException;
import com.mococo.delivery.domain.exception.entity.UnauthorizedStoreAccessException;
import com.mococo.delivery.domain.model.Category;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.repository.CategoryRepository;
import com.mococo.delivery.domain.repository.StoreRepository;
import com.mococo.delivery.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final AuditorAwareImpl auditorAware;

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
			.createdAt(savedStore.getCreatedAt())
			.createdBy(savedStore.getCreatedBy())
			.build();

		return AddStoreResponseDto.builder().store(storeResponseDto).build();
	}

	public StoreListResponseDto getAllStores(String sortBy, String direction, boolean filter, int page,
		int size, String searchQuery) {
		Sort sort;

		// 기본 정렬: createdAt과 updatedAt
		if (sortBy == null || sortBy.isEmpty()) {
			sort = Sort.by(
				direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
				"createdAt"
			).and(Sort.by(
				direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
				"updatedAt"
			));
		} else {
			sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC
				, sortBy);
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Store> storePage;

		if (searchQuery != null && !searchQuery.isEmpty()) {
			// 검색어와 필터를 조합한 쿼리
			if (filter) {
				storePage = storeRepository.findByNameContainingIgnoreCaseAndOperationStatusTrue(searchQuery, pageable);
			} else {
				storePage = storeRepository.findByNameContainingIgnoreCase(searchQuery, pageable);
			}
		} else {
			// 검색어가 없을 때의 처리
			if (filter) {
				storePage = storeRepository.findByOperationStatusTrue(pageable);
			} else {
				storePage = storeRepository.findAll(pageable);
			}
		}

		List<StoreSimpleResponseDto> storeList = storePage.getContent().stream()
			.map(store -> StoreSimpleResponseDto.builder()
				.storeId(store.getId())
				.name(store.getName())
				.category(store.getCategory().getName())
				.description(store.getDescription())
				.build())
			.collect(Collectors.toList());

		PageInfoDto pageInfo = PageInfoDto.builder()
			.totalItems(storePage.getTotalElements())
			.totalPages(storePage.getTotalPages())
			.currentPage(storePage.getNumber())
			.pageSize(storePage.getSize())
			.hasNextPage(storePage.hasNext())
			.build();

		return StoreListResponseDto.builder()
			.storeList(storeList)
			.pageInfo(pageInfo)
			.build();
	}

	@Transactional
	public StoreResponseDto deleteStore(UUID storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if (store.isDeleted()) {
			throw new StoreAlreadyDeletedException();
		}

		String currentUser = auditorAware.getCurrentAuditor().orElse("system");

		// 현재 사용자가 스토어의 소유자인지 확인
		if (!store.getOwner().getUsername().equals(currentUser)) {
			throw new UnauthorizedStoreAccessException();
		}

		store.softDelete(currentUser);
		Store savedStore = storeRepository.save(store);

		return StoreResponseDto.builder()
			.storeId(savedStore.getId())
			.username(savedStore.getOwner().getUsername())
			.name(savedStore.getName())
			.category(savedStore.getCategory().getName())
			.notice(savedStore.getNotice())
			.description(savedStore.getDescription())
			.createdAt(savedStore.getCreatedAt())
			.createdBy(savedStore.getCreatedBy())
			.updatedAt(savedStore.getUpdatedAt())
			.updatedBy(savedStore.getUpdatedBy())
			.deletedAt(savedStore.getDeletedAt())
			.deletedBy(savedStore.getDeletedBy())
			.build();
	}

	@Transactional
	public StoreResponseDto updateStoreStatus(UUID storeId, UpdateStoreStatusRequestDto requestDto) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		String currentUser = auditorAware.getCurrentAuditor().orElse("system");

		if (!store.getOwner().getUsername().equals(currentUser)) {
			throw new UnauthorizedStoreAccessException();
		}

		store.updateOperatingStatus(requestDto.getIsOperating());
		Store savedStore = storeRepository.save(store);

		return StoreResponseDto.builder()
			.storeId(savedStore.getId())
			.username(savedStore.getOwner().getUsername())
			.name(savedStore.getName())
			.category(savedStore.getCategory().getName())
			.notice(savedStore.getNotice())
			.description(savedStore.getDescription())
			.createdAt(savedStore.getCreatedAt())
			.createdBy(savedStore.getCreatedBy())
			.updatedAt(savedStore.getUpdatedAt())
			.updatedBy(savedStore.getUpdatedBy())
			.deletedAt(savedStore.getDeletedAt())
			.deletedBy(savedStore.getDeletedBy())
			.build();
	}
}
