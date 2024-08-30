package com.mococo.delivery.adapters.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mococo.delivery.application.service.UserService;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.model.enumeration.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final UserService userService;

	@Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
	private String secretKey;

	// Header KEY 값
	public static final String AUTHORIZATION_HEADER = "Authorization";
	// Token 식별자
	public static final String BEARER_PREFIX = "Bearer ";

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {

		// 인증 필요 여부 확인
		String path = request.getRequestURI();
		HttpMethod method = HttpMethod.valueOf(request.getMethod());
		if (path.endsWith("sign-up") || path.endsWith("log-in")) {
			log.info("인증/인가가 필요없는 요청입니다.");
			filterChain.doFilter(request, response);
		}

		// 토큰 추출 및 인증
		String token = getJwtFromHeader(request);
		if (token == null || !validateToken(token)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		// 권한 확인
		UserRole role = extractUserRole(token);
		boolean hasRole = RoleAccessLevel.of(role).getEndpointList().stream()
			.filter(endPoint -> Pattern.matches(endPoint.getEndPointName(), path))
			.anyMatch(endPoint -> endPoint.getMethod().toString().equals(method.toString()));
		// 롤이 있는지 확인 및 추출한 권한과 DB 권한 일치 하는지
		if (!hasRole || matchRole(role, token)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}

		filterChain.doFilter(request, response);
	}

	public String getJwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	// 토큰 검증
	public boolean validateToken(String token) {
		String username = extractUsername(token);
		if (username != null) {
			return userService.verifyUser(username);
		}
		return false;
	}

	public String extractUsername(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
			Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(key)
				.build().parseSignedClaims(token);
			return claimsJws.getPayload().getSubject();
		} catch (SignatureException | SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return null;
	}

	public UserRole extractUserRole(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
		Jws<Claims> claimsJws = Jwts.parser()
			.verifyWith(key)
			.build().parseSignedClaims(token);
		String authority = claimsJws.getPayload().get("auth").toString();
		return UserRole.of(authority);
	}

	public boolean matchRole(UserRole givenRole, String token) {
		UserRole realRole = userService.getUserRole(extractUsername(token));
		return givenRole == realRole;
	}
}
