package com.mococo.delivery.application.service;

import java.util.Date;

import javax.crypto.SecretKey;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
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

		String password = request.getPassword();
		User user = userRepository.findByUsername(request.getUsername())
			.orElseThrow(EntityNotFoundException::new);

		//넘겨 받은 패스워드를 암호화 했을때 기존 DB 데이터 일치
		if (passwordEncoder.matches(password, user.getPassword())) {
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
				.claim(AUTHORIZATION_KEY, user.getRole().getAuthority()) // 사용자 권한
				.expiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
				.issuedAt(date) // 발급일
				.signWith(key, Jwts.SIG.HS512)
				.compact();
	}

	public Boolean signUp(SignUpRequestDto request) {
		return userRepository.save(
			User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.isPublic(request.getIsPublic())
				.build()
		).isPresent();
	}
}
