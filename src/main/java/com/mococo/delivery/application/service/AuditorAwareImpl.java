package com.mococo.delivery.application.service;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.mococo.delivery.adapters.security.JwtFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

	private final HttpServletRequest request;
	private final JwtFilter jwtFilter;

	@Override
	public Optional<String> getCurrentAuditor() {
		String token = resolveToken(request);
		if (token != null) {
			return Optional.of(jwtFilter.extractUsername(token));
		}
		return Optional.of("system");
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
