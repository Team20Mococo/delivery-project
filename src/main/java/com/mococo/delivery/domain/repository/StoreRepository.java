package com.mococo.delivery.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.mococo.delivery.domain.model.Store;

@NoRepositoryBean
public interface StoreRepository extends Repository<Store, UUID> {
	Optional<Store> findById(UUID storeId);

	Store save(Store store);

	Page<Store> findAll(Pageable pageable);

	Page<Store> findByOperationStatusTrue(Pageable pageable);

	Page<Store> findByNameContainingIgnoreCaseAndOperationStatusTrue(String searchQuery, Pageable pageable);

	Page<Store> findByNameContainingIgnoreCase(String searchQuery, Pageable pageable);

	Page<Store> findByOwnerUsername(String currentUsername, Pageable pageable);

	Page<Store> findByOperationStatusTrueAndNameContainingIgnoreCase(String searchQuery, Pageable pageable);

	Page<Store> findByOwnerUsernameAndNameContainingIgnoreCase(String currentUsername, String searchQuery, Pageable pageable);
}
