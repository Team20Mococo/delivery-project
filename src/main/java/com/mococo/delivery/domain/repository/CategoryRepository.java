package com.mococo.delivery.domain.repository;

import com.mococo.delivery.domain.model.Category;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;


@NoRepositoryBean
public interface CategoryRepository extends Repository<Category, String> {
    Optional<Category> findByName(String name);

    Category save(Category category);
}
