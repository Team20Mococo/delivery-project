package com.mococo.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.mococo.delivery.domain.model.Product;

@NoRepositoryBean
public interface ProductRepository extends Repository<Product, UUID> {
	Product save(Product product);
}
