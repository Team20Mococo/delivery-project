package com.mococo.delivery.adapters.persistence;

import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.repository.StoreRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaStoreRepository extends StoreRepository, JpaRepository<Store, UUID> {
}
