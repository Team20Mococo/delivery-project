package com.mococo.delivery.domain.repository;

import com.mococo.delivery.domain.model.Store;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface StoreRepository extends Repository<Store, UUID> {
    Optional<Store> findById(UUID storeId);
}
