package com.mococo.delivery.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.mococo.delivery.domain.model.Product;

@NoRepositoryBean
public interface ProductRepository extends Repository<Product, UUID> {
	Product save(Product product);

	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<Product> findByNameContainingIgnoreCaseAndIsPublicTrue(String name, Pageable pageable);

	Page<Product> findByIsPublicTrue(Pageable pageable);

	Page<Product> findAll(Pageable pageable);

	Optional<Product> findById(UUID productId);
}
