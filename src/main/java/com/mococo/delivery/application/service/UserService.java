package com.mococo.delivery.application.service;

import java.util.Date;

import javax.crypto.SecretKey;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mococo.delivery.application.dto.user.LoginRequestDto;
import com.mococo.delivery.application.dto.user.SignUpRequestDto;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
	private String secretKey;
	// 사용자 권한 값의 KEY
	public static final String AUTHORIZATION_KEY = "auth";
	// Token 식별자
	public static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

	public User getUser(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	public User save(String username) {
		return userRepository.save(User.builder().username(username).build()).orElse(null);
	}

	public boolean verifyUser(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	public Boolean login(HttpServletResponse response, LoginRequestDto request) {
		String username = request.getUsername();
		String password = request.getPassword();
		User user = userRepository.findByUsername(username)
			.orElseThrow(EntityNotFoundException::new);
		if (user.getPassword().equals(passwordEncoder.encode(password))) {
			setAuthorities(response, user);
			return true;
		}
		return false;
	}

	private void setAuthorities(HttpServletResponse response, User user) {
		String token = createToken(user);
		response.setHeader("Authorization", token);
	}

	public String createToken(User user) {
		Date date = new Date();
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
		return BEARER_PREFIX +
			Jwts.builder()
				.subject(user.getUsername()) // 사용자 식별자값(ID)
				.claim(AUTHORIZATION_KEY, user.getRole()) // 사용자 권한
				.expiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
				.issuedAt(date) // 발급일
				.signWith(key, Jwts.SIG.HS512)
				.compact();
	}

	public Boolean signUp(SignUpRequestDto request) {
		return userRepository.save(
			User.builder()
				.username(request.getUsername())
				.password(request.getPassword())
				.isPublic(request.getIsPublic())
				.build()
		).isPresent();
	}
}
