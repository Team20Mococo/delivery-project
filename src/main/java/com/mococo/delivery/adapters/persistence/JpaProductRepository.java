package com.mococo.delivery.adapters.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mococo.delivery.domain.model.Product;
import com.mococo.delivery.domain.repository.ProductRepository;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, UUID> {
}
