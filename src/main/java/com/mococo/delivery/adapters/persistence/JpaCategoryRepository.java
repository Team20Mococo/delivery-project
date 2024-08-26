package com.mococo.delivery.adapters.persistence;

import com.mococo.delivery.domain.model.Category;
import com.mococo.delivery.domain.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaCategoryRepository extends CategoryRepository, JpaRepository<Category, String> {
}
