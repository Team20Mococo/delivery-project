package com.mococo.delivery.application.service;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mococo.delivery.application.dto.PageInfoDto;
import com.mococo.delivery.application.dto.user.LoginRequestDto;
import com.mococo.delivery.application.dto.user.SignUpRequestDto;
import com.mococo.delivery.application.dto.user.UserListResponseDto;
import com.mococo.delivery.application.dto.user.UserPutRequestDto;
import com.mococo.delivery.application.dto.user.UserResponseDto;
import com.mococo.delivery.application.dto.user.UserRolePatchRequestDto;
import com.mococo.delivery.domain.exception.entity.PasswordNotMatchedException;
import com.mococo.delivery.domain.exception.entity.UserNotFoundException;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.model.enumeration.UserRole;
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

	public UserResponseDto getUser(String username) {
		return UserResponseDto
			.of(userRepository.findByUsername(username)
				.orElseThrow(EntityNotFoundException::new)
			);
	}

	public UserListResponseDto getAllUsers(String sortBy, String direction, boolean filter, int page,
		int size, String searchQuery) {

		// 기본 정렬: createdAt, updatedAt
		Sort sort = Sort.by(
			direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
			"createdAt"
		).and(Sort.by(
			direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
			"updatedAt"
		));

		if (sortBy != null && !sortBy.isEmpty()) {
			sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC
				, sortBy);
		}

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<User> userPage;

		if (searchQuery != null && !searchQuery.isEmpty()) {
			// 검색어와 필터를 조합한 쿼리
			userPage = userRepository.findByUsernameContainingIgnoreCaseAndDeletedAtIsNull(searchQuery, pageable);

		} else {
			// 검색어가 없을 때의 처리
			userPage = userRepository.findByDeletedAtIsNull(pageable);
		}

		List<UserResponseDto> userResponseList = userPage.getContent().stream()
			.map(UserResponseDto::of)
			.toList();
		PageInfoDto pageInfo = PageInfoDto.builder()
			.totalItems(userPage.getTotalElements())
			.totalPages(userPage.getTotalPages())
			.currentPage(userPage.getNumber())
			.pageSize(userPage.getSize())
			.hasNextPage(userPage.hasNext())
			.build();
		return UserListResponseDto.of(userResponseList, pageInfo);
	}

	@Transactional
	public UserResponseDto updateUser(String username, UserPutRequestDto requestDto) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(EntityNotFoundException::new);
		user.modify(requestDto.getNickname(), requestDto.getAddress(), requestDto.isPublic());
		return UserResponseDto.of(user);
	}

	@Transactional
	public UserResponseDto updateRole(String username, UserRolePatchRequestDto requestDto) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(EntityNotFoundException::new);
		user.changeRole(UserRole.of(requestDto.getRoleName()));
		return UserResponseDto.of(user);
	}

	@Transactional
	public Boolean deleteUser(String username) {
		log.info(username);
		User user = userRepository.findByUsername(username)
			.orElseThrow();

		return userRepository.delete(user);
	}

	public Boolean signUp(SignUpRequestDto request) {
		return userRepository.save(
			User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.isPublic(request.getIsPublic())
				.role(UserRole.ROLE_CUSTOMER)
				.build()
		).isPresent();
	}

	public Boolean login(HttpServletResponse response, LoginRequestDto request) {

		String password = request.getPassword();
		User user = userRepository.findByUsername(request.getUsername())
			.orElseThrow(UserNotFoundException::new);

		//넘겨 받은 패스워드를 암호화 했을때 기존 DB 데이터 일치
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new PasswordNotMatchedException();
		}
		setAuthorities(response, user);
		return true;
	}

	public boolean verifyUser(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	private void setAuthorities(HttpServletResponse response, User user) {
		String token = createToken(user);
		response.setHeader("Authorization", token);
	}

	private String createToken(User user) {
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
}
