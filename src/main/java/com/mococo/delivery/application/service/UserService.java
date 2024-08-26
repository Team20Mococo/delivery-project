package com.mococo.delivery.application.service;

import org.springframework.stereotype.Service;

import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User getUser(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	public User save(String username) {
		return userRepository.save(User.builder().username(username).build()).orElse(null);
	}
}
