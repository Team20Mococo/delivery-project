package com.mococo.delivery.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.mococo.delivery.domain.model.User;

@NoRepositoryBean
public interface UserRepository extends Repository<User, String> {
	Optional<User> findByUsername(String username);

	Optional<User> save(User user);
}
