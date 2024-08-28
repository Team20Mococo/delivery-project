package com.mococo.delivery.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.mococo.delivery.domain.model.User;

@NoRepositoryBean
public interface UserRepository extends Repository<User, String> {
	Optional<User> findByUsername(String username);

	Optional<User> save(User user);

	Boolean delete(User user);

	Page<User> findByUsernameContainingIgnoreCaseAndDeletedAtIsNull(String username, Pageable pageable);

	Page<User> findByDeletedAtIsNull(Pageable pageable);
}
